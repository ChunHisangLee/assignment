package com.example.assignment.service;

import com.example.assignment.entity.User;

public interface IUserService {
    /**
     * Register the user data
     * @param user  user Class
     */
    void register(User user);
    /**
     * Delete user
     * @param user  user Class
     */
    void deleteUser(User user);
}
