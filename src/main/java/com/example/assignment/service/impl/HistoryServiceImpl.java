package com.example.assignment.service.impl;

import com.example.assignment.entity.History;
import com.example.assignment.mapper.HistoryMapper;
import com.example.assignment.service.IHistoryService;
import com.example.assignment.service.exception.InsertException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HistoryServiceImpl implements IHistoryService {

    private final HistoryMapper historyMapper;

    public HistoryServiceImpl(HistoryMapper historyMapper) {
        this.historyMapper = historyMapper;
    }

    @Override
    public void createHistory(History history) {
        String historyId = generateHistoryId();
        history.setHistoryId(historyId);
        Integer rows = historyMapper.insert(history);
        if (rows != 1) {
            throw new InsertException("Unknown exception while inserting history!");
        }
    }

    @Override
    public List<History> getHistories(String userId) {
        return historyMapper.findByUserId(userId);
    }

    @Override
    public Integer deleteHistory(String userId) {
        return historyMapper.deleteHistory(userId);
    }

    private String generateHistoryId() {
        return UUID.randomUUID().toString().toUpperCase();
    }
}
