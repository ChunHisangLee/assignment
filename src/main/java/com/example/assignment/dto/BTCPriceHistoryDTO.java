package com.example.assignment.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BTCPriceHistoryDTO {
    private Long id;
    private double price;
    private LocalDateTime timestamp;

    public BTCPriceHistoryDTO(Long id, double price, LocalDateTime timestamp) {
        this.id = id;
        this.price = price;
        this.timestamp = timestamp;
    }
}
