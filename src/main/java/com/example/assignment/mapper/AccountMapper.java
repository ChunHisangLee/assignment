package com.example.assignment.mapper;

import com.example.assignment.entity.Account;


public interface AccountMapper {
    /**
     * 插入使用者帳戶資料
     *
     * @param account 使用者帳戶資料
     * @return 插入的行數
     */
    Integer insert(Account account);
    Integer setUSDNetValue(Integer userId,Integer coinId,Integer netValue);
}
