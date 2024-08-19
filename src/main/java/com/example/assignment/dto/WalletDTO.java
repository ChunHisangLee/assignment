package com.example.assignment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WalletDTO {
    private Long id;
    private double usdBalance;
    private double btcBalance;

    public WalletDTO(Long id, double usdBalance, double btcBalance) {
        this.id = id;
        this.usdBalance = usdBalance;
        this.btcBalance = btcBalance;
    }
}
