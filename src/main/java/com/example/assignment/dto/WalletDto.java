package com.example.assignment.dto;

import java.math.BigDecimal;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {
  private Long id;
  private BigDecimal usdBalance;
  private BigDecimal btcBalance;
}
