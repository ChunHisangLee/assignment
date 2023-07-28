package com.example.assignment.service;

import com.example.assignment.entity.Trade;

import java.util.List;

public interface ITradeService {
    List<Trade> createTrade(Double quantity, String direction, String userName, String coinName);
}