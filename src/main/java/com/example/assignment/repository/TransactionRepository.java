package com.example.assignment.repository;

import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUsers(Users users);
}

