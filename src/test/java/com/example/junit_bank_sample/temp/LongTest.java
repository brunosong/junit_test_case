package com.example.junit_bank_sample.temp;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LongTest {

    @Test
    void long_test() {

        //given
        Long number1 = 1111L;
        Long number2 = 1111L;

        //when
        if(number1.longValue() == number2.longValue()) {
            System.out.println("테스트 : 동일하다");
        } else {
            System.out.println("테스트 : 동일하지 않다.");
        }

        Long amount1 = 1111L;
        Long amount2 = 1111L;

        //when
        if(amount1 < amount2) {
            System.out.println("테스트 : amount1이 작습니다.");
        } else {
            System.out.println("테스트 : amount1이 큽니다.");
        }


        //then
        //assertThat()
    }

}
