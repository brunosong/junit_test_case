package com.example.junit_bank_sample.service;


import com.example.junit_bank_sample.config.dummy.DummyObject;
import com.example.junit_bank_sample.domain.user.User;
import com.example.junit_bank_sample.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import com.example.junit_bank_sample.dto.user.UserReqDto.JoinReqDto;
import com.example.junit_bank_sample.dto.user.UserRespDto.JoinRespDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)  //서비스만 테스트를 하기 위해서 사용된다.
class UserServiceTest extends DummyObject {

    @InjectMocks
    private UserService userService;

    @Mock  //실제로 띄우는게 아니라 가짜로 띄워 @InjectMocks 에 주입이 된다.
    private UserRepository userRepository;

    @Spy //이거는 진짜 객체로 스프링 IOC 들어 있는 객체를 꺼내와서 가짜 객체에 넣어줄수 있다.
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void 회원가입_test() {

        JoinReqDto joinReqDto = new JoinReqDto();

        joinReqDto.setUsername("brunosong");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("brunosong@gmail.com");
        joinReqDto.setFullname("song");


        //stub
        //니가 이런걸 실행하면 이런걸 리턴할꺼야 라는 가설
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());   //findByUsername이 실행이 되면 Optional.empty()를 리턴해준다.

        User user = newMockUser(1L, "brunosong", "song");
        //stub
        when(userRepository.save(any())).thenReturn(user);   // userRepository 에 save 메소드가 호출되면 user 를 반환해줘라


        //회원가입
        JoinRespDto joinRespDto = userService.회원가입(joinReqDto);

        //then
        Assertions.assertThat(joinRespDto.getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(joinRespDto.getId()).isEqualTo(user.getId());
    }


}