package com.example.junit_bank_sample.config.dummy;

import com.example.junit_bank_sample.domain.account.Account;
import com.example.junit_bank_sample.domain.transaction.Transaction;
import com.example.junit_bank_sample.domain.transaction.TransactionEnum;
import com.example.junit_bank_sample.domain.user.User;
import com.example.junit_bank_sample.domain.user.UserEnum;
import com.example.junit_bank_sample.repository.AccountRepository;
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


    /* */
    protected Transaction newWithdrawTransaction(Account account, AccountRepository accountRepository ) {

        account.withdraw(100L); //1000원 이었다면 900원이 됨

        //더티채킹이 안되기 때문에
        if(accountRepository != null) {
            accountRepository.save(account);
        }
        
        /* 출금 트랜잭션 */
        Transaction transaction = Transaction.builder()
                                .withdrawAccount(account)
                                .depositAccount(null)
                                .withdrawAccountBalance(account.getBalance())
                                .depositAccountBalance(null)
                                .amount(100L)
                                .sender(account.getNumber()+"")
                                .gubun(TransactionEnum.WITHDRAW)
                                .receiver("ATM")
                                .build();

        return transaction;

    }




    /* 입금 */
    protected Transaction newDepositTransaction(Account account, AccountRepository accountRepository ) {

        account.deposit(100L); //1000원 이었다면 900원이 됨

        //더티채킹이 안되기 때문에
        if(accountRepository != null) {
            accountRepository.save(account);
        }

        /* 입금 트랜잭션 */
        Transaction transaction = Transaction.builder()
                .withdrawAccount(null)
                .depositAccount(account)
                .withdrawAccountBalance(null)
                .depositAccountBalance(account.getBalance())
                .amount(100L)
                .sender("ATM")
                .gubun(TransactionEnum.DEPOSIT)
                .receiver(account.getNumber()+"")
                .tel("01012345678")
                .build();

        return transaction;

    }



    protected Transaction newTransferTransaction(Account withdrawAccount, Account depositAccount, AccountRepository accountRepository ) {

        withdrawAccount.withdraw(100L); //1000원 이었다면 900원이 됨
        depositAccount.deposit(100L);

        //Controller 에서는 더티채킹이 안되기 때문에 강제로 처리 해준다.
        if(accountRepository != null) {
            accountRepository.save(withdrawAccount);
            accountRepository.save(depositAccount);
        }

        /* 출금 트랜잭션 */
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccount)
                .depositAccount(depositAccount)
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .depositAccountBalance(depositAccount.getBalance())
                .amount(100L)
                .gubun(TransactionEnum.TRANSFER)
                .sender(withdrawAccount.getNumber()+"")
                .receiver(depositAccount.getNumber()+"")
                .receiver("ATM")
                .build();

        return transaction;

    }


}
