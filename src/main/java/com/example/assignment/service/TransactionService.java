package com.example.assignment.service;

import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.TransactionType;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Long userId, double btcAmount, TransactionType transactionType);
    List<Transaction> getUserTransactionHistory(Long userId);
}