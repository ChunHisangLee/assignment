package com.example.assignment.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Account extends BaseEntity {
    private String userId;
    private Integer coinId;
    private String accountStatus;
    @Getter
    private BigDecimal carryingAmount;
    @Getter
    private BigDecimal balanceAmount;
    @Getter
    private BigDecimal netValue;
    @Getter
    private BigDecimal baseAmount;
    @Getter
    private BigDecimal minAmount;
    @Getter
    private BigDecimal maxAmount;
    @Getter
    private BigDecimal dayMaxAmount;


    public void setCarryingAmount(BigDecimal carryingAmount) {
        this.carryingAmount = carryingAmount.setScale(5, RoundingMode.DOWN);
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount.setScale(5, RoundingMode.DOWN);
    }

    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue.setScale(5, RoundingMode.DOWN);
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

