package com.example.assignment.service.exception;

public class UserNameDuplicatedException extends ServiceException {
    public UserNameDuplicatedException(String message) {
        super(message);
    }
}
