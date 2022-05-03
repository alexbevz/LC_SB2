package ru.bevz.freeter.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.bevz.freeter.domain.Role;
import ru.bevz.freeter.domain.User;
import ru.bevz.freeter.service.UserService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        List<User> userList = userService.findAll();

        model.addAttribute("users", userList);

        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditFrom(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) {

        userService.saveUser(user, username, form);

        return "redirect:/user";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email
    ) {

        userService.updateProfile(user, password, email);

        return "redirect:/user/profile";
    }

    @GetMapping("/subscribe/{user}")
    public String subscribeChannel(
            @AuthenticationPrincipal User user,
            @PathVariable("user") User userChannel,
            RedirectAttributes redirectAttributes
    ) {

        userService.subscribeUser(userChannel, user);
        redirectAttributes.addFlashAttribute("messageSubscribe", "You are success subscribed!");

        return "redirect:/user-messages/" + userChannel.getId();
    }

    @GetMapping("/unsubscribe/{user}")
    public String unsubscribeChannel(
            @AuthenticationPrincipal User user,
            @PathVariable("user") User userChannel,
            RedirectAttributes redirectAttributes
    ) {

        userService.unsubscribeUser(userChannel, user);
        redirectAttributes.addFlashAttribute("messageUnsubscribe", "You are success unsubscribed!");

        return "redirect:/user-messages/" + userChannel.getId();
    }

    @GetMapping("/{type}/{user}/list")
    public String userList(
            Model model,
            @PathVariable User user,
            @PathVariable String type
    ) {

        model.addAttribute("userChannel", user);
        model.addAttribute("type", (type.substring(0, 1).toUpperCase() + type.substring(1)));

        if (type.equals("subscriptions")) {
            model.addAttribute("users", user.getSubscriptions());
        } else if (type.equals("subscribers")) {
            model.addAttribute("users", user.getSubscribers());
        } else {
            return "error";
        }

        return "subscriptions";
    }
}
