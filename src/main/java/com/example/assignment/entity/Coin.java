package com.example.assignment.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class Coin {
    private Integer coinId;
    private String name;
    private BigDecimal baseAmount;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private BigDecimal dayMaxAmount;

    public Coin() {
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount.setScale(2, RoundingMode.DOWN);
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount.setScale(2, RoundingMode.DOWN);
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount.setScale(2, RoundingMode.DOWN);
    }

    public void setDayMaxAmount(BigDecimal dayMaxAmount) {
        this.dayMaxAmount = dayMaxAmount.setScale(2, RoundingMode.DOWN);
    }
}
