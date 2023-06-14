# Junit Bank App

### JPA LocalDateTime 자동으로 생성하는 법
- @EnableJpaAuditing (Main 클래스에 선언)
- @EntityListeners(AuditingEntityListener.class) (Entity 클래스에 선언)
```java

    @CreatedDate //INSERT
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate //UPDATE
    @Column(nullable = false)
    private LocalDateTime updatedAt;

```



### 테이블 정보
USERS 1  ----- N ACCOUNT 1 -------- N TRANSACTION
회원               계좌                   거래내역

TRANSACTION TABLE :
id, amount , gubun , withdraw_account_id , deposit_account_id , withdraw_account_balance , deposit_account_balance
금액

계좌 생성시 기본금액은 1000원이라고 픽스한다.

gubun : 출금 = WITHDRAW , 입금 = DEPOSIT , 이체 = TRANSFER


ACCOUNT TABLE :
id , user_id , number , balance





