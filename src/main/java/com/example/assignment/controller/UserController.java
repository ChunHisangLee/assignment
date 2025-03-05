package com.example.assignment.controller;

import com.example.assignment.constants.MessagesConstants;
import com.example.assignment.dto.ResponseDto;
import com.example.assignment.dto.UsersDto;
import com.example.assignment.security.JwtAuthenticationResponse;
import com.example.assignment.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<ResponseDto> registerUser(@RequestBody UsersDto usersDto) {
    userService.registerUser(usersDto);
    log.info("User registered successfully with ID: {}", usersDto.getId());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ResponseDto(MessagesConstants.STATUS_201, MessagesConstants.MESSAGE_201));
  }

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

  @GetMapping("/{id}")
  public ResponseEntity<UsersDto> getUserById(@PathVariable Long id) {
    UsersDto usersDto = userService.getUserById(id);
    log.info("User with ID: {} found.", id);
    return ResponseEntity.status(HttpStatus.OK).body(usersDto);
  }

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
