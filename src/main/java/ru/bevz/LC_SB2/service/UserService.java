package ru.bevz.LC_SB2.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.bevz.LC_SB2.domain.Role;
import ru.bevz.LC_SB2.domain.User;
import ru.bevz.LC_SB2.repos.UserRepo;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;

    private final EmailSenderService emailSenderService;

    public UserService(UserRepo userRepo, EmailSenderService emailSenderService) {
        this.userRepo = userRepo;
        this.emailSenderService = emailSenderService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);

        if (!user.getEmail().isBlank() && !user.getEmail().isEmpty()) {
            String message = "Hello, " + user.getUsername() + "! Your code activation: " + user.getActivationCode()
                    + "\n Or you may visit the next link: http://localhost:8080/activate/" + user.getActivationCode()
                    + "\n Thanks for attention!";
            Thread thread = new Thread(() -> emailSenderService.send(user.getEmail(), "Activation code", message));
            thread.start();
        }

        return true;
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
}
