package com.example.assignment.service;

import com.example.assignment.entity.User;

public interface IUserService {
    /**
     * 使用者註冊
     * @param user
     */
    void register(User user);
    /**
     * 使用者刪除
     * @param user
     */
    void deleteUser(User user);
}
