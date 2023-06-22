package com.example.junit_bank_sample.web;

import com.example.junit_bank_sample.config.dummy.DummyObject;
import com.example.junit_bank_sample.domain.account.Account;
import com.example.junit_bank_sample.domain.user.User;
import com.example.junit_bank_sample.dto.account.AccountReqDto;
import com.example.junit_bank_sample.dto.account.AccountReqDto.AccountDepositReqDto;
import com.example.junit_bank_sample.dto.account.AccountReqDto.AccountTransferReqDto;
import com.example.junit_bank_sample.dto.account.AccountReqDto.AccountWithdrawReqDto;
import com.example.junit_bank_sample.dto.account.AccountRespDto.AccountSaveReqDto;
import com.example.junit_bank_sample.handler.ex.CustomApiException;
import com.example.junit_bank_sample.repository.AccountRepository;
import com.example.junit_bank_sample.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@Transactional //이걸로 처리하면 auto increment 가 초기화가 되질 않는다.
@Sql("classpath:db/teardown.sql") // 그래서 이넘을 사용해서 테스트 케이스가 돌때마다 직접 테이블을 날려버린다.
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AccountControllerTest extends DummyObject {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    public void setup() throws Exception {
        User ssar = userRepository.save(newUser("ssar", "쌀"));
        User cos = userRepository.save(newUser("cos", "코스"));

        Account ssarAccount = accountRepository.save(newAccount(1111L,ssar));
        Account cosAccount = accountRepository.save(newAccount(1112L,cos));

        em.clear();
    }

    // JWT 토큰을 만들어서 할필요가 없다. 시큐리티에 세션만 있으면 통과할수 있다. 필터는 알아서 통과가 된다.
    // setupBefore=TEST_METHOD 메서드 실행 전에 실행된다. setup 메소드 전에 수행이 된다.
    // setup 이 실행되기 전에 먼저 실행이 되서 바꿔줘야 한다.
    // TEST_EXECUTION 는 saveAccount_test 메소드 실행 바로 전에 실행된다. setup 보다 늦게 실행된다.
    @WithUserDetails(value = "ssar" , setupBefore = TestExecutionEvent.TEST_EXECUTION)  //디비에서 유저네임 ssar로 검색을 하는것이다.
    @Test
    void saveAccount_test() throws Exception {

        //given
        AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
        accountSaveReqDto.setNumber(9999L);
        accountSaveReqDto.setPassword(1234L);
        String requestBody = objectMapper.writeValueAsString(accountSaveReqDto);
        System.out.println(requestBody);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/s/account").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        //then
        resultActions.andExpect(status().isCreated());
    }

    /*
    * 테스트시에는 insert 한것들이 전부 pc 에 올라감(영속화)
    * 영속회 된것을 초기화 해주는 것이 개발 모드와 동일환 환경응로 테스트 할수 있게 해준다.
    * 최초 select 는 쿼리가 발생하지만 - pc에 있다면 1차 캐시를 함
    * Lazy 로딩은 쿼리가 발생안함 - pc에 있다면
    * Lazy 로딩할 때 PC 에 없다면 쿼리를 발생한다.
    * */
    @WithUserDetails(value = "ssar" , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void deleteAccount_test() throws Exception {

        Long number = 1111L;

        ResultActions rs = mockMvc.perform(delete("/api/s/account/" + number));
        String responseBody = rs.andReturn().getResponse().getContentAsString();
        System.out.println("테스트:" + responseBody);

        // Junit 테스트에서는 delete 쿼리는 발생을 안한다.
        // pc 에서만 저장을 해놧다가 한번에 처리를 해주는 상황으로 보인다.
        // 그래서 다시 조회 할라고 하니깐 pc 에서 db 를 호출하는것 같음
        Assertions.assertThrows(CustomApiException.class, () ->
                accountRepository.findByNumber(number).orElseThrow(
                    () -> new CustomApiException("계좌를 찾을수 없습니다.")
                )
        );

    }


    @Test
    void depositAccount_test() throws Exception {

        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setTel("01074637177");

        ResultActions rs = mockMvc.perform(post("/api/account/deposit")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(accountDepositReqDto)));

        String responseBody = rs.andReturn().getResponse().getContentAsString();
        System.out.println("테스트:" + responseBody);

        rs.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "ssar" , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void withdrawAccount_test() throws Exception {

        AccountWithdrawReqDto reqDto = new AccountWithdrawReqDto();
        reqDto.setNumber(1111L);
        reqDto.setPassword(1234L);
        reqDto.setAmount(100L);
        reqDto.setGubun("WITHDRAW");

        ResultActions rs = mockMvc.perform(post("/api/s/account/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqDto)));

        String responseBody = rs.andReturn().getResponse().getContentAsString();
        System.out.println("테스트:" + responseBody);

        rs.andExpect(status().isCreated());
    }


    @WithUserDetails(value = "ssar" , setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void transferAccount_test() throws Exception {

        AccountTransferReqDto reqDto = new AccountTransferReqDto();
        reqDto.setWithdrawNumber(1111L);
        reqDto.setDepositNumber(1112L);
        reqDto.setWithdrawPassword(1234L);
        reqDto.setAmount(100L);
        reqDto.setGubun("TRANSFER");

        ResultActions rs = mockMvc.perform(post("/api/s/account/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqDto)));

        String responseBody = rs.andReturn().getResponse().getContentAsString();
        System.out.println("테스트:" + responseBody);

        rs.andExpect(status().isCreated());
    }


}
