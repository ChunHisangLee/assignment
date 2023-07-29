package com.example.assignment.service.impl;

import com.example.assignment.entity.User;
import com.example.assignment.mapper.UserMapper;
import com.example.assignment.service.IUserService;
import com.example.assignment.service.ex.InsertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(User user) {
        String oldPassword = user.getPassword();
        String salt = UUID.randomUUID().toString().toUpperCase();
        user.setSalt(salt);
        String md5Password = getMD5Password(oldPassword, salt);
        user.setPassword(md5Password);
        Instant instant = Instant.now();
        user.setCreateTime(LocalDateTime.from(instant));
        user.setUpdateTime(LocalDateTime.from(instant));
        Integer rows = userMapper.insert(user);
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
        }
    }

    @Override
    public Integer deleteUser(User user) {
        return userMapper.deleteUser(user);
    }

    @Override
    public User getUser(User user) {
        return userMapper.getUser(user);
    }

    @Override
    public User getUser(String userId) {
        return userMapper.getUser(userId);
    }

    // md5加密
    private String getMD5Password(String password, String salt) {
        for (int i = 0; i < 3; i++) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        return password;
    }
}
