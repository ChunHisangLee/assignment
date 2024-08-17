package com.example.assignment.service;

import com.example.assignment.entity.Users;

import java.util.Optional;

public interface UserService {
    Users createUser(Users users);
    Optional<Users> updateUser(Long id, Users users);
    boolean deleteUser(Long id);
    Optional<Users> getUserById(Long id);
    Optional<Users> findByEmail(String email);
    boolean verifyPassword(String rawPassword, String encodedPassword);
}
