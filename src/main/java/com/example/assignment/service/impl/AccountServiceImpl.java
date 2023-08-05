package com.example.assignment.service.impl;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.Coin;
import com.example.assignment.entity.User;
import com.example.assignment.mapper.AccountMapper;
import com.example.assignment.service.IAccountService;
import com.example.assignment.service.exception.InsertException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public void createAccount(@NotNull User user, @NotNull Coin coin) {
        Account account = new Account();
        account.setUserId(user.getUserId());
        account.setCoinId(coin.getCoinId());
        if ("USD".equals(coin.getCoinName())) {
            account.setNetValue(BigDecimal.valueOf(1000));
        }
        account.setAccountStatus("Normal");
        Instant instant = Instant.now();
        account.setCreateTime(Date.from(instant));
        account.setUpdateTime(Date.from(instant));
        Integer rows = accountMapper.insert(account);
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
        }
    }

    /**
     * Obtains the Account with a specific userId and coinId
     * <p>
     * It will return null if the Account doesn't exist
     *
     * @param userId the ID of a User
     * @param coinId the ID of a Coin
     * @return an Account or null
     */
    @Override
    public Account getAccount(String userId, int coinId) {
        return accountMapper.findByKey(userId, coinId);
    }

    /**
     * Obtains the Account(s) with a specific userId
     * <p>
     * It will return null if the Account doesn't exist
     *
     * @param userId the ID of a User
     * @return Account(s) or null
     */
    @Override
    public List<Account> getAccounts(String userId) {
        return accountMapper.findByUserId(userId);
    }

    @Override
    public Integer deleteAccount(String userId) {
        return accountMapper.deleteAccount(userId);
    }
}
