package com.example.assignment.mapper;

import com.example.assignment.entity.Account;


public interface AccountMapper {
    Integer insert(Account account);

    Integer setUSDNetValue(Account account);

    Account findByKey(String userId, Integer coinId);

    Integer updateTradingBalance(Account account);
}
