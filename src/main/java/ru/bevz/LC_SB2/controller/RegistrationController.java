package ru.bevz.LC_SB2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.bevz.LC_SB2.domain.User;
import ru.bevz.LC_SB2.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Value("${recaptcha.html}")
    private String captchaHtml;

    private final UserService userService;

    private final ControllerUtils controllerUtils;

    public RegistrationController(UserService userService, ControllerUtils controllerUtils) {
        this.userService = userService;
        this.controllerUtils = controllerUtils;
    }

    @ModelAttribute(name = "captchaHtml")
    private String getCaptchaHtml() {
        return captchaHtml;
    }

    @GetMapping("/login")
    public String getLogin(Model model, HttpSession session) {

        if (session != null && session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION") != null) {
            model.addAttribute("message", "Invalid credentials!");
            model.addAttribute("messageType", "danger");
            session.setAttribute("SPRING_SECURITY_LAST_EXCEPTION", null);
        }

        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @Valid User user,
            BindingResult result,
            Model model
    ) {
        boolean validCaptcha = controllerUtils.checkCaptcha(captchaResponse);

        if (!validCaptcha) {
            //TODO: to need to implement by BindingResult
            model.addAttribute("captchaError", "fill captcha");
        }

        if (user.getPassword() != null && !user.getConfirmPassword().equals(user.getPassword())) {
            result.rejectValue("confirmPassword", "confirmPasswordError", "passwords are different");
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            result.rejectValue("email", "emailError", "this email already uses");
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            result.rejectValue("username", "usernameError", "user exists");
        }

        if (result.hasErrors() || !validCaptcha) {
            model.mergeAttributes(controllerUtils.getErrors(result));
            return "registration";
        }

        userService.addUser(user);

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivate = userService.activateUser(code);
        String message;
        String messageType;

        if (isActivate) {
            message = "User successfully activated!";
            messageType = "success";
        } else {
            message = "Activate code is not found!";
            messageType = "danger";
        }

        model.addAttribute("message", message);
        model.addAttribute("messageType", messageType);

        return "login";
    }
}
