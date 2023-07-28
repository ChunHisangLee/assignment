package com.example.assignment.service;

import com.example.assignment.entity.Trade;

import java.util.List;

public interface IHistoryService {
    /**
     * Save trading history data
     *
     * @param list an ArrayList of trade
     */
    void createHistory(List<Trade> list);
}
