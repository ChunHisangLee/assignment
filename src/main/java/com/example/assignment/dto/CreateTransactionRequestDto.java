package com.example.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.*;

@Schema(name = "CreateTransactionRequest", description = "Request body for creating a transaction")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequestDto {
  @Schema(description = "User ID", example = "1")
  private Long userId;

  @Schema(description = "BTC amount", example = "1000.00")
  private BigDecimal btcAmount;
}
