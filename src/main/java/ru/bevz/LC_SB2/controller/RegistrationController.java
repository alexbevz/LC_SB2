package ru.bevz.LC_SB2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.bevz.LC_SB2.domain.User;
import ru.bevz.LC_SB2.domain.dto.CaptchaResponseDto;
import ru.bevz.LC_SB2.service.UserService;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class RegistrationController {

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    private final UserService userService;

    private final RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    @Value("${recaptcha.html}")
    private String recaptchaHtml;

    public RegistrationController(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    @ModelAttribute(name = "recaptchaKey")
    private String getRecaptchaHtml() {
        return recaptchaHtml;
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
        String url = String.format(CAPTCHA_URL, recaptchaSecret, captchaResponse);

        CaptchaResponseDto response =
                restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if (response != null && !response.isSuccess()) {
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

        if (result.hasErrors() || (response != null && !response.isSuccess())) {
            model.mergeAttributes(ControllerUtils.getErrors(result));
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
            model.addAttribute("messageType", "success");
        } else {
            model.addAttribute("message", "Activate code is not found!");
            model.addAttribute("messageType", "danger");
        }

        return "login";
    }
}
