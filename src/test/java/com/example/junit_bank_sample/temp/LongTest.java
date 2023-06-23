package com.example.junit_bank_sample.temp;

import com.example.junit_bank_sample.domain.user.UserEnum;
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


    @Test
    void long_test2() {

        Long v1 = 128L;
        Long v2 = 128L;

        /* 단순히 크고 작다는 비교가 가능하다. */
        if(v1 < v2) System.out.println("크다");

        /* 127까지는 되는데 그 위 숫자는 동일성 비교가 되지 않는다. */
        if(v1 == v2) {
            System.out.println("같습니다.");
        }
    }


    @Test
    void long_test3() {

        Long v1 = 128L;
        Long v2 = 128L;

        assertThat(v1).isEqualTo(v2);
    }


}
