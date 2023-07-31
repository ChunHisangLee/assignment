package com.example.assignment.service;

import com.example.assignment.entity.History;

import java.util.List;

public interface IHistoryService {
    void createHistory(History history);

    List<History> getHistories(String userId);

    Integer deleteHistory(String userId);
}
