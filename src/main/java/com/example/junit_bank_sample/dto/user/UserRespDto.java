package com.example.junit_bank_sample.dto.user;

import com.example.junit_bank_sample.domain.user.User;
import com.example.junit_bank_sample.util.CustomDateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class UserRespDto {

    @Setter
    @Getter
    public static class LoginRespDto {
        private Long id;
        private String username;
        private String createdAt;

        public LoginRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.createdAt = CustomDateUtil.toStringFormat(user.getCreatedAt());
        }
    }

    @ToString
    @Getter
    @Setter
    public static class JoinRespDto {
        private Long id;
        private String username;
        private String email;
        private String fullname;

        public JoinRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.fullname = user.getFullname();
        }
    }
}
