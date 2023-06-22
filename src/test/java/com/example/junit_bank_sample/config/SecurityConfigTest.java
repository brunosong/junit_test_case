package com.example.junit_bank_sample.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Sql("classpath:db/teardown.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)  //가짜 환경에서 테스트를 하겠다.
class SecurityConfigTest {


    //가짜 환경에 등록된 MockMvc 를 DI함.
    @Autowired
    private MockMvc mvc;

    // 서버는 일관성 있게 에러가 리턴되어야 한다.
    // 내가 모르는 에러가 프론트한테 날라가지 않게, 내가 직접 다 제어하자.
    @Test
    void authentication_test() throws Exception {

        //given


        //when
        ResultActions resultActions = mvc.perform(get("/api/s/hello"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int status = resultActions.andReturn().getResponse().getStatus();
        System.out.println("status : " +  status);
        System.out.println("테스트 : " + responseBody);

        //then
        Assertions.assertThat(status).isEqualTo(401);


    }

    @Test
    void authorization_test() throws Exception {

        //when
        ResultActions resultActions = mvc.perform(get("/api/admin/hello"));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int status = resultActions.andReturn().getResponse().getStatus();

        System.out.println("status : " +  status);
        System.out.println("테스트 : " + responseBody);

        //then
        Assertions.assertThat(status).isEqualTo(401);


    }
}