package com.example.assignment.service;

import com.example.assignment.entity.Account;

public interface IAccountService {
    /**
     * 使用者註冊
     * @param userId
     * @param coinId
     */
    void reg(Integer userId,Integer coinId);
}
