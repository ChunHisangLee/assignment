package com.example.assignment.service;

import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    Transaction createTransaction(Long userId, double btcAmount, TransactionType transactionType);

    Page<Transaction> getUserTransactionHistory(Long userId, Pageable pageable);
}
