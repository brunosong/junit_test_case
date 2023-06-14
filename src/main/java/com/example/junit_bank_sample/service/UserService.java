package com.example.junit_bank_sample.service;

import com.example.junit_bank_sample.domain.user.User;
import com.example.junit_bank_sample.handler.ex.CustomApiException;
import com.example.junit_bank_sample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.example.junit_bank_sample.dto.user.UserReqDto.JoinReqDto;
import com.example.junit_bank_sample.dto.user.UserRespDto.JoinRespDto;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinRespDto 회원가입(JoinReqDto joinReqDto) {

        //1. 회원이 있는지 검색 (동일한 이름의)
        Optional<User> user = userRepository.findByUsername(joinReqDto.getUsername());

        if(user.isPresent()){
            throw new CustomApiException("기존 회원이 있습니다.");
        }

        //2. 비번을 암호화 하고 저장한다.
        User userPS = userRepository.save(joinReqDto.toEntity(bCryptPasswordEncoder));

        //3. Dto 응답한다.
        return new JoinRespDto(userPS);

    }







}
