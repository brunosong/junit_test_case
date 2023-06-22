package com.example.junit_bank_sample.config.dummy;

import com.example.junit_bank_sample.domain.account.Account;
import com.example.junit_bank_sample.domain.transaction.Transaction;
import com.example.junit_bank_sample.domain.transaction.TransactionEnum;
import com.example.junit_bank_sample.domain.user.User;
import com.example.junit_bank_sample.domain.user.UserEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class DummyObject {

    protected User newUser(String username, String fullname){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("1234");

        return User.builder()
                .username(username)
                .password(password)
                .email(username + "@gmail.com")
                .fullname(fullname)
                .role(UserEnum.CUSTOMER)
                .build();

    }

    protected User newMockUser(Long id, String username, String fullname){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("1234");
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(username + "@gmail.com")
                .fullname(fullname)
                .role(UserEnum.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Account newAccount(Long number , User user){

        return Account.builder()
                .number(number)
                .password(1234L)
                .balance(1000L)
                .user(user)
                .build();

    }


    protected Account newMockAccount(Long id, Long number, Long balance, User user){
        return Account.builder()
                .id(id)
                .number(number)
                .password(1234L)
                .balance(balance)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }


    //계좌 1111L
    //입금 트랜잭션 -> 계좌 1100원 변경 -> 입금 트랜잭션 히스토리가 생성되어야 함.
    protected Transaction newMockDepositTransaction(Long id, Account account) {
        account.deposit(100L);
        return Transaction.builder()
                .id(id)
                .withdrawAccount(null)    // 출금 계좌
                .depositAccount(account)  // 입금 계좌
                .withdrawAccountBalance(null)  
                .depositAccountBalance(account.getBalance())     // 입금 계좌 잔액
                .amount(100L)
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(account.getNumber()+"")
                .tel("01074637177")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }


}
