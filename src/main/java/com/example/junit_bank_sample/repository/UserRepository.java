package com.example.junit_bank_sample.repository;

import com.example.junit_bank_sample.domain.user.User;
import com.example.junit_bank_sample.service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);
}
