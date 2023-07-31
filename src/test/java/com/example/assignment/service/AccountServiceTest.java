package com.example.assignment.service;

import com.example.assignment.entity.Coin;
import com.example.assignment.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceTest {
    @Autowired
    private IAccountService accountService;

    @Test
    public void createAccount() {
        User user = new User();
        user.setUserId("*******");
        Coin coin = new Coin();
        coin.setCoinId(1);
        coin.setCoinName("USD");
        accountService.createAccount(user, coin);
        System.out.println("OK!!");
    }

    @Test
    public void deleteAccount() {
        User user = new User();
        user.setUserId("***************");
        accountService.deleteAccount(user.getUserId());
        System.out.println("OK!!");
    }
}
