package com.example.assignment.service;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.Coin;
import com.example.assignment.entity.User;

public interface IAccountService {
    void createAccount(User user, Coin coin);

    Account getAccount(String userId, int coinId);
}
