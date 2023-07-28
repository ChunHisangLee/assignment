package com.example.assignment.service.impl;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.Coin;
import com.example.assignment.entity.User;
import com.example.assignment.mapper.AccountMapper;
import com.example.assignment.mapper.CoinMapper;
import com.example.assignment.mapper.UserMapper;
import com.example.assignment.service.IAccountService;
import com.example.assignment.service.IUserService;
import com.example.assignment.service.ex.InsertException;
import com.example.assignment.service.ex.ServiceException;
import com.example.assignment.service.ex.UserNameDuplicatedException;
import com.example.assignment.service.ex.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private CoinMapper coinMapper;
    @Autowired
    private IAccountService accountService;

    @Override
    public void register(User user) {
        final int INITIAL_VALUE = 1000;
        String userName = user.getUserName();
        User userQuery = userMapper.findByUserName(userName);
        if (userQuery != null) {
            throw new UserNameDuplicatedException("The userName has been registered!");
        }

        String oldPassword = user.getPassword();
        String salt = UUID.randomUUID().toString().toUpperCase();
        user.setSalt(salt);
        String md5Password = getMD5Password(oldPassword, salt);
        user.setPassword(md5Password);

        user.setIsDeleted(0);
        user.setCreateUser(user.getUserName());
        user.setUpdateUser(user.getUserName());
        Instant instant = Instant.now();
        user.setCreateTime(Date.from(instant));
        user.setUpdateTime(Date.from(instant));
        Integer rows = userMapper.insert(user);
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
        }

        accountService.createAccount(user, "USD");
        Coin coinQuery = coinMapper.findByName("USD");
        if (coinQuery == null) {
            throw new ServiceException("The coin data doesn't exist!");
        }
        Account account = new Account();
        account.setUserId(user.getUserId());
        account.setCoinId(coinQuery.getCoinId());
        account.setNetValue(BigDecimal.valueOf(INITIAL_VALUE));
        instant = Instant.now();
        account.setUpdateTime(Date.from(instant));
        accountMapper.setUSDNetValue(account);
        accountService.createAccount(user, "BTC");
    }

    @Override
    public void deleteUser(User user) {
        String userName = user.getUserName();
        User userQuery = userMapper.findByUserName(userName);
        if (userQuery == null) {
            throw new UserNotFoundException("The user data doesn't exist!");
        }
        Integer rows = userMapper.deleteByUserName(userName);
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
        }
    }

    // md5加密
    private String getMD5Password(String password, String salt) {
        for (int i = 0; i < 3; i++) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        return password;
    }
}
