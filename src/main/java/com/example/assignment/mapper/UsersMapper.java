package com.example.assignment.mapper;

import com.example.assignment.dto.UsersDto;
import com.example.assignment.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {

  public UsersDto toDto(Users users) {
    return UsersDto.builder()
        .id(users.getId())
        .name(users.getName())
        .email(users.getEmail())
        .build();
  }

  public Users toEntity(UsersDto usersDto) {
    return Users.builder()
        .id(usersDto.getId())
        .name(usersDto.getName())
        .email(usersDto.getEmail())
        .password(usersDto.getPassword())
        .build();
  }
}
