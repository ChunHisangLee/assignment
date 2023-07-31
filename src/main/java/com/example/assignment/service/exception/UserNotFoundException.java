package com.example.assignment.service.exception;

public class UserNotFoundException extends ServiceException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
