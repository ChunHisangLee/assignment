package com.example.assignment.entity;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class Coin {
    private Integer coinId;
    private String name;
    @Getter
    private BigDecimal baseAmount;
    @Getter
    private BigDecimal minAmount;
    @Getter
    private BigDecimal maxAmount;
    @Getter
    private BigDecimal dayMaxAmount;

    public Coin() {
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount.setScale(5, RoundingMode.DOWN);
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount.setScale(5, RoundingMode.DOWN);
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount.setScale(5, RoundingMode.DOWN);
    }

    public void setDayMaxAmount(BigDecimal dayMaxAmount) {
        this.dayMaxAmount = dayMaxAmount.setScale(5, RoundingMode.DOWN);
    }
}