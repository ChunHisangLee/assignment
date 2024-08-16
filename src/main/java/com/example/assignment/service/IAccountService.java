package com.example.assignment.service;

import com.example.assignment.entity.Wallet;
import com.example.assignment.entity.User;

import java.util.List;

public interface IAccountService {
    void createAccount(User user, Wallet coin);

    Account getAccount(String userId, int coinId);

    List<Account> getAccounts(String userId);

    Integer deleteAccount(String userId);
}
