package com.example.assignment.mapper;

import com.example.assignment.entity.User;


public interface UserMapper {
    /**
     * Insert user data
     *
     * @param user user data
     * @return the row to be inserted
     */
    Integer insert(User user);

    /**
     * Query user data by userName
     *
     * @param userName userName
     * @return if not find, return null value
     */
    User findByUserName(String userName);

    /**
     * Delete user data by userName
     *
     * @param userName userName
     * @return the row to be deleted
     */
    Integer deleteByUserName(String userName);
}
