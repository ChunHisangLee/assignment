package com.example.assignment.controller;

import com.example.assignment.dto.UsersDTO;
import com.example.assignment.entity.Users;
import com.example.assignment.error.CustomErrorResponse;
import com.example.assignment.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody Users users) {
        if (emailExists(users.getEmail())) {
            CustomErrorResponse errorResponse = new CustomErrorResponse("Email already registered. Try to log in or register with another email.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users createdUser = userService.createUser(users);

        return ResponseEntity.ok(toDTO(createdUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsersDTO> updateUser(@PathVariable Long id, @Valid @RequestBody Users users) {
        if (isEmailRegisteredForAnotherUser(users.getEmail(), id)) {
            CustomErrorResponse errorResponse = new CustomErrorResponse("Email already registered by another user.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        Optional<Users> updatedUser = userService.updateUser(id, users);
        return updatedUser
                .map(user -> ResponseEntity.ok(toDTO(user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(toDTO(user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users loginRequest) {
        Optional<Users> user = userService.findByEmail(loginRequest.getEmail());

        if (user.isPresent() && userService.verifyPassword(loginRequest.getPassword(), user.get().getPassword())) {
            return ResponseEntity.ok(toDTO(user.get()));
        } else {
            CustomErrorResponse errorResponse = new CustomErrorResponse("Invalid email or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return ResponseEntity.ok().build();
    }

    private boolean emailExists(String email) {
        return userService.findByEmail(email).isPresent();
    }

    private boolean isEmailRegisteredForAnotherUser(String email, Long userId) {
        Optional<Users> existingUser = userService.findByEmail(email);
        return existingUser.isPresent() && !existingUser.get().getId().equals(userId);
    }

    private UsersDTO toDTO(Users user) {
        return new UsersDTO(user.getId(), user.getName(), user.getEmail());
    }
}
