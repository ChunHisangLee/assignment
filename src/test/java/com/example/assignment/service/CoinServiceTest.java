package com.example.assignment.service;

import com.example.assignment.entity.Coin;
import com.example.assignment.service.ex.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CoinServiceTest {
    @Autowired
    private ICoinService coinService;

    @Test
    public void createCoin() {
        Coin coin = new Coin();
        coin.setCoinName("USD");
        coinService.createCoin(coin);
        System.out.println("OK!!");
    }
}
