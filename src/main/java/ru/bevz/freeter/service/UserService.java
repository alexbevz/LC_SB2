package ru.bevz.freeter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bevz.freeter.domain.Role;
import ru.bevz.freeter.domain.User;
import ru.bevz.freeter.repos.UserRepo;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;

    @Value("${hostname}")
    private String hostname;

    public UserService(UserRepo userRepo, EmailSenderService emailSenderService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User byUsername = userRepo.findByUsername(username);

        if (byUsername == null) {
            throw new UsernameNotFoundException("User already exists");
        }

        return byUsername;
    }

    public boolean addUser(User user) {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            return false;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        sendMessage(user);
        return true;
    }

    private void sendMessage(User user) {
        if (!user.getEmail().isBlank() && !user.getEmail().isEmpty()) {
            String message = "Hello, " + user.getUsername() + "! Your code activation: " + user.getActivationCode()
                    + "\n Or you may visit the next link: http://%s/activate/".formatted(hostname) + user.getActivationCode()
                    + "\n Thanks for attention!";
            Thread thread = new Thread(() -> emailSenderService.send(user.getEmail(), "Activation code", message));
            thread.start();
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);
        userRepo.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();
        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));

        if (isEmailChanged) {
            user.setEmail(email);

            if (!email.isEmpty() && !email.isBlank()) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if (!password.isEmpty() && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepo.save(user);

        if (isEmailChanged) {
            sendMessage(user);
        }

    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public void subscribeUser(User channelUser, User subscriber) {
        channelUser.getSubscribers().add(subscriber);
        userRepo.save(channelUser);
    }

    public void unsubscribeUser(User channelUser, User unsubscribed) {
        channelUser.getSubscribers().remove(unsubscribed);
        userRepo.save(channelUser);
    }
}
