package com.example.assignment.dto;

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
    private String transactionType;
    private BTCPriceHistoryDTO btcPriceHistory; // Nested DTO for BTCPriceHistory
    private UsersDTO users; // Nested DTO for Users
}
