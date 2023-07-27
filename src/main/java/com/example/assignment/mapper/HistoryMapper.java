package com.example.assignment.mapper;

import com.example.assignment.entity.Trade;


public interface HistoryMapper {
    /**
     * 插入交易資料
     *
     * @param trade 交易資料
     * @return 插入的行數
     */
    Integer insert(Trade trade);
}
