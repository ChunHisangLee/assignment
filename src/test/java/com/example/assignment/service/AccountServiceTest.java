package com.example.assignment.service;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.Coin;
import com.example.assignment.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceTest {
    @Autowired
    private IAccountService accountService;

    @Test
    public void createAccount() {
        User user = new User();
        user.setUserId("***************");
        Coin coin = new Coin();
        coin.setCoinId(2);
        coin.setCoinName("BCT");
        accountService.createAccount(user, coin);
        System.out.println("OK!!");
    }

    @Test
    public void getAccounts() {
        User user = new User();
        user.setUserId("***************");
        List<Account> accountList = accountService.getAccounts(user.getUserId());
        for (Account account : accountList) {
            System.out.println(account);
        }
    }

    @Test
    public void deleteAccount() {
        User user = new User();
        user.setUserId("***************");
        accountService.deleteAccount(user.getUserId());
        System.out.println("OK!!");
    }
}
