package com.example.junit_bank_sample.web;


import com.example.junit_bank_sample.config.dummy.DummyObject;
import com.example.junit_bank_sample.domain.user.User;
import com.example.junit_bank_sample.dto.user.UserReqDto.JoinReqDto;
import com.example.junit_bank_sample.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class UserControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void setup(){
        dataSetting();
    }


    @Test
    public void join_success_test() throws Exception {

        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("love");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("brunosong@gmail.com");
        joinReqDto.setFullname("song");

        String reqBody = objectMapper.writeValueAsString(joinReqDto);

        //when
        ResultActions resultActions = mvc.perform( post("/api/join")
                                                    .content(reqBody)
                                                    .contentType(MediaType.APPLICATION_JSON) );

//        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
//        System.out.println(contentAsString);

        //then
        resultActions.andExpect(status().isCreated());

    }


    @Test
    public void join_fail_test() throws Exception {

        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("ssar");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("brunosong@gmail.com");
        joinReqDto.setFullname("song");

        String reqBody = objectMapper.writeValueAsString(joinReqDto);

        //when
        ResultActions resultActions = mvc.perform( post("/api/join")
                .content(reqBody)
                .contentType(MediaType.APPLICATION_JSON) );

        //then
        resultActions.andExpect(status().isBadRequest());

    }

    private void dataSetting() {
        userRepository.save(newUser("ssar","쌀"));
    }


}