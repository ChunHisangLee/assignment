package com.example.assignment.service;

import com.example.assignment.entity.Trade;

import java.util.List;

public interface IHistoryService {
    /**
     * 儲存交易歷史資料
     *
     * @param list
     */
    void createHistory(List<Trade> list);
}
