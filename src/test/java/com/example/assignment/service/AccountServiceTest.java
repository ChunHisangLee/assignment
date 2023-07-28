package com.example.assignment.service;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.User;
import com.example.assignment.mapper.AccountMapper;
import com.example.assignment.mapper.CoinMapper;
import com.example.assignment.service.ex.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceTest {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private CoinMapper coinMapper;

    @Test
    public void createAccount() {
        try {
            User user = new User();
            user.setUserId(4);
            user.setName("Jack");
            user.setUserName("JackIsGood7");
            accountService.createAccount(user,"BTC");
            System.out.println("OK!!");
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }
}
