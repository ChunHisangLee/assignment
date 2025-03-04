package com.example.assignment.dto;

import java.math.BigDecimal;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequestDto {
  private Long userId;
  private BigDecimal btcAmount;
}
