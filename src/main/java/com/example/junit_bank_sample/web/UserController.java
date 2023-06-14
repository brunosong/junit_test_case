package com.example.junit_bank_sample.web;

import com.example.junit_bank_sample.dto.ResponseDto;
import com.example.junit_bank_sample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import com.example.junit_bank_sample.dto.user.UserReqDto.JoinReqDto;
import com.example.junit_bank_sample.dto.user.UserRespDto.JoinRespDto;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinReqDto reqDto , BindingResult bindingResult) {

        JoinRespDto joinRespDto = userService.회원가입(reqDto);
        return new ResponseEntity<>(new ResponseDto<>(1,"회원가입 성공" , joinRespDto),HttpStatus.CREATED);
    }

}
