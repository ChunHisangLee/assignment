package com.example.assignment.service;

import java.util.Date;

public interface IPasswordService {
    String generateUUID();

    Date getCurrentTime();


    String hashPassword(String password, String salt);
}
