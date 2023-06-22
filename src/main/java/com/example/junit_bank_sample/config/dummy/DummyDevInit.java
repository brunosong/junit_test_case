package com.example.junit_bank_sample.config.dummy;

import com.example.junit_bank_sample.domain.account.Account;
import com.example.junit_bank_sample.domain.user.User;
import com.example.junit_bank_sample.repository.AccountRepository;
import com.example.junit_bank_sample.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DummyDevInit extends DummyObject{


    /* 부트가 뜰때 자동으로 실행시켜 준다. */
    @Profile("dev") //dev 모드일때만 실행되야 한다.
    @Bean
    CommandLineRunner init(UserRepository userRepository, AccountRepository accountRepository) {
        return (args -> {
            // 서버 실행시에 무조건 실행된다.
            User ssar = userRepository.save(newUser("ssar", "쌀"));
            User cos = userRepository.save(newUser("cos", "코스"));

            Account ssarAccount = accountRepository.save(newAccount(1111L,ssar));
            Account cosAccount = accountRepository.save(newAccount(1112L,cos));

        });
    }


}
