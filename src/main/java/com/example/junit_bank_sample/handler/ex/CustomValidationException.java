package com.example.junit_bank_sample.handler.ex;


import lombok.Getter;

import java.util.Map;

@Getter
public class CustomValidationException extends RuntimeException {

    private Map<String,Object> errorMap;

    public CustomValidationException(String message, Map<String, Object> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }

}
