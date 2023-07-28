package com.example.assignment.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Account extends BaseEntity {
    private Integer userId;
    private Integer coinId;
    private String accountStatus;
    @Getter
    private BigDecimal carryingAmount;
    @Getter
    private BigDecimal balanceAmount;
    @Getter
    private BigDecimal freezeAmount;
    @Getter
    private BigDecimal netValue;


    public void setCarryingAmount(BigDecimal carryingAmount) {
        this.carryingAmount = carryingAmount.setScale(5, RoundingMode.DOWN);
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount.setScale(5, RoundingMode.DOWN);
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount.setScale(5, RoundingMode.DOWN);
    }

    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue.setScale(5, RoundingMode.DOWN);
    }
}

