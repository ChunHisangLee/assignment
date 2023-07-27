package com.example.assignment.service;

public interface IPriceService {
    /**
     * 取得價格
     * @param time
     */
    Integer getPrice(Long time);
    /**
     * 設定價格
     */
    void setPrice();
}
