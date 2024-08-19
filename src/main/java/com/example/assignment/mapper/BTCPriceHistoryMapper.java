package com.example.assignment.mapper;

import com.example.assignment.dto.BTCPriceHistoryDTO;
import com.example.assignment.entity.BTCPriceHistory;
import org.springframework.stereotype.Component;

@Component
public class BTCPriceHistoryMapper {

    public BTCPriceHistoryDTO convertToDto(BTCPriceHistory btcPriceHistory) {
        return new BTCPriceHistoryDTO(
                btcPriceHistory.getId(),
                btcPriceHistory.getPrice(),
                btcPriceHistory.getTimestamp()
        );
    }
}
