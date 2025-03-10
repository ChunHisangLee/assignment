package com.example.assignment.dto;

import com.example.assignment.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Schema(name = "Transaction", description = "Transaction details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
  @Schema(description = "ID of the transaction", example = "1")
  private Long id;

  @Schema(description = "ID of the user", example = "1")
  private Long userId;

  @Schema(description = "BTC amount", example = "1000.00")
  private BigDecimal btcAmount;

  @Schema(description = "Transaction time", example = "2023-01-01T00:00:00")
  private LocalDateTime transactionTime;

  @Schema(description = "Transaction type", example = "BUY")
  private TransactionType transactionType;

  @Schema(description = "BTC Price History")
  private BTCPriceHistoryDto btcPriceHistory; // Nested DTO for BTCPriceHistory

  private UsersDto users; // Nested DTO for Users

  // Fields to track balances before and after the transaction
  private BigDecimal usdBalanceBefore;
  private BigDecimal btcBalanceBefore;
  private BigDecimal usdBalanceAfter;
  private BigDecimal btcBalanceAfter;
}
