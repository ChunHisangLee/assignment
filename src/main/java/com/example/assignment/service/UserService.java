package com.example.assignment.service;

import com.example.assignment.dto.UsersDto;

public interface UserService {

  void registerUser(UsersDto usersDto);

  boolean updateUser(Long id, UsersDto usersDto);

  boolean deleteUser(Long id);

  String login(UsersDto usersDto);

  UsersDto getUserById(Long id);

  boolean verifyPassword(String rawPassword, String encodedPassword);
}
