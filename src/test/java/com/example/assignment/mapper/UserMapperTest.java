package com.example.assignment.mapper;

import com.example.assignment.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private static final String TEST_USER_ID = "******";
    private static final String TEST_USER_NAME = "Jack01";

    @Test
    public void insert() {
        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setName("Jack");
        user.setUserName(TEST_USER_NAME);
        user.setEmail("jack01@gmail.com");
        user.setPassword("Aa123456");
        user.setSalt("**********");

        Integer rows = userMapper.insert(user);

        assertNotNull(rows);
        assertTrue(rows > 0);
    }

    @Test
    public void getUser() {
        User userQuery = new User();
        userQuery.setUserName(TEST_USER_NAME);

        User user = userMapper.getUser(userQuery);

        assertNotNull(user);
        assertEquals(TEST_USER_NAME, user.getUserName());

        userQuery.setUserId(TEST_USER_ID);

        User user2 = userMapper.getUser(userQuery.getUserId());

        assertNotNull(user2);
        assertEquals(TEST_USER_ID, user2.getUserId());
    }

    @Test
    public void deleteUser() {
        User user = new User();
        user.setUserId(TEST_USER_ID);

        Integer rows = userMapper.deleteUser(user);

        assertNotNull(rows);
        assertTrue(rows > 0);
    }
}
