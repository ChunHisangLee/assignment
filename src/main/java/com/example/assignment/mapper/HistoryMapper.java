package com.example.assignment.mapper;


import com.example.assignment.entity.Transaction;

import java.util.List;

public interface HistoryMapper {
    Integer insert(Transaction history);
    List<Transaction> findByUserId(String userId);

    Integer deleteHistory(String userId);
}
