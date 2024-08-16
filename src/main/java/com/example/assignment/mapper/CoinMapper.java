package com.example.assignment.mapper;

import com.example.assignment.entity.Wallet;


public interface CoinMapper {
    Integer insert(Wallet coin);
    Wallet getCoin(Wallet coin);
    Wallet getCoin(String coinName);
    Wallet getCoin(Integer coinId);
}
