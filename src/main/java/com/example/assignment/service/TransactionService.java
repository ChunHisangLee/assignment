package com.example.assignment.service;

import com.example.assignment.dto.TransactionDTO;
import com.example.assignment.dto.CreateTransactionRequest;
import com.example.assignment.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionDTO createTransaction(CreateTransactionRequest request, TransactionType transactionType);
    Page<TransactionDTO> getUserTransactionHistory(Long userId, Pageable pageable);
}
