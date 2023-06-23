package com.example.junit_bank_sample.domain.account;

import com.example.junit_bank_sample.domain.user.User;
import com.example.junit_bank_sample.handler.ex.CustomApiException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account_tb")
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true , nullable = false, length = 4)
    private Long number;    //계좌번호

    @Column(nullable = false, length = 4)
    private Long password; //계좌비번

    @Column(nullable = false)
    private Long balance;   // 잔액 ( 기본값은 1000원 )

    @ManyToOne(fetch = FetchType.LAZY)  //account.getUser().아무필드호출() 이때 디비에서 호출한다.
    private User user;

    @CreatedDate //INSERT
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate //UPDATE
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Account(Long id, Long number, Long password, Long balance, User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.balance = balance;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /* 이런식으로 엔티티안에서 자기가 할수 있는 일을 해준다.
    *  다른 객체에서 이 값을 검증하는게 집중화에 맞는것인가?
    *  단일 책임 원칙 (Single Responsiblity Principle)에 맞게 작성하려면 엔티티 안에 넣어서
    *  자기 기능을 해주는게 SRP를 지키는것이라고 생각한다.
    *  확실치 않으니깐 더 확인을 해봐야 한다.
    *
    * */
    public void checkOwner(Long userId) {
        // user.getUsername() //이렇게 하면 lazy 로딩이 되어 셀렉트를 해온다.
        // persitace context 에 머물러 있어서 조회를 해도 디비로 날리지 않는다.
        // 그렇기 때문에 pc 를 깨끗히 비워줘야 한다
        if(user.getId().longValue() != userId.longValue()){  // id 를 조회할때는 lazy 되지 않고 ( 셀렉트를 하지 않고 ) 가져온다.
            throw new CustomApiException("계좌 소유자가 아닙니다.");
        }
    }

    public void checkSamePassword(Long password) {
        if(this.password.longValue() != password.longValue()){
            throw new CustomApiException("계좌 비밀번호가 다릅니다.");
        }
    }


    public void checkBalance(Long amount) {
        if(this.balance < amount){
            throw new CustomApiException("잔액이 부족합니다.");
        }
    }

    /* 출금 */
    public void withdraw(Long amount) {
        checkBalance(amount);
        balance = balance - amount;
    }

    /* 입금 */
    public void deposit(Long amount) {
        balance = balance + amount;
    }

}
