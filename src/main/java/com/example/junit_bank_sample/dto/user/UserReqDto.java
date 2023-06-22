package com.example.junit_bank_sample.dto.user;

import com.example.junit_bank_sample.domain.user.User;
import com.example.junit_bank_sample.domain.user.UserEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserReqDto {

    @Setter
    @Getter
    public static class LoginReqDto {
        private String username;
        private String password;
    }


    @Getter
    @Setter
    public static class JoinReqDto {

        // 영문, 숫자는 되고, 길이 최소 2~20자 이내
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$" , message = "영문/숫자 2~20자 이내로 작성해주세요" )
        @NotEmpty
        private String username;

        // 길이 4~20
        @NotEmpty
        @Size(min = 4,max = 20)
        private String password;

        @Pattern(regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 작성해주세요")
        @NotEmpty
        private String email;

        // 영어, 한글, 1~20
        @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣]{1,20}$" , message = "한글/영문 1~20자 이내로 작성해주세요")
        private String fullname;

        public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder){
            return User.builder()
                    .username(username)
                    .password(bCryptPasswordEncoder.encode(password))
                    .email(email)
                    .fullname(fullname)
                    .role(UserEnum.CUSTOMER)
                    .build();
        }

    }
}
