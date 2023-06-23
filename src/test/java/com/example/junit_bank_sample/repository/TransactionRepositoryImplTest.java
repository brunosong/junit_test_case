package com.example.junit_bank_sample.repository;

import com.example.junit_bank_sample.config.dummy.DummyObject;
import com.example.junit_bank_sample.domain.account.Account;
import com.example.junit_bank_sample.domain.transaction.Transaction;
import com.example.junit_bank_sample.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@DataJpaTest  // 데이터베이스 관련된 Bean 들이 다 올라온다.
public class TransactionRepositoryImplTest extends DummyObject {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager em;


    @BeforeEach
    public void setup(){
        autoincrementReset();
        dataSetting();
        em.clear();
    }


    @Test
    void findTransactionList_all_test() {

        //given
        Long accountId = 1L;

        List<Transaction> transactionListPS = transactionRepository.findTransactionList(accountId, "ALL", 0);

        //when
        transactionListPS.forEach( t -> {
            System.out.println("테스트 : id : " + t.getId());
            System.out.println("테스트 : amount : " + t.getAmount());
            System.out.println("테스트 : sender : " + t.getSender());
            System.out.println("테스트 : reciver : " + t.getReceiver());
            System.out.println("테스트 : withdrawAccount잔액 : " + t.getWithdrawAccountBalance());
            System.out.println("테스트 : depositAccount잔액 : " + t.getDepositAccountBalance());
            System.out.println("테스트 : 잔액 : " + t.getWithdrawAccount().getBalance());
            System.out.println("테스트 : 풀네임 : " + t.getWithdrawAccount().getUser().getFullname());
            System.out.println("테스트 : ======================================");
        });

        //then
        //assertThat()
    }




    @Test
    void datajpa_test1() {

        List<Transaction> all = transactionRepository.findAll();
        all.forEach( t -> {
            System.out.println(t.getId());
            System.out.println(t.getGubun());
        });
    }


    @Test
    void datajpa_test2() {

        List<Transaction> all = transactionRepository.findAll();
        all.forEach( t -> {
            System.out.println(t.getId());
            System.out.println(t.getGubun());
        });
    }


    private void dataSetting() {
        User ssar = userRepository.save(newUser("ssar", "쌀"));
        User cos = userRepository.save(newUser("cos", "코스,"));
        User love = userRepository.save(newUser("love", "러브"));
        User admin = userRepository.save(newUser("admin", "관리자"));

        Account ssarAccount1 = accountRepository.save(newAccount(1111L, ssar));
        Account cosAccount = accountRepository.save(newAccount(2222L, cos));
        Account loveAccount = accountRepository.save(newAccount(3333L, love));
        Account ssarAccount2 = accountRepository.save(newAccount(4444L, ssar));

        Transaction withdrawTransaction1 = transactionRepository
                .save(newWithdrawTransaction(ssarAccount1, accountRepository));

        Transaction depositTransaction1 = transactionRepository
                .save(newDepositTransaction(cosAccount, accountRepository));

        Transaction transferTransaction1 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, cosAccount, accountRepository));

        Transaction transferTransaction2 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, loveAccount, accountRepository));

        Transaction transferTransaction3 = transactionRepository
                .save(newTransferTransaction(cosAccount, ssarAccount1, accountRepository));
    }


    public void autoincrementReset() {
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE account_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE transaction_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }
}
