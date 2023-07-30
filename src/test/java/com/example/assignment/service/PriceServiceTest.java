package com.example.assignment.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PriceServiceTest {
    @Autowired
    private IPriceService priceService;

    @Test
    public void getPrice() {
        System.out.println(priceService.getPrice());
    }
}
