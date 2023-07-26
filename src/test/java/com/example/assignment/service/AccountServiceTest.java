package com.example.assignment.service;

import com.example.assignment.entity.User;
import com.example.assignment.service.ex.ServiceException;
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
    @Autowired
    private IUserService userService;

    @Test
    public void reg() {
        try {
            User user = new User();
            user.setName("Jack");
            user.setUserName("JackIsGood7");
            user.setEmail("jackisgood7@gmail.com");
            user.setPassword("Aa123456");
            userService.reg(user);
            accountService.reg(user,1);
            System.out.println("OK!!");
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }
}
