package com.example.junit_bank_sample.repository;

import com.example.junit_bank_sample.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    // checkPoint : 리팩토링 해야함!!!
    // 다대일에서 user를 꺼낼일이 있으면 계속 셀렉트를 하게 된다.
    // 그래서 패치조인으로 값을 가져오게 처리를 하면 된다.
    // 이 프로젝트에서는 그럴일이 없지만 하는 방법 적음
    //@Query("SELECT ac FROM Account ac JOIN FETCH ac.user u where ac.number = :number")
    Optional<Account> findByNumber(@Param("number") Long number);

    // select * from account a where a.user_id = :id  //쿼리 메서드 임
    List<Account> findByUser_id(Long id);

}
