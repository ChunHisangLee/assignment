package com.example.assignment;

import com.example.assignment.service.IPriceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.Instant;

@SpringBootTest
class AssignmentApplicationTests {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private IPriceService priceService;

    @Test
    void contextLoads() {
    }

    @Test
    void getConnection() throws SQLException {
        System.out.println(dataSource.getConnection());
    }

    @Test
    void getPrice() {
        Instant instant = Instant.now();
        long seconds = instant.getEpochSecond() / 5;
        System.out.println(seconds);
        System.out.println(priceService.getPrice(seconds));
    }
}
