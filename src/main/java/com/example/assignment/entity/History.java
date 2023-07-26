package com.example.assignment.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class History {
    Integer coinId;
    String transType;
    BigDecimal transPrice;
    BigDecimal transQuantity;
    Date transTime;

}
