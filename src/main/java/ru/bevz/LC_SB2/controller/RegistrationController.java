package ru.bevz.LC_SB2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.bevz.LC_SB2.domain.User;
import ru.bevz.LC_SB2.service.UserService;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {

        if (user.getPassword() != null && !user.getConfirmPassword().equals(user.getPassword())) {
            bindingResult.rejectValue("confirmPassword", "confirmPasswordError", "passwords are different");
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            bindingResult.rejectValue("email", "emailError", "this email already uses");
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            bindingResult.rejectValue("username", "usernameError", "user exists");
        }

        if (bindingResult.hasErrors()) {
            model.mergeAttributes(ControllerUtils.getErrors(bindingResult));
            return "registration";
        }

        userService.addUser(user);

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivate = userService.activateUser(code);

        if (isActivate) {
            model.addAttribute("message", "User successfully activated!");
        } else {
            model.addAttribute("message", "Activate code is not found!");
        }

        return "login";
    }
}
