package com.example.assignment.service;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.Coin;
import com.example.assignment.entity.User;

import java.util.List;

public interface IAccountService {
    void createAccount(User user, Coin coin);

    Account getAccount(String userId, int coinId);

    List<Account> getAccounts(String userId);

    Integer deleteAccount(String userId);
}
