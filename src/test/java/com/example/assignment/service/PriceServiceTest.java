package com.example.assignment.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PriceServiceTest {
    final int initNum = 100;
    final int maxNum = 460;

    @Test
    public void getPrice() {
        int num = (int) (338102605 % 72);
        int res;
        if (num <= 36) {
            res = initNum + num * 10;
        } else {
            res = maxNum - (num - 36) * 10;
        }
        System.out.println(res);
    }
}
