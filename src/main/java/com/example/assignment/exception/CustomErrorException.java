package com.example.assignment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomErrorException extends RuntimeException {

    private final int status;
    private final String error;
    private final String message;
    private final String details;

    public CustomErrorException(int status, String error, String message, String details) {
        super(message);
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }

}
