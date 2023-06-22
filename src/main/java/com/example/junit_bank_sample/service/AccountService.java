package com.example.junit_bank_sample.service;

import com.example.junit_bank_sample.domain.account.Account;
import com.example.junit_bank_sample.domain.transaction.Transaction;
import com.example.junit_bank_sample.domain.transaction.TransactionEnum;
import com.example.junit_bank_sample.domain.user.User;
import com.example.junit_bank_sample.dto.account.AccountReqDto.AccountDepositReqDto;
import com.example.junit_bank_sample.dto.account.AccountReqDto.AccountSaveRespDto;
import com.example.junit_bank_sample.dto.account.AccountReqDto.AccountTransferReqDto;
import com.example.junit_bank_sample.dto.account.AccountReqDto.AccountWithdrawReqDto;
import com.example.junit_bank_sample.dto.account.AccountRespDto;
import com.example.junit_bank_sample.dto.account.AccountRespDto.*;
import com.example.junit_bank_sample.handler.ex.CustomApiException;
import com.example.junit_bank_sample.repository.AccountRepository;
import com.example.junit_bank_sample.repository.TransactionRepository;
import com.example.junit_bank_sample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    public AccountListRespDto 계좌목록보기_유저별(Long userId) {

        User userPS = userRepository.findById(userId).orElseThrow(
                () -> new CustomApiException("유저를 찾을수 없습니다.")
        );

        //유저가 가지고 있는 모든 목록이 나온다.
        List<Account> accountList = accountRepository.findByUser_id(userId);

        return new AccountListRespDto(userPS,accountList);

    }

    @Transactional
    public AccountSaveRespDto 계좌등록(AccountSaveReqDto accountSaveReqDto, Long userId ) {
        // User 로그인되어 있는 상태 ( 로그인 상태가 되어있는지는 컨트롤러에서 체크하기 때문에 서비스는 그부분은 신경쓰지 않는다 )
        // User 가 DB에 있는지 검증
        User userPS = userRepository.findById(userId).orElseThrow(
                () -> new CustomApiException("유저를 찾을수 없습니다.")
        );

        // 해당계좌가 DB에 있는 중복여부를 체크
        Optional<Account> accountOP = accountRepository.findByNumber(accountSaveReqDto.getNumber());
        if(accountOP.isPresent()) {
            throw new CustomApiException("해당계좌가 이미 존재합니다.");
        }

        // 계좌 등록
        Account accountPS = accountRepository.save(accountSaveReqDto.toEntity(userPS));

        // DTO 응답
        return new AccountSaveRespDto(accountPS);


    }

    @Transactional
    public void 계좌삭제(Long number, Long userId) {

        //1. 계좌확인
        Account accountPS = accountRepository.findByNumber(number).orElseThrow(
                () -> new CustomApiException("계좌를 찾을수 없습니다.")
        );

        //2. 계좌 소유자 확인
        accountPS.checkOwner(userId);

        //3. 계좌 삭제
        accountRepository.deleteById(accountPS.getId());

    }


    @Transactional
    public AccountDepositRespDto 계좌입금(AccountDepositReqDto accountDepositReqDto) {   // ATM 기기 -> 누군가의 계좌

        // 0원 체크
        if(accountDepositReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        // 입금 계좌 확인
        Account depositAccountPS = accountRepository.findByNumber(accountDepositReqDto.getNumber())
                .orElseThrow( () -> new CustomApiException("계좌가 없습니다.") );

        // 입금 ( 해당 계좌 balance 조정 - update 문 - 더티 체킹 )
        depositAccountPS.deposit(accountDepositReqDto.getAmount());

        // 거래내역 남기기
        Transaction transaction = Transaction.builder()
                .depositAccount(depositAccountPS)
                .withdrawAccount(null)
                .depositAccountBalance(depositAccountPS.getBalance())
                .amount(accountDepositReqDto.getAmount())
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(accountDepositReqDto.getNumber()+"")
                .tel(accountDepositReqDto.getTel())
                .build();

        Transaction transactionPS = transactionRepository.save(transaction);

        return new AccountDepositRespDto(depositAccountPS, transactionPS);

    }

    @Transactional
    public AccountWithdrawRespDto 계좌출금(AccountWithdrawReqDto accountWithdrawReqDto, Long userId) {

        // 0원 체크
        if(accountWithdrawReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        // 출금 계좌 체크
        Account withdrawAccountPS = accountRepository.findByNumber(accountWithdrawReqDto.getNumber())
                .orElseThrow( () -> new CustomApiException("출금 계좌가 없습니다.") );


        // 출금 소유자 확인 ( 로그인한 사람과 동일한지 )
        withdrawAccountPS.checkOwner(userId);

        // 출금계좌 비밀번호 확인
        withdrawAccountPS.checkSamePassword(accountWithdrawReqDto.getPassword());

        // 출금계좌 잔액확인
        withdrawAccountPS.checkBalance(accountWithdrawReqDto.getAmount());

        // 출금하기
        withdrawAccountPS.withdraw(accountWithdrawReqDto.getAmount());

        // 거래내역 남기기 ( 내 계좌에서 ATM 으로 출금 )
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccountPS) //출금 계좌
                .depositAccount(null)               //입금 계좌
                .withdrawAccountBalance(withdrawAccountPS.getBalance())   //출금계좌 잔액
                .depositAccountBalance(null)                              //입금계좌 잔액
                .amount(accountWithdrawReqDto.getAmount())                //입출금 금액
                .gubun(TransactionEnum.WITHDRAW)
                .sender(accountWithdrawReqDto.getNumber()+"")
                .receiver("ATM")
                .build();

        Transaction transactionPS = transactionRepository.save(transaction);

        return new AccountWithdrawRespDto(withdrawAccountPS, transactionPS);

    }




    @Transactional
    public AccountTransferRespDto 계좌이체(AccountTransferReqDto transferReqDto, Long userId) {

        // 출금 계좌와 입금계좌가 동일하면 안됨
        if(transferReqDto.getWithdrawNumber().longValue() == transferReqDto.getDepositNumber().longValue()) {
            throw new CustomApiException("같은 계좌로 이체를 할수 없습니다.");
        }

        // 0원 체크
        if(transferReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        // 출금 계좌 체크
        Account withdrawAccountPS = accountRepository.findByNumber(transferReqDto.getWithdrawNumber())
                .orElseThrow( () -> new CustomApiException("출금 계좌가 없습니다.") );

        // 입금 계좌 체크
        Account depositAccountPS = accountRepository.findByNumber(transferReqDto.getDepositNumber())
                .orElseThrow( () -> new CustomApiException("입금 계좌가 없습니다.") );

        // 출금 소유자 확인 ( 로그인한 사람과 동일한지 )
        withdrawAccountPS.checkOwner(userId);
        withdrawAccountPS.checkSamePassword(transferReqDto.getWithdrawPassword());
        withdrawAccountPS.checkBalance(transferReqDto.getAmount());

        // 이체하기
        withdrawAccountPS.withdraw(transferReqDto.getAmount());
        depositAccountPS.deposit(transferReqDto.getAmount());

        // 거래내역 남기기 ( 내 계좌에서 ATM 으로 출금 )
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccountPS) //출금 계좌
                .depositAccount(depositAccountPS)               //입금 계좌
                .withdrawAccountBalance(withdrawAccountPS.getBalance())   //출금계좌 잔액
                .depositAccountBalance(depositAccountPS.getBalance())     //입금계좌 잔액
                .amount(transferReqDto.getAmount())                //입출금 금액
                .gubun(TransactionEnum.TRANSFER)
                .sender(withdrawAccountPS.getNumber()+"")
                .receiver(depositAccountPS.getNumber()+"")
                .build();

        Transaction transactionPS = transactionRepository.save(transaction);

        return new AccountTransferRespDto(withdrawAccountPS, transactionPS);

    }


}
