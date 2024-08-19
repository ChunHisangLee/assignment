package com.example.assignment.dto;

import com.example.assignment.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private double btcAmount;
    private LocalDateTime transactionTime;
    private TransactionType transactionType;
    private BTCPriceHistoryDTO btcPriceHistory; // Nested DTO for BTCPriceHistory
    private UsersDTO users; // Nested DTO for Users

    // Fields to track balances before and after the transaction
    private double usdBalanceBefore;
    private double btcBalanceBefore;
    private double usdBalanceAfter;
    private double btcBalanceAfter;
}
