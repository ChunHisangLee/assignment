package com.example.assignment.service;

import com.example.assignment.entity.User;

public interface IAccountService {
    /**
     * 使用者帳號資料註冊
     *
     * @param user
     * @param coinName
     */
    void createAccount(User user, String coinName);
}
