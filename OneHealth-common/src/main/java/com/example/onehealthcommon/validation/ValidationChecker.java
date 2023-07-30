package com.example.onehealthcommon.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
@Slf4j

public class ValidationChecker {

    private ValidationChecker() {}

    public  static StringBuilder checkValidation(BindingResult bindingResult) {
        StringBuilder errorBuilder = new StringBuilder();
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> errorBuilder.append(error.getDefaultMessage()).append("\n"));
            log.info("checkValidation() in UserServiceImpl found errors");
        }
        return errorBuilder;
    }
}
