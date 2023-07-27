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
    void setPrice() {
        priceService.setPrice();
    }

    @Test
    void getPrice() {
        Instant instant = Instant.now();
        long seconds = instant.getEpochSecond() / 5;
        priceService.getPrice(seconds);
        priceService.getPrice(seconds + 5);
        priceService.getPrice(seconds + 10);
        priceService.getPrice(seconds + 15);
        priceService.getPrice(seconds + 20);
        priceService.getPrice(seconds + 25);
        priceService.getPrice(seconds + 30);
        priceService.getPrice(seconds + 35);
        priceService.getPrice(seconds + 40);
    }
}
