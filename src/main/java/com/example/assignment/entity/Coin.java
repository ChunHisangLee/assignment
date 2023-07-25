package com.example.assignment.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class Coin {
    private Long id;
    private String name;
    private BigDecimal baseAmount;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private BigDecimal dayMaxAmount;

    public Coin() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount.setScale(8, RoundingMode.HALF_UP);
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount.setScale(8, RoundingMode.HALF_UP);
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount.setScale(8, RoundingMode.HALF_UP);
    }

    public void setDayMaxAmount(BigDecimal dayMaxAmount) {
        this.dayMaxAmount = dayMaxAmount.setScale(8, RoundingMode.HALF_UP);
    }
}
