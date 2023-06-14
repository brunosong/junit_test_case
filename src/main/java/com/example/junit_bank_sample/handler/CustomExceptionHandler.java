package com.example.junit_bank_sample.handler;

import com.example.junit_bank_sample.dto.ResponseDto;
import com.example.junit_bank_sample.handler.ex.CustomApiException;
import com.example.junit_bank_sample.handler.ex.CustomValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto(-1, "동일한 username 이 존재합니다." , null), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<?> validationException(CustomValidationException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto(-1, "유효성 검사가 실패하였습니다.." , e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }

}
