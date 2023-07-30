package com.example.assignment.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends BaseEntity implements Serializable {

    private String userId;
    private String name;
    private String userName;
    private String email;
    private String password;
    private String salt;
}
