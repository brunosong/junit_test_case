package com.example.junit_bank_sample.handler.ex;


public class CustomApiException extends RuntimeException {

    public CustomApiException(String message) {
        super(message);
    }
}
