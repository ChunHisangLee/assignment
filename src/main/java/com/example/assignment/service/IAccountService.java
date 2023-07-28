package com.example.assignment.service;

import com.example.assignment.entity.User;

public interface IAccountService {
    /**
     * Create a user account
     *
     * @param user  user Class
     * @param coinName  coin name
     */
    void createAccount(User user, String coinName);
}
