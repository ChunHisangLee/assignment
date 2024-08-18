package com.example.assignment.controller;

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
    private final PasswordEncoder passwordEncoder;  // Inject PasswordEncoder

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;  // Initialize PasswordEncoder
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody Users users) {
        if (isEmailRegistered(users.getEmail())) {
            CustomErrorResponse errorResponse = new CustomErrorResponse("Email already registered. Try to log in or register with another email.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        users.setPassword(passwordEncoder.encode(users.getPassword()));

        Users createdUsers = userService.createUser(users);

        return ResponseEntity.ok(removePassword(createdUsers));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody Users users) {
        if (isEmailRegisteredForAnotherUser(users.getEmail(), id)) {
            CustomErrorResponse errorResponse = new CustomErrorResponse("Email already registered by another user.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        Optional<Users> updatedUser = userService.updateUser(id, users);
        return updatedUser.map(user -> ResponseEntity.ok(removePassword(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);

        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Optional<Users> user = userService.getUserById(id);
        return user.map(u -> ResponseEntity.ok(removePassword(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users loginRequest) {
        Optional<Users> user = userService.findByEmail(loginRequest.getEmail());

        if (user.isPresent() && userService.verifyPassword(loginRequest.getPassword(), user.get().getPassword())) {
            return ResponseEntity.ok(removePassword(user.get()));
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

    private Users removePassword(Users user) {
        user.setPassword(null);
        return user;
    }

    private boolean isEmailRegistered(String email) {
        Optional<Users> optionalUsers = userService.findByEmail(email);
        return optionalUsers.isPresent();
    }

    private boolean isEmailRegisteredForAnotherUser(String email, Long userId) {
        Optional<Users> existingUser = userService.findByEmail(email);
        return existingUser.isPresent() && !existingUser.get().getId().equals(userId);
    }
}
