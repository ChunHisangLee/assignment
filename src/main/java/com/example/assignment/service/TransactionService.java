package com.example.assignment.service;

import com.example.assignment.dto.TransactionDTO;
import com.example.assignment.dto.CreateTransactionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionDTO createTransaction(CreateTransactionRequest request, String transactionType);
    Page<TransactionDTO> getUserTransactionHistory(Long userId, Pageable pageable);
}
