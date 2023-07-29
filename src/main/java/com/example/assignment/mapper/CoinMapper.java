package com.example.assignment.mapper;

import com.example.assignment.entity.Coin;


public interface CoinMapper {
    Integer insert(Coin coin);
    Coin getCoin(Coin coin);
    Coin getCoin(String coinName);
    Coin getCoin(Integer coinId);
}
