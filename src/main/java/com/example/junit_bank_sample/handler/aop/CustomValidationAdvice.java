package com.example.junit_bank_sample.handler.aop;

import com.example.junit_bank_sample.dto.ResponseDto;
import com.example.junit_bank_sample.handler.ex.CustomValidationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class CustomValidationAdvice {

    //포인트컷
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping() {}

    @Around("postMapping() || putMapping()")
    public Object validationAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs(); //joinPoint 에 매개변수다

        for (Object arg: args) {
            if(arg instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult)arg;

                if(bindingResult.hasErrors()) {

                    Map<String,Object> errorMap = new HashMap<>();

                    for(FieldError fieldError :  bindingResult.getFieldErrors()) {
                        errorMap.put(fieldError.getField(),fieldError.getDefaultMessage());
                    }

                    throw new CustomValidationException("유효성검사 실패" ,errorMap);
                }

            }
        }

        return joinPoint.proceed();
    }

}


/*
* get , delete , post , put  post 와 put 은 바디데이터가 존재한다.
* */
