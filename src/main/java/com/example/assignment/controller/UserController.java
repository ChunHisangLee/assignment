package com.example.assignment.controller;

import com.example.assignment.dto.UsersDTO;
import com.example.assignment.entity.Users;
import com.example.assignment.exception.CustomErrorException;
import com.example.assignment.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UsersDTO> registerUser(@Valid @RequestBody Users users) {
        Users createdUser = userService.registerUser(users);
        return ResponseEntity.ok(toDTO(createdUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsersDTO> updateUser(@PathVariable Long id, @Valid @RequestBody Users users) {
        Users updatedUser = userService.updateUser(id, users)
                .orElseThrow(() -> new CustomErrorException(
                        HttpStatus.NOT_FOUND.value(),
                        "Not Found",
                        "User not found.",
                        "PUT /api/users/" + id
                ));
        return ResponseEntity.ok(toDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> getUserById(@PathVariable Long id) {
        Users user = userService.getUserById(id)
                .orElseThrow(() -> new CustomErrorException(
                        HttpStatus.NOT_FOUND.value(),
                        "Not Found",
                        "User not found.",
                        "GET /api/users/" + id
                ));
        return ResponseEntity.ok(toDTO(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UsersDTO> login(@RequestBody Users loginRequest) {
        Users user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(toDTO(user));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResponseEntity.ok().build();
    }

    private UsersDTO toDTO(Users user) {
        return new UsersDTO(user.getId(), user.getName(), user.getEmail());
    }
}
