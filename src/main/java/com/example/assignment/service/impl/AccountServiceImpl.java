package com.example.assignment.service.impl;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.Coin;
import com.example.assignment.entity.User;
import com.example.assignment.mapper.AccountMapper;
import com.example.assignment.mapper.CoinMapper;
import com.example.assignment.service.IAccountService;
import com.example.assignment.service.ex.InsertException;
import com.example.assignment.service.ex.UserNameDuplicatedExecption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private CoinMapper coinMapper;

    @Override
    public void createAccount(User user, String coinName) {
        Account account = new Account();
        account.setUserId(user.getUserId());
        Coin coinQuery = coinMapper.findByName(coinName);
        if (coinQuery != null) {
            throw new UserNameDuplicatedExecption("該幣別資料已經存在!");
        }
        account.setCoinId(coinQuery.getCoinId());
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
