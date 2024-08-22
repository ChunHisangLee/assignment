package com.example.assignment.mapper;

import com.example.assignment.dto.UsersDTO;
import com.example.assignment.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {

    public UsersDTO toDto(Users users) {
        return UsersDTO.builder()
                .id(users.getId())
                .name(users.getName())
                .email(users.getEmail())
                .build();
    }

    public Users toEntity(UsersDTO usersDTO) {
        return Users.builder()
                .name(usersDTO.getName())
                .email(usersDTO.getEmail())
                .password(usersDTO.getPassword()) // Ensure password is mapped if provided
                .build();
    }
}
