package com.example.assignment.controller;

import com.example.assignment.entity.Users;
import com.example.assignment.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public ResponseEntity<Users> createUser(@RequestBody Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users createdUsers = userService.createUser(users);
        return ResponseEntity.ok(removePassword(createdUsers));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
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

    private Users removePassword(Users user) {
        user.setPassword(null);
        return user;
    }
}
