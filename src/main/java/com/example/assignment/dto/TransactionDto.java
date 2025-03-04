package com.example.assignment.dto;

import com.example.assignment.entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
  private Long id;
  private Long userId;
  private BigDecimal btcAmount;
  private LocalDateTime transactionTime;
  private TransactionType transactionType;
  private BTCPriceHistoryDto btcPriceHistory; // Nested DTO for BTCPriceHistory
  private UsersDto users; // Nested DTO for Users

  // Fields to track balances before and after the transaction
  private BigDecimal usdBalanceBefore;
  private BigDecimal btcBalanceBefore;
  private BigDecimal usdBalanceAfter;
  private BigDecimal btcBalanceAfter;
}
