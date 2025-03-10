package com.example.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.*;

@Schema(name = "Wallet", description = "Wallet details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {
  @Schema(description = "ID of the wallet", example = "1")
  private Long id;

  @Schema(description = "USD balance of the wallet", example = "1000.00")
  private BigDecimal usdBalance;

  @Schema(description = "BTC balance of the wallet", example = "1000.00")
  private BigDecimal btcBalance;
}
