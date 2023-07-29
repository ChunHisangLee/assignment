package com.example.assignment.service.impl;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.Coin;
import com.example.assignment.entity.User;
import com.example.assignment.mapper.AccountMapper;
import com.example.assignment.service.IAccountService;
import com.example.assignment.service.ex.InsertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public void createAccount(User user, Coin coin) {
        Account account = new Account();
        account.setUserId(user.getUserId());
        account.setCoinId(coin.getCoinId());
        if ("USD".equals(coin.getCoinName())) {
            account.setNetValue(BigDecimal.valueOf(100));
        }
        account.setAccountStatus("Normal");
        Instant instant = Instant.now();
        account.setCreateTime(LocalDateTime.from(instant));
        account.setUpdateTime(LocalDateTime.from(instant));
        Integer rows = accountMapper.insert(account);
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
        }
    }

    @Override
    public Account getAccount(String userId, int coinId) {
        return accountMapper.findByKey(userId, coinId);
    }
}
