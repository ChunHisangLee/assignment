package com.example.assignment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AssignmentApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationContext context;

    @Test
    void getConnection() throws SQLException {
        System.out.println(dataSource.getConnection());
    }

    @Test
    void contextLoads() {
        assertNotNull(context);
    }

    @Test
    public void testMain() {
        AssignmentApplication.main(new String[]{});
    }
}

