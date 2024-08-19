package com.example.assignment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO {
    private Long id;
    private double btcAmount;
    private LocalDateTime transactionTime;
    private String transactionType;
    private BTCPriceHistoryDTO btcPriceHistory; // Nested DTO for BTCPriceHistory
    private UsersDTO users; // Nested DTO for Users

    public TransactionDTO(Long id, double btcAmount, LocalDateTime transactionTime, String transactionType, BTCPriceHistoryDTO btcPriceHistory, UsersDTO users) {
        this.id = id;
        this.btcAmount = btcAmount;
        this.transactionTime = transactionTime;
        this.transactionType = transactionType;
        this.btcPriceHistory = btcPriceHistory;
        this.users = users;
    }
}
