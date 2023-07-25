package com.example.assignment.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Data
public class Account {
    private Long id;
    private Long coinId;
    private String accountStatus;
    private BigDecimal carryingAmount;
    private BigDecimal balanceAmount;
    private BigDecimal freezeAmount;
    private BigDecimal netValue;

    public void setId(Long id) {
        this.id = id;
    }

    public void setCoinId(Long coinId) {
        this.coinId = coinId;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

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

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private Date updateTime;
    private Date createTime;
}

