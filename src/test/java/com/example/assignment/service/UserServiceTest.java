package com.example.assignment.service;

import com.example.assignment.entity.User;
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

    @Test
    public void register() {
        User userI = new User();
        userI.setUserId("***************");
        userI.setUserName("Jack02");
        userI.setPassword("Aa123456");
        userService.register(userI);
        System.out.println("OK!!");
    }

    @Test
    public void deleteUser() {
        User userD = new User();
        userD.setUserId("***************");
        userService.deleteUser(userD);
        System.out.println("OK!!");
    }

    @Test
    public void getUser() {
        User userG = new User();
        userG.setUserName("Jack01");
        userService.getUser(userG);
        System.out.println(userG);
        User user2 = new User();
        userG.setUserId("************");
        userService.getUser(user2);
        System.out.println(user2);
    }
}
