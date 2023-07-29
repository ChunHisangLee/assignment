package com.example.assignment.service;

import com.example.assignment.entity.Coin;

public interface ICoinService {
    /**
     * Create coin data
     * @param coin  coin Class
     */
    void createCoin(Coin coin);
    Coin getCoin(Coin coin);
    Coin getCoin(String coinName);
    Coin getCoin(Integer coinId);
}
