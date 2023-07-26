package com.example.assignment.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class BaseEntity implements Serializable {
    private Integer isDeleted;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
}
