package com.example.junit_bank_sample.repository;

import com.example.junit_bank_sample.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

}
