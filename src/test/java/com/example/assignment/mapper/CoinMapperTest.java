package com.example.assignment.mapper;

import com.example.assignment.entity.Coin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CoinMapperTest {

    @Autowired
    private CoinMapper coinMapper;

    private static final String TEST_COIN_NAME = "BTC";

    @Test
    public void insert() {
        Coin coin = new Coin();
        coin.setCoinName(TEST_COIN_NAME);

        Integer rows = coinMapper.insert(coin);

        assertNotNull(rows);
        assertTrue(rows > 0);
    }

    @Test
    public void getCoin() {
        Coin coin = coinMapper.getCoin(TEST_COIN_NAME);

        assertNotNull(coin);
        assertEquals(TEST_COIN_NAME, coin.getCoinName());
    }
}
