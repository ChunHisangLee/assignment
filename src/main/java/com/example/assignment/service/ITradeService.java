package com.example.assignment.service;

import com.example.assignment.entity.Trade;

import java.util.Date;
import java.util.List;

public interface ITradeService {
    List<Trade> createTrade(Integer price, Double quantity, String direction, String userName, String coinName, Date date);
}