package com.example.assignment.service;

import com.example.assignment.entity.User;
import com.example.assignment.mapper.UserMapper;
import com.example.assignment.service.ex.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    private IUserService userService;
    @Autowired
    private IAccountService accountService;

    @Test
    public void register() {
        try {
            User user = new User();
            user.setName("Jack");
            user.setUserName("JackIsGood6");
            user.setEmail("jackisgood6@gmail.com");
            user.setPassword("Aa123456");
            userService.register(user);
            System.out.println("OK!!");

            userService.register(user);
            accountService.createAccount(user, "USD");
            accountService.createAccount(user, "BTC");
            System.out.println("OK!!");

        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void deleteUser() {
        User user = new User();
        user.setUserName("JackIsGood3");
        userService.deleteUser(user);
        System.out.println("OK!!");
    }
    @Test
    public void getUser() {
        User user = new User();
        user.setUserName("JackIsGood3");
        userService.deleteUser(user);
        System.out.println("OK!!");
    }
}
