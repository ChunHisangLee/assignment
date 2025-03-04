package com.example.assignment.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDto {
  private Long id;
  private String name;
  private String email;
  private String password; // Only used for registration or updates, not returned in responses
}
