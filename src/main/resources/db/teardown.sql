SET REFERENTIAL_INTEGRITY FALSE;   -- 모든제약조건을 삭제한다.
truncate table transaction_tb;
truncate table account_tb;
truncate table user_tb;
SET REFERENTIAL_INTEGRITY TRUE;