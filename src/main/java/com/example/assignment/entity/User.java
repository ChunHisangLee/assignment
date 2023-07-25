package com.example.assignment.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private Integer userId;
    private String name;
    private String userName;
    private String email;
    private String password;

}
