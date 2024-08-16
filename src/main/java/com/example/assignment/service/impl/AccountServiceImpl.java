package com.example.assignment.service.impl;

import com.example.assignment.entity.Wallet;
import com.example.assignment.entity.User;
import com.example.assignment.mapper.AccountMapper;
import com.example.assignment.service.IAccountService;
import com.example.assignment.service.exception.InsertException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class AccountServiceImpl implements IAccountService {
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    public void createAccount(@NotNull User user, @NotNull Wallet coin) {
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

    @Override
    public Account getAccount(String userId, int coinId) {
        return accountMapper.findByKey(userId, coinId);
    }
    @Override
    public List<Account> getAccounts(String userId) {
        return accountMapper.findByUserId(userId);
    }

    @Override
    public Integer deleteAccount(String userId) {
        return accountMapper.deleteAccount(userId);
    }
}
