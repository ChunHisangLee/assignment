package com.example.assignment.mapper;

import com.example.assignment.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void insert() {
        User user = new User();
        user.setUserId("******");
        user.setName("Jack");
        user.setUserName("Jack01");
        user.setEmail("jack01@gmail.com");
        user.setPassword("Aa123456");
        user.setSalt("**********");
        Integer rows = userMapper.insert(user);
        System.out.println(rows);
    }

    @Test
    public void getUser() {
        User userQuery = new User();
        userQuery.setUserName("Jack01");
        User user = userMapper.getUser(userQuery);
        System.out.println(user);
        userQuery.setUserId("******");
        User user2 = userMapper.getUser(userQuery.getUserId());
        System.out.println(user2);
    }

    @Test
    public void deleteUser() {
        User user = new User();
        user.setUserId("******");
        Integer rows = userMapper.deleteUser(user);
        System.out.println(rows);
    }
}
