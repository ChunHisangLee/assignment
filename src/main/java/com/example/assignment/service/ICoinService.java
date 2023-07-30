package com.example.assignment.service;

import com.example.assignment.entity.Coin;

public interface ICoinService {
    void createCoin(Coin coin);

    Coin getCoin(String coinName);

    Coin getCoin(Integer coinId);
}
