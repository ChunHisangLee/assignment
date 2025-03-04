package com.example.assignment.service;

import com.example.assignment.dto.CreateTransactionRequestDto;
import com.example.assignment.dto.TransactionDto;
import com.example.assignment.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
  TransactionDto createTransaction(
      CreateTransactionRequestDto request, TransactionType transactionType);

  Page<TransactionDto> getUserTransactionHistory(Long userId, Pageable pageable);
}
