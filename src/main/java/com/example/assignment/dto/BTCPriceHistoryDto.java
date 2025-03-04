package com.example.assignment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BTCPriceHistoryDto {
  private Long id;
  private BigDecimal price;
  private LocalDateTime timestamp;
}
