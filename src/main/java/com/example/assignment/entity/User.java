package com.example.assignment.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User extends BaseEntity implements Serializable {

    private Integer userId;
    private String name;
    private String userName;
    private String email;
    private String password;
    private String salt;
    private Integer isDeleted;
}
