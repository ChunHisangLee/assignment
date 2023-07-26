package com.example.assignment.service;

import com.example.assignment.entity.Coin;
import com.example.assignment.entity.User;
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
        try {
            Coin coin = new Coin();
            coin.setName("BTC");
            coin.setBaseAmount(BigDecimal.valueOf(0.01));
            coin.setMinAmount(BigDecimal.valueOf(0.01));
            coin.setMaxAmount(BigDecimal.valueOf(1e3));
            coin.setDayMaxAmount(BigDecimal.valueOf(1e6));
            coinService.createCoin(coin);
            System.out.println("OK!!");
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }
}
