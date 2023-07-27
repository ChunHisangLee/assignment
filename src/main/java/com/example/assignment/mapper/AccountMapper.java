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

    /**
     * 設定帳戶初始值
     *
     * @param userId   使用者ID
     * @param coinId   幣別ID
     * @param netValue 淨值
     * @return 插入的行數
     */

    Integer setUSDNetValue(Integer userId, Integer coinId, Integer netValue);

    /**
     * 查詢帳戶資料
     *
     * @param userId 使用者ID
     * @param coinId 幣別ID
     * @return Account
     */
    Account findByKey(Integer userId, Integer coinId);
}
