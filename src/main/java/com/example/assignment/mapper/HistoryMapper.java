package com.example.assignment.mapper;


import com.example.assignment.entity.History;

import java.util.List;

public interface HistoryMapper {
    Integer insert(History history);
    List<History> findByUserId(String userId);

    Integer deleteHistory(String userId);
}
