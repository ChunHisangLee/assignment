package com.example.assignment.mapper;

import com.example.assignment.entity.Coin;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CoinMapperTest {
    @Autowired
    private CoinMapper coinMapper;

    @Test
    public void insert() {
        Coin coin = new Coin();
        coin.setCoinName("BTC");
        Integer rows = coinMapper.insert(coin);
        System.out.println(rows);
    }

    @Test
    public void getCoin() {
        Coin coin = coinMapper.getCoin("BTC");
        System.out.println(coin);
    }
}
