package com.example.junit_bank_sample.temp;

import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotEmpty;
import java.util.regex.Pattern;

// java.util.regex.Pattern
public class RegexTest {

    @Test
    void 한글만된다() {

        // "[가-힣]" 이렇게 하면 가 한글자만 된다. "가나"를 넣으면 false 를 리턴한다.
        // "[가-힣]*" 로 해버리면 *는 그 전 글자가 없어도 true를 반환하기 때문에 +로 해줘야 한다.
        String value = "가나다";
        boolean result = Pattern.matches("^[가-힣]+$",value);
        System.out.println(result);

    }


    @Test
    void 한글은안된다() {

        String value = "ㅇㅇㅇ";
        boolean result = Pattern.matches("^[^ㄱ-ㅎ가-힣]+$",value);
        System.out.println(result);

    }

    @Test
    void 영어만된다() {

        String value = "ABC";
        boolean result = Pattern.matches("^[a-zA-Z]+$",value);
        System.out.println(result);
    }

    @Test
    void 영어는안된다() {

        // 공백도 되게 하려고 *를 붙인다.
        String value = "";
        boolean result = Pattern.matches("^[^a-zA-Z]*$",value);
        System.out.println(result);


    }


    @Test
    void 영어와숫자만된다() {
        String value = "ABC2222";
        boolean result = Pattern.matches("^[a-zA-Z0-9]+$",value);
        System.out.println(result);
    }

    @Test
    void 영어만되고_길이는최소2최대4이다() {

        String value = "ABCD";
        boolean result = Pattern.matches("^[a-zA-Z]{2,4}$",value);
        System.out.println(result);

    }


    @Test
    void account_gubun_test() {

        //String gubun = "DEPOSIT";
        String gubun = "TRANSFER";

        boolean result = Pattern.matches("^(DEPOSIT|TRANSFER)$",gubun);
        System.out.println(result);

    }


    @Test
    void account_tel_test1() {

        String tel = "01074637177";
        boolean result = Pattern.matches("^[0-9]{3}[0-9]{4}[0-9]{4}",tel);
        System.out.println(result);

    }

    @Test
    void account_tel_test2() {

        String tel = "010-7463-7177";
        boolean result = Pattern.matches("^[0-9]{11}|^[0-9]{3}-[0-9]{4}-[0-9]{4}",tel);
        System.out.println(result);

    }


}
