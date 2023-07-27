package com.example.assignment.service.impl;

import com.example.assignment.entity.Trade;
import com.example.assignment.mapper.HistoryMapper;
import com.example.assignment.service.IHistoryService;
import com.example.assignment.service.ex.InsertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryServiceImpl implements IHistoryService {
    @Autowired
    private HistoryMapper historyMapper;

    @Override
    public void createHistory(List<Trade> list) {
        for (Trade trade : list) {
            Integer rows = historyMapper.insert(trade);
            if (rows != 1) {
                throw new InsertException("在儲存交易歷史資料的過程中產生了未知的異常");
            }
        }
    }
}
