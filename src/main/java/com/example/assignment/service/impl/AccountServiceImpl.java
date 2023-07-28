package com.example.assignment.service.impl;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.Coin;
import com.example.assignment.entity.User;
import com.example.assignment.mapper.AccountMapper;
import com.example.assignment.mapper.CoinMapper;
import com.example.assignment.service.IAccountService;
import com.example.assignment.service.ex.InsertException;
import com.example.assignment.service.ex.UserNameDuplicatedException;
import com.example.assignment.service.ex.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private CoinMapper coinMapper;

    @Override
    public void createAccount(User user, String coinName) {
        Coin coinQuery = coinMapper.findByName(coinName);
        if (coinQuery == null) {
            throw new UserNotFoundException("The coin data doesn't exist!");
        }
        Account accountQuery = accountMapper.findByKey(user.getUserId(), coinQuery.getCoinId());
        if (accountQuery != null) {
            throw new UserNameDuplicatedException("The user already has a(n) " + coinName + " account!");
        }
        Account account = new Account();
        account.setUserId(user.getUserId());
        account.setCoinId(coinQuery.getCoinId());
        account.setAccountStatus("Normal");
        account.setCreateUser(user.getUserName());
        account.setUpdateUser(user.getUserName());
        Instant instant = Instant.now();
        account.setCreateTime(Date.from(instant));
        account.setUpdateTime(Date.from(instant));
        Integer rows = accountMapper.insert(account);
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
        }
    }
}
