package com.example.assignment.service;

import com.example.assignment.entity.User;

public interface IAccountService {
    /**
     * 使用者帳號資料註冊
     *
     * @param user
     * @param coinId
     */
    void reg(User user, Integer coinId);
}
