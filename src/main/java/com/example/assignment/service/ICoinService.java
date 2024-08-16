package com.example.assignment.service;

import com.example.assignment.entity.Wallet;

public interface ICoinService {
    void createCoin(Wallet coin);

    Wallet getCoin(String coinName);

    Wallet getCoin(Integer coinId);
}
