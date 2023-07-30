package com.example.assignment.entity;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Data
public class History {
    String historyId;
    String userId;
    Integer coinId;
    String transType;
    @Getter
    BigDecimal transPrice;
    @Getter
    BigDecimal transQuantity;
    @Getter
    BigDecimal beforeBalance;
    @Getter
    BigDecimal afterBalance;
    @Getter
    BigDecimal beforeBalanceUSD;
    @Getter
    BigDecimal afterBalanceUSD;
    Date transTime;

    public void setTransPrice(BigDecimal transPrice) {
        this.transPrice = transPrice.setScale(5, RoundingMode.DOWN);
    }

    public void setTransQuantity(BigDecimal transQuantity) {
        this.transQuantity = transQuantity.setScale(5, RoundingMode.DOWN);
    }

    public void setBeforeBalance(BigDecimal beforeBalance) {
        this.beforeBalance = beforeBalance.setScale(5, RoundingMode.DOWN);
    }

    public void setAfterBalance(BigDecimal afterBalance) {
        this.afterBalance = afterBalance.setScale(5, RoundingMode.DOWN);
    }

    public void setBeforeBalanceUSD(BigDecimal beforeBalanceUSD) {
        this.beforeBalanceUSD = beforeBalanceUSD.setScale(5, RoundingMode.DOWN);
    }

    public void setAfterBalanceUSD(BigDecimal afterBalanceUSD) {
        this.afterBalanceUSD = afterBalanceUSD.setScale(5, RoundingMode.DOWN);
    }
}
