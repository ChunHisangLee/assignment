package com.example.assignment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsersDTO {
    private Long id;
    private String name;
    private String email;
    private WalletDTO wallet;

    public UsersDTO(Long id, String name, String email, WalletDTO wallet) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.wallet = wallet;
    }
}
