package com.example.assignment.service.impl;

import com.example.assignment.entity.History;
import com.example.assignment.mapper.HistoryMapper;
import com.example.assignment.service.IHistoryService;
import com.example.assignment.service.exception.InsertException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HistoryServiceImpl implements IHistoryService {
    @Autowired
    private HistoryMapper historyMapper;

    @Override
    public void createHistory(@NotNull History history) {
        String historyId = UUID.randomUUID().toString().toUpperCase();
        history.setHistoryId(historyId);
        Integer rows = historyMapper.insert(history);
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
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
}
