package com.example.assignment.service.impl;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.User;
import com.example.assignment.mapper.AccountMapper;
import com.example.assignment.service.IAccountService;
import com.example.assignment.service.ex.InsertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public void reg(User user, Integer coinId) {
        Account account = new Account();
        account.setUserId(user.getUserId());
        account.setCoinId(coinId);
        account.setAccountStatus("正常");
        account.setCreateUser(user.getUserName());
        account.setUpdateUser(user.getUserName());
        Date date = new Date();
        account.setCreateTime(date);
        account.setUpdateTime(date);

        Integer rows = accountMapper.insert(account);
        if (rows != 1) {
            throw new InsertException("在註冊的過程中產生了未知的異常");
        }
    }
}
