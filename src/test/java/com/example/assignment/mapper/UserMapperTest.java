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
        user.setName("Jack");
        user.setUserName("JackIsGood2");
        user.setEmail("jackisgood2@gmail.com");
        user.setPassword("Aa123456");
        Integer rows = userMapper.insert(user);
        System.out.println(rows);
    }

    @Test
    public void findByUserName() {
        User user = userMapper.findByUserName("JackIsGood2");
        System.out.println(user);
    }

    @Test
    public void deleteByUserName() {
        Integer rows = userMapper.deleteByUserName("JackIsGood4");
        System.out.println(rows);
    }
}
