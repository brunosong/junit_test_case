package com.example.junit_bank_sample.dto.account;

import com.example.junit_bank_sample.domain.account.Account;
import com.example.junit_bank_sample.domain.transaction.Transaction;
import com.example.junit_bank_sample.domain.user.User;
import com.example.junit_bank_sample.util.CustomDateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountRespDto {

    // AccountWithdrawRespDto == AccountDepositRespDto 거의 같더라도 재사용은 하지 말자
    // 이유는 나중에 만약에 출금쪽에 뭔가 바뀌게 되서 DTO 가 변경된다면 망한다.
    // 독립적으로 만들자

    @Getter
    @Setter
    public static class AccountTransferRespDto {
        private Long id;  //계좌 ID
        private Long number; //계좌 번호
        private Long balance; //출금 계좌 잔액
        private TransactionDto transaction;

        public AccountTransferRespDto(Account account , Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDto(transaction);
        }

        @Setter
        @Getter
        public class TransactionDto {

            private Long id;
            private String gubun;
            private String sender;
            private String receive;
            private Long amount;

            //@JsonIgnore
            private Long depositAccountBalance;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.receive = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.depositAccountBalance = transaction.getDepositAccountBalance();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }




    @Getter
    @Setter
    public static class AccountWithdrawRespDto {
        private Long id;  //계좌 ID
        private Long number; //계좌 번호
        private Long balance; //잔액
        private TransactionDto transaction;

        public AccountWithdrawRespDto(Account account , Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDto(transaction);
        }

        @Setter
        @Getter
        public class TransactionDto {

            private Long id;
            private String gubun;
            private String sender;
            private String receive;
            private Long amount;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.receive = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }


    @Getter
    @Setter
    public static class AccountDepositRespDto {
        private Long id;  //계좌 ID
        private Long number; //계좌 번호
        private TransactionDto transaction;

        public AccountDepositRespDto(Account account , Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.transaction = new TransactionDto(transaction);
        }

        @Setter
        @Getter
        public class TransactionDto {

            private Long id;
            private String gubun;
            private String sender;
            private String receive;
            private Long amount;

            @JsonIgnore
            private Long depositAccountBalance; //클라이언트에게 전달 X -> 서비스단 테스트 용도

            private String tel;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.receive = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.depositAccountBalance = transaction.getDepositAccountBalance();
                this.tel = transaction.getTel();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Setter
    @Getter
    public static class AccountSaveReqDto {

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;

        public Account toEntity(User user){
            return Account.builder()
                    .number(number)
                    .password(password)
                    .balance(1000L)
                    .user(user)
                    .build();
        }

    }


    @Setter
    @Getter
    public static class AccountListRespDto {
        private String fullname;
        private List<AccountDto> accounts = new ArrayList<>();

        public AccountListRespDto(User user,List<Account> accounts) {
            this.fullname = user.getFullname();
            //this.accounts = accounts.stream().map((account -> new AccountDto(account))).collect(Collectors.toList());
            this.accounts = accounts.stream().map((AccountDto::new)).collect(Collectors.toList());
        }

        public class AccountDto {
            private Long id;
            private Long number;
            private Long balance;

            public AccountDto(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }





}
