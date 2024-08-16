package com.example.assignment.service;

import com.example.assignment.entity.User;

import java.util.Optional;

public interface UserService {
    User createUser(User user);
    Optional<User> updateUser(Long id, User user);
    boolean deleteUser(Long id);
    Optional<User> getUserById(Long id);
}
