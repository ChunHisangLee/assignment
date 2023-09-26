package com.example.assignment.service.impl;

import com.example.assignment.service.IPasswordService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class PasswordServiceImpl implements IPasswordService {
    @Override
    public String generateUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    @Override
    public Date getCurrentTime() {
        return Date.from(Instant.now());
    }

    @Override
    public String hashPassword(String password, String salt) {
        for (int i = 0; i < 3; i++) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        return password;
    }
}
