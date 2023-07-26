package com.example.assignment.mapper;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.Coin;


public interface CoinMapper {
    /**
     * 插入幣別資料
     *
     * @param coin 幣別資料
     * @return 插入的行數
     */
    Integer insert(Coin coin);
    /**
     * 根據使幣別名稱查詢幣別的數據
     *
     * @param name 幣別名稱
     * @return 沒找到傳回null值
     */
    Coin findByName(String name);
}
