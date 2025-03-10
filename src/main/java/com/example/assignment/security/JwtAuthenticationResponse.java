package com.example.assignment.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "JwtAuthenticationResponse", description = "JWT authentication response")
@Getter
@Setter
@AllArgsConstructor
public class JwtAuthenticationResponse {
  @Schema(description = "JWT token")
  private String token;
}
