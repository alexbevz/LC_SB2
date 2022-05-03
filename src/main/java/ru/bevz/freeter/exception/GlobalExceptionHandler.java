package ru.bevz.freeter.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public String handleAnyException(
            Exception exception,
            Model model
    ) {

        model.addAttribute("infoError", exception.getMessage());

        return "error";
    }
}
