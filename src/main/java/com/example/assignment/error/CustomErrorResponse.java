package com.example.assignment.error;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomErrorResponse {
    private String message;

    public CustomErrorResponse(String message) {
        this.message = message;
    }

}
