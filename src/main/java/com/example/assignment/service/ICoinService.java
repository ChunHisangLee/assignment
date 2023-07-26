package com.example.assignment.service;

import com.example.assignment.entity.Coin;

public interface ICoinService {
    /**
     * 使用者註冊
     * @param coin
     */
    void createCoin(Coin coin);
}
