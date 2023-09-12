package com.example.assignment.controller;

import com.example.assignment.entity.User;
import com.example.assignment.service.UserRegistrationService;
import com.example.assignment.service.UserDeletionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserRegistrationService userRegistrationService;
    private final UserDeletionService userDeletionService;

    public UserController(UserRegistrationService userRegistrationService, UserDeletionService userDeletionService) {
        this.userRegistrationService = userRegistrationService;
        this.userDeletionService = userDeletionService;
    }

    @PostMapping("/register")
    public Integer register(@RequestBody User user) {
        userRegistrationService.registerUser(user);
        return 1;
    }

    @PostMapping("/delete")
    public Integer deleteUser(@RequestBody User user) {
        userDeletionService.deleteUser(user);
        return 1;
    }
}
