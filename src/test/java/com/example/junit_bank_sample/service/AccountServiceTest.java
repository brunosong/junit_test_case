package com.example.junit_bank_sample.service;

import com.example.junit_bank_sample.config.dummy.DummyObject;
import com.example.junit_bank_sample.domain.account.Account;
import com.example.junit_bank_sample.domain.transaction.Transaction;
import com.example.junit_bank_sample.domain.user.User;
import com.example.junit_bank_sample.dto.account.AccountReqDto;
import com.example.junit_bank_sample.dto.account.AccountReqDto.AccountDepositReqDto;
import com.example.junit_bank_sample.dto.account.AccountReqDto.AccountSaveRespDto;
import com.example.junit_bank_sample.dto.account.AccountReqDto.AccountTransferReqDto;
import com.example.junit_bank_sample.dto.account.AccountRespDto.AccountDepositRespDto;
import com.example.junit_bank_sample.dto.account.AccountRespDto.AccountSaveReqDto;
import com.example.junit_bank_sample.handler.ex.CustomApiException;
import com.example.junit_bank_sample.repository.AccountRepository;
import com.example.junit_bank_sample.repository.TransactionRepository;
import com.example.junit_bank_sample.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AccountServiceTest extends DummyObject {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Spy
    private ObjectMapper objectMapper;

    @Test
    void 계좌등록_test() throws Exception {

        //given
        Long userId = 1L;
        AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
        accountSaveReqDto.setNumber(1111L);
        accountSaveReqDto.setPassword(1234L);

        //stub 1
        User ssar = newMockUser(userId,"ssar","쌀");
        when(userRepository.findById(any())).thenReturn(Optional.of(ssar));

        //stub 2  여기서 값이 나오면 에러를 호출하기때문에 empty 를 리턴한다.
        when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());

        //stub 3
        Account ssarAccount = newMockAccount(1L,1111L, 1000L, ssar);
        when(accountRepository.save(any())).thenReturn(ssarAccount);


        // when
        AccountSaveRespDto accountSaveRespDto = accountService.계좌등록(accountSaveReqDto, userId);
        String s = objectMapper.writeValueAsString(accountSaveRespDto);
        System.out.println(s);

        //then
        assertThat(accountSaveRespDto.getNumber()).isEqualTo(1111L);
    }


    @Test
    void 계좌삭제_test() {

        //given
        Long number = 1111L;
        Long userId = 2L;

        //stub 1
        User ssar = newMockUser( 1L ,"ssar","쌀" );
        Account ssarAccount = newMockAccount(1L,1111L,1000L, ssar);
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(ssarAccount));

        //when
        Assertions.assertThrows( CustomApiException.class , () -> {
            accountService.계좌삭제(number, userId );
        });

    }

    
    // Account -> balance 
    // Transaction -> balance 잘 기록됬는지
    @Test
    void 계좌입금_test() {

        //given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setTel("01074637177");

        //stub 1
        User ssar = newMockUser( 1L ,"ssar","쌀" );
        Account ssarAccount1 = newMockAccount(1L,1111L,1000L, ssar);
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(ssarAccount1));

        //stub 2  : 스텁이 진행될때 마다 연관된 객체는 새로 만들어 주는게 안꼬인다. (타이밍 때문에 꼬이는 문제가 발생함)
        Account ssarAccount2 = newMockAccount(1L,1111L,1000L, ssar);
        Transaction transaction = newMockDepositTransaction(1L, ssarAccount2);
        when(transactionRepository.save(any())).thenReturn(transaction);


        //when
        AccountDepositRespDto depositRespDto = accountService.계좌입금(accountDepositReqDto);
        System.out.println("테스트 잔액 :" + depositRespDto.getTransaction().getDepositAccountBalance());
        System.out.println("계좌쪽 잔액 :" + ssarAccount1.getBalance());

        //then
        assertThat(depositRespDto.getTransaction().getDepositAccountBalance()).isEqualTo(1100L);
        assertThat(ssarAccount1.getBalance()).isEqualTo(1100L);


    }


    @Test
    void 계좌입금2_test() {
        //서비스를 테스트 하는 이유가 뭘까???? 위꺼가 정석이라고 한다.
        //계좌입금을 할때 기능들이 잘 동작하는지 ... 디비를 사용하지 않기때문에 맘대로 테스트 가능
        //DTO 가 잘 만들어 지는지 등등...
        //진짜 서비스를 테스트 하고 싶으면 내가 지금 무엇을 여기서 테스트해야할지 명확히 구분(책임분리)
        //DTO를 만드는 책임 -> 서비스에 있지만 서비스에서 DTO검증 안할래 ! .. 컨트롤러에서 테스트 해볼꺼니깐
        //DB 관련된 것도 -> 서비스 것이 아니야 볼필요없어
        //DB 관련된 것을 조회했을때 그 값을 통해서 어떤 비지니스 로직이 흘러가는 것이 있으면 -> stub 으로 정의해서 테스트 해보면 된다.



        //then
        //assertThat()
    }

    @Test
    void 계좌입금3_test() {

        //given
        Account account = newMockAccount(1L,1111L,1000L, null);
        Long amount = 0L;

        //when
        if(amount <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        account.deposit(100L);

        //then
        assertThat(account.getBalance()).isEqualTo(1100L);
    }


    //계좌 출금 테스트
    @Test
    public void 계좌출금_test() {

        //given
        Long amount = 100L;
        Long password = 1234L;
        Long userId = 1L;

        //given
        User user = newMockUser(1L,"ssar","쌀");
        Account ssarAccount = newMockAccount(1L,1111L,1000L,user);

        // 0원 체크
        if(amount <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        // when
        // 같은 유저인지 체크
        ssarAccount.checkOwner(userId);

        // 비밀번호 확인
        ssarAccount.checkSamePassword(password);  // 비밀번호가 틀리면 터진다.

        // 출금 잔액 확인
        //ssarAccount.checkBalance(500L);  //1000원이 넘어가면 터진다.

        // 출금
        ssarAccount.withdraw(amount);  // checkBalance를 주석처리를 하여도 withdraw 안에서 한번더 체크하기 때문에 문제가 없다.

        // then
        assertThat(ssarAccount.getBalance()).isEqualTo(900L);

    }


    @Test
    @DisplayName("계좌 이체 테스트")
    public void 계좌이체_test() {
        //given
        Long userId = 1L;

        User ssar = newMockUser(1L,"ssar","쌀");
        User cos = newMockUser(2L,"cos","코스");

        AccountTransferReqDto reqDto = new AccountTransferReqDto();
        reqDto.setWithdrawNumber(1111L);
        reqDto.setDepositNumber(2222L);
        reqDto.setWithdrawPassword(1234L);
        reqDto.setAmount(100L);
        reqDto.setGubun("TRANSFER");

        Account withdrawAccount = newMockAccount(1L,1111L, 1000L, ssar);
        Account depositAccount = newMockAccount(2L,2222L, 1000L, cos);

        //when

        // 출금 계좌와 입금계좌가 동일하면 안됨
        if(reqDto.getWithdrawNumber().longValue() == reqDto.getDepositNumber().longValue()) {
            throw new CustomApiException("같은 계좌로 이체를 할수 없습니다.");
        }

        // 0원 체크
        if(reqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        // when

        withdrawAccount.checkOwner(userId);

        withdrawAccount.checkSamePassword(reqDto.getWithdrawPassword());  // 비밀번호가 틀리면 터진다.

        withdrawAccount.checkBalance(reqDto.getAmount());

        withdrawAccount.withdraw(reqDto.getAmount()); //출금
        depositAccount.deposit(reqDto.getAmount());  //입금

        assertThat(withdrawAccount.getBalance()).isEqualTo(900L);
        assertThat(depositAccount.getBalance()).isEqualTo(1100L);

    }







}

