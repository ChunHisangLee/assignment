package com.example.assignment.mapper;

import com.example.assignment.dto.UsersDTO;
import com.example.assignment.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {

    private final WalletMapper walletMapper;

    public UsersMapper(WalletMapper walletMapper) {
        this.walletMapper = walletMapper;
    }

    public UsersDTO convertToDto(Users users) {
        return new UsersDTO(
                users.getId(),
                users.getName(),
                users.getEmail()
        );
    }

    public Users convertToEntity(UsersDTO usersDTO) {
        Users users = new Users();
        users.setId(usersDTO.getId());
        users.setName(usersDTO.getName());
        users.setEmail(usersDTO.getEmail());
        users.setPassword(usersDTO.getPassword()); // Map password from DTO to entity
        return users;
    }
}
