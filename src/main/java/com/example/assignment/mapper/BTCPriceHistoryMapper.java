package com.example.assignment.mapper;

import com.example.assignment.dto.BTCPriceHistoryDTO;
import com.example.assignment.entity.BTCPriceHistory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class BTCPriceHistoryMapper {

    public BTCPriceHistoryDTO toDto(BTCPriceHistory btcPriceHistory) {
        return BTCPriceHistoryDTO.builder()
                .id(btcPriceHistory.getId())
                .price(btcPriceHistory.getPrice())
                .timestamp(btcPriceHistory.getTimestamp())
                .build();
    }

    public BTCPriceHistory toEntity(BTCPriceHistoryDTO btcPriceHistoryDTO) {
        return BTCPriceHistory.builder()
                .price(btcPriceHistoryDTO.getPrice())
                .timestamp(Optional.ofNullable(btcPriceHistoryDTO.getTimestamp()).orElse(LocalDateTime.now()))
                .build();
    }
}
