package com.example.assignment.service;

import java.math.BigDecimal;

public interface PriceService {
  BigDecimal getPrice();

  void setPrice(BigDecimal price);
}
