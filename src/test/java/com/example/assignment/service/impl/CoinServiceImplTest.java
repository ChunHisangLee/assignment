package com.example.assignment.service.impl;

import com.example.assignment.entity.Coin;
import com.example.assignment.mapper.CoinMapper;
import com.example.assignment.service.exception.InsertException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CoinServiceImplTest {

    @InjectMocks
    private CoinServiceImpl coinService;

    @Mock
    private CoinMapper coinMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCoin() {
        Coin coin = new Coin();
        coin.setCoinName("BTC");

        when(coinMapper.insert(coin)).thenReturn(1);

        coinService.createCoin(coin);

        verify(coinMapper, times(1)).insert(coin);
    }

    @Test
    public void testCreateCoinInsertException() {
        Coin coin = new Coin();
        coin.setCoinName("BTC");

        when(coinMapper.insert(coin)).thenReturn(0);

        assertThrows(InsertException.class, () -> coinService.createCoin(coin));
    }

    @Test
    public void testGetCoinByName() {
        Coin expectedCoin = new Coin();
        expectedCoin.setCoinName("BTC");

        when(coinMapper.getCoin("BTC")).thenReturn(expectedCoin);

        Coin result = coinService.getCoin("BTC");

        assertEquals(expectedCoin, result);
    }

    @Test
    public void testGetCoinById() {
        Coin expectedCoin = new Coin();
        expectedCoin.setCoinId(1);

        when(coinMapper.getCoin(1)).thenReturn(expectedCoin);

        Coin result = coinService.getCoin(1);

        assertEquals(expectedCoin, result);
    }
}
