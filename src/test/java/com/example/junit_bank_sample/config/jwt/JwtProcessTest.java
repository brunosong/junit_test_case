package com.example.junit_bank_sample.config.jwt;

import com.example.junit_bank_sample.config.auth.LoginUser;
import com.example.junit_bank_sample.domain.user.User;
import com.example.junit_bank_sample.domain.user.UserEnum;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtProcessTest {

    @Test
    void create_test() {

        //given
        String jwtToken = create_token();
        System.out.println(jwtToken);

        //then
        assertTrue(jwtToken.startsWith(JwtVO.TOKEN_PREFIX));

    }



    @Test
    void verify_test() {

        String jwtToken = create_token();
        //when
        LoginUser loginUser2 = JwtProcess.verify(jwtToken.replaceAll(JwtVO.TOKEN_PREFIX, ""));

        //then
        assertThat(loginUser2.getUser().getId()).isEqualTo(1L);
        assertThat(loginUser2.getUser().getRole()).isEqualTo(UserEnum.CUSTOMER);

    }


    public String create_token() {
        //given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser1 = new LoginUser(user);

        //when
        String jwtToken = JwtProcess.create(loginUser1);

        return jwtToken;
    }


}