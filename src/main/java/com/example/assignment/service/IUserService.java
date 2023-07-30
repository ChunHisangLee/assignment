package com.example.assignment.service;

import com.example.assignment.entity.User;

public interface IUserService {
    void register(User user);

    Integer deleteUser(User user);

    User getUser(User user);

    User getUser(String userId);
}
