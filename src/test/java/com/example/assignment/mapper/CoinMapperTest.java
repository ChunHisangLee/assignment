package com.example.assignment.mapper;

import com.example.assignment.entity.Coin;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CoinMapperTest {
    @Autowired
    private CoinMapper coinMapper;

    @Test
    public void insert() {
        Coin coin = new Coin();
        coin.setName("BTC");
        coin.setBaseAmount(BigDecimal.valueOf(0.01));
        coin.setMinAmount(BigDecimal.valueOf(0.01));
        coin.setMaxAmount(BigDecimal.valueOf(1e3));
        coin.setDayMaxAmount(BigDecimal.valueOf(1e6));
        Integer rows = coinMapper.insert(coin);
        System.out.println(rows);
    }

    @Test
    public void findByName() {
        Coin coin = coinMapper.findByName("BTC");
        System.out.println(coin);
    }
}
