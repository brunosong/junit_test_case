package com.example.junit_bank_sample.dto.user;

import com.example.junit_bank_sample.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class UserRespDto {
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
