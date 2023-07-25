package com.example.assignment.mapper;

import com.example.assignment.entity.User;


public interface UserMapper {
    /**
     * 插入使用者資料
     *
     * @param user 使用者資料
     * @return 插入的行數
     */
    Integer insert(User user);

    /**
     * 根據使用者的名稱來查詢使用者的數據
     *
     * @param userName 使用者名稱
     * @return 沒找到傳回null值
     */
    User findByUserName(String userName);
}
