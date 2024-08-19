package com.example.assignment.mapper;

import com.example.assignment.dto.UsersDTO;
import com.example.assignment.dto.WalletDTO;
import com.example.assignment.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {

    private final WalletMapper walletMapper;

    public UsersMapper(WalletMapper walletMapper) {
        this.walletMapper = walletMapper;
    }

    public UsersDTO convertToDto(Users users) {
        WalletDTO walletDTO = walletMapper.convertToDto(users.getWallet());
        return new UsersDTO(users.getId(), users.getName(), users.getEmail(), walletDTO);
    }
}
