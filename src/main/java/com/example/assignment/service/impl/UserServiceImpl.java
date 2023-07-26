package com.example.assignment.service.impl;

import com.example.assignment.entity.User;
import com.example.assignment.mapper.UserMapper;
import com.example.assignment.service.IUserService;
import com.example.assignment.service.ex.InsertException;
import com.example.assignment.service.ex.UserNameDuplicatedExecption;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.stream.IIOByteBuffer;
import java.time.Instant;
import java.util.Date;

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
}
