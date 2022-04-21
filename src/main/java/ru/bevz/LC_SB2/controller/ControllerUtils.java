package ru.bevz.LC_SB2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.client.RestTemplate;
import ru.bevz.LC_SB2.domain.dto.CaptchaResponseDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class ControllerUtils {

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    private final RestTemplate restTemplate;

    public ControllerUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String field = fieldError.getField() + "Error";
            if (errors.containsKey(field)) {
                errors.put(field, errors.get(field) + " and " + fieldError.getDefaultMessage());
            } else {
                errors.put(field, fieldError.getDefaultMessage());
            }
        }

        return errors;
    }

    public boolean checkCaptcha(String captchaResponse) {
        String url = String.format(CAPTCHA_URL, recaptchaSecret, captchaResponse);

        CaptchaResponseDto response =
                restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        return response != null && response.isSuccess();
    }

}
