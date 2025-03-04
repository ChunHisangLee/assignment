package com.example.assignment.mapper;

import com.example.assignment.dto.BTCPriceHistoryDto;
import com.example.assignment.entity.BTCPriceHistory;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class BTCPriceHistoryMapper {

  public BTCPriceHistoryDto toDto(BTCPriceHistory btcPriceHistory) {
    return BTCPriceHistoryDto.builder()
        .id(btcPriceHistory.getId())
        .price(btcPriceHistory.getPrice())
        .timestamp(btcPriceHistory.getTimestamp())
        .build();
  }

  public BTCPriceHistory toEntity(BTCPriceHistoryDto btcPriceHistoryDTO) {
    return BTCPriceHistory.builder()
        .price(btcPriceHistoryDTO.getPrice())
        .timestamp(
            Optional.ofNullable(btcPriceHistoryDTO.getTimestamp()).orElse(LocalDateTime.now()))
        .build();
  }
}
