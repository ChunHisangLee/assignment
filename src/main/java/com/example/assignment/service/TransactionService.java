package com.example.assignment.service;

import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.TransactionType;

public interface TransactionService {
    Transaction createTransaction(Long userId, double btcAmount, TransactionType transactionType);
}
