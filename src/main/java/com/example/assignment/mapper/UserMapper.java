package com.example.assignment.mapper;

import com.example.assignment.entity.User;


public interface UserMapper {
    Integer insert(User user);

    User getUser(User user);

    User getUser(String userId);

    Integer deleteUser(User user);
}
