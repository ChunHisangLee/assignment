package com.example.assignment.mapper;

import com.example.assignment.entity.Trade;


public interface HistoryMapper {
    /**
     * Insert trading data
     *
     * @param trade trading data
     * @return the row to be inserted
     */
    Integer insert(Trade trade);
}
