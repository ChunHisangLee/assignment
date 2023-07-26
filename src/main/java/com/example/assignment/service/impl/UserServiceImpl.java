package com.example.assignment.service.impl;

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
            throw new UserNameDuplicatedException("該名稱已被註冊!");
        }

        String oldPassword = user.getPassword();
        String salt = UUID.randomUUID().toString().toUpperCase();
        user.setSalt(salt);
        String md5Password = getMD5Password(oldPassword, salt);
        user.setPassword(md5Password);

        user.setIsDeleted(0);
        user.setCreateUser(user.getUserName());
        user.setUpdateUser(user.getUserName());
        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        Integer rows = userMapper.insert(user);
        if (rows != 1) {
            throw new InsertException("在註冊的過程中產生了未知的異常");
        }

        accountService.createAccount(user, "USD");
        Coin coinQuery = coinMapper.findByName("USD");
        if (coinQuery == null) {
            throw new ServiceException("該幣別資料不存在!");
        }
        accountMapper.setUSDNetValue(user.getUserId(), coinQuery.getCoinId(), INITIAL_VALUE);
        accountService.createAccount(user, "BTC");
    }

    @Override
    public void deleteUser(User user) {
        String userName = user.getUserName();
        User result = userMapper.findByUserName(userName);
        if (result == null) {
            throw new UserNotFoundException("無該筆資料!");
        }
        Integer rows = userMapper.deleteByUserName(userName);
        if (rows != 1) {
            throw new InsertException("在刪除的過程中產生了未知的異常");
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
