package com.example.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Schema(name = "BTCPriceHistory", description = "BTC Price History")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BTCPriceHistoryDto {
  @Schema(description = "ID of the BTC price history", example = "1")
  private Long id;

  @Schema(description = "Price of the BTC", example = "1000.00")
  private BigDecimal price;

  @Schema(description = "Timestamp of the BTC price history", example = "2023-01-01T00:00:00")
  private LocalDateTime timestamp;
}
