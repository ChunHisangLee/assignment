package com.example.assignment.service.impl;

import com.example.assignment.entity.User;
import com.example.assignment.mapper.UserMapper;
import com.example.assignment.service.IPasswordService;
import com.example.assignment.service.IUserService;
import com.example.assignment.service.exception.InsertException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    private final UserMapper userMapper;
    private final IPasswordService passwordService;

    public UserServiceImpl(UserMapper userMapper, IPasswordService passwordService) {
        this.userMapper = userMapper;
        this.passwordService = passwordService;
    }


    @Override
    public void register(User user) {
        user.setUserId(passwordService.generateUUID());
        user.setSalt(passwordService.generateUUID());
        user.setPassword(passwordService.hashPassword(user.getPassword(), user.getSalt()));
        user.setCreateTime(passwordService.getCurrentTime());
        user.setUpdateTime(passwordService.getCurrentTime());
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
}
