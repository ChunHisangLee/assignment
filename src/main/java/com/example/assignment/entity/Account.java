package com.example.assignment.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class Account extends BaseEntity {
    private Integer userId;
    private Integer coinId;
    private String accountStatus;
    private BigDecimal carryingAmount;
    private BigDecimal balanceAmount;
    private BigDecimal freezeAmount;
    private BigDecimal netValue;


    public void setCarryingAmount(BigDecimal carryingAmount) {
        this.carryingAmount = carryingAmount.setScale(2, RoundingMode.DOWN);
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount.setScale(2, RoundingMode.DOWN);
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount.setScale(2, RoundingMode.DOWN);
    }

    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue.setScale(2, RoundingMode.DOWN);
    }
}

