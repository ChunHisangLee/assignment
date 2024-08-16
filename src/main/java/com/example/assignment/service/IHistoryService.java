package com.example.assignment.service;

import com.example.assignment.entity.Transaction;

import java.util.List;

public interface IHistoryService {
    void createHistory(Transaction history);

    List<Transaction> getHistories(String userId);

    Integer deleteHistory(String userId);
}
