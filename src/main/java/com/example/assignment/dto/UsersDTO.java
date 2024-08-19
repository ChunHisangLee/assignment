package com.example.assignment.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class UsersDTO {
    private Long id;
    private String name;
    private String email;
    private String password; // Only used for registration or updates, not returned in responses

    public UsersDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
