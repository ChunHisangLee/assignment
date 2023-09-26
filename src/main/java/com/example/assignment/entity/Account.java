package com.example.assignment.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Account extends BaseEntity implements Serializable {
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

    private BigDecimal scaleAmount(BigDecimal amount) {
        return amount.setScale(5, RoundingMode.DOWN);
    }

    public void setCarryingAmount(BigDecimal carryingAmount) {
        this.carryingAmount = scaleAmount(carryingAmount);
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = scaleAmount(balanceAmount);
    }

    public void setNetValue(BigDecimal netValue) {
        this.netValue = scaleAmount(netValue);
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = scaleAmount(baseAmount);
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = scaleAmount(minAmount);
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = scaleAmount(maxAmount);
    }

    public void setDayMaxAmount(BigDecimal dayMaxAmount) {
        this.dayMaxAmount = scaleAmount(dayMaxAmount);
    }
}

