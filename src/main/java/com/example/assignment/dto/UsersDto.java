package com.example.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Schema(name = "Users", description = "User details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDto {
  @Schema(description = "ID of the user", example = "1")
  private Long id;

  @Schema(description = "Name of the user", example = "John Doe")
  @Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
  private String name;

  @Schema(description = "Email of the user", example = "1X2V2@example.com")
  @Email
  private String email;

  @Schema(description = "Password of the user", example = "password123")
  private String password; // Only used for registration or updates, not returned in responses
}
