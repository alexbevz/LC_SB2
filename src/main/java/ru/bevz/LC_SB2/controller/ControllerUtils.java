package ru.bevz.LC_SB2.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class ControllerUtils {

    static Map<String, String> getErrors(BindingResult bindingResult) {
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

}
