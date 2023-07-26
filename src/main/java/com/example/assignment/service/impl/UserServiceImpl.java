package com.example.assignment.service.impl;

import com.example.assignment.entity.User;
import com.example.assignment.mapper.UserMapper;
import com.example.assignment.service.IUserService;
import com.example.assignment.service.ex.InsertException;
import com.example.assignment.service.ex.UserNameDuplicatedExecption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void reg(User user) {
        String userName = user.getUserName();
        User result = userMapper.findByUserName(userName);
        if (result != null) {
            throw new UserNameDuplicatedExecption("該名稱已被註冊!");
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
    }

    // md5加密
    private String getMD5Password(String password, String salt) {
        for (int i = 0; i < 3; i++) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        return password;
    }
}
