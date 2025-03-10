package com.example.assignment.controller;

import com.example.assignment.constants.MessagesConstants;
import com.example.assignment.dto.UsersDto;
import com.example.assignment.response.ErrorResponseDto;
import com.example.assignment.response.ResponseDto;
import com.example.assignment.security.JwtAuthenticationResponse;
import com.example.assignment.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "CRUD REST APIs for User",
    description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE")
@RestController
@Slf4j
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

  private final UserService userService;

  @Operation(summary = "Register a new user", description = "REST API to register a new user")
  @ApiResponses({
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_201,
        description = MessagesConstants.MESSAGE_201),
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_409,
        description = MessagesConstants.MESSAGE_409),
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_500,
        description = MessagesConstants.MESSAGE_500,
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @PostMapping("/register")
  public ResponseEntity<ResponseDto> registerUser(@RequestBody UsersDto usersDto) {
    userService.registerUser(usersDto);
    log.info("User registered successfully with ID: {}", usersDto.getId());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ResponseDto(MessagesConstants.STATUS_201, MessagesConstants.MESSAGE_201));
  }

  @Operation(
      summary = "Update an existing user",
      description = "REST API to update an existing user")
  @ApiResponses({
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_200,
        description = MessagesConstants.MESSAGE_200),
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_417,
        description = MessagesConstants.MESSAGE_417_UPDATE),
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_500,
        description = MessagesConstants.MESSAGE_500,
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @PutMapping("/{id}")
  public ResponseEntity<ResponseDto> updateUser(
      @PathVariable Long id, @RequestBody UsersDto usersDto) {
    boolean isUpdated = userService.updateUser(id, usersDto);

    if (isUpdated) {
      log.info("User with ID: {} updated successfully.", id);
      return ResponseEntity.status(HttpStatus.OK)
          .body(new ResponseDto(MessagesConstants.STATUS_200, MessagesConstants.MESSAGE_200));
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
          .body(
              new ResponseDto(MessagesConstants.STATUS_417, MessagesConstants.MESSAGE_417_UPDATE));
    }
  }

  @Operation(
      summary = "Delete an existing user",
      description = "REST API to delete an existing user")
  @ApiResponses({
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_200,
        description = MessagesConstants.MESSAGE_200),
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_417,
        description = MessagesConstants.MESSAGE_417_DELETE),
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_500,
        description = MessagesConstants.MESSAGE_500,
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseDto> deleteUser(@PathVariable Long id) {
    boolean isDeleted = userService.deleteUser(id);

    if (isDeleted) {
      log.info("User with ID: {} deleted successfully.", id);
      return ResponseEntity.status(HttpStatus.OK)
          .body(new ResponseDto(MessagesConstants.STATUS_200, MessagesConstants.MESSAGE_200));
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
          .body(
              new ResponseDto(MessagesConstants.STATUS_417, MessagesConstants.MESSAGE_417_DELETE));
    }
  }

  @Operation(summary = "Get user by ID", description = "REST API to get a user by ID")
  @ApiResponses({
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_200,
        description = MessagesConstants.MESSAGE_200),
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_500,
        description = MessagesConstants.MESSAGE_500,
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @GetMapping("/{id}")
  public ResponseEntity<UsersDto> getUserById(@PathVariable Long id) {
    UsersDto usersDto = userService.getUserById(id);
    log.info("User with ID: {} found.", id);
    return ResponseEntity.status(HttpStatus.OK).body(usersDto);
  }

  @Operation(summary = "Login a user", description = "REST API to login a user")
  @ApiResponses({
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_200,
        description = MessagesConstants.MESSAGE_200),
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_401,
        description = MessagesConstants.MESSAGE_401),
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_500,
        description = MessagesConstants.MESSAGE_500,
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @PostMapping("/login")
  public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody UsersDto userDto) {
    log.info("User login attempt with email: {}", userDto.getEmail());
    String token = userService.login(userDto);

    if (token != null) {
      log.info("User with email: {} logged in successfully.", userDto.getEmail());
      return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    } else {
      log.error("Invalid credentials for email: {}", userDto.getEmail());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
  }

  @Operation(summary = "Logout a user", description = "REST API to logout a user")
  @ApiResponses({
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_200,
        description = MessagesConstants.MESSAGE_200),
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_500,
        description = MessagesConstants.MESSAGE_500,
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @GetMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      log.info("User with principal: {} logging out.", authentication.getName());
      new SecurityContextLogoutHandler().logout(request, response, authentication);
    }
    return ResponseEntity.ok().build();
  }
}
