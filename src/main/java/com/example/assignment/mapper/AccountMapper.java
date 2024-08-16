package com.example.assignment.mapper;

import java.util.List;


public interface AccountMapper {
    Integer insert(Account account);

    Integer setUSDNetValue(Account account);

    Account findByKey(String userId, Integer coinId);

    List<Account> findByUserId(String userId);

    Integer updateTradingBalance(Account account);

    Integer deleteAccount(String userId);
}
