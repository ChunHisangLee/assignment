package com.example.assignment.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceServiceImplTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private PriceServiceImpl priceService;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        // Set initialPrice field explicitly for the test
        ReflectionTestUtils.setField(priceService, "initialPrice", 100);
    }

    @Test
    void testGetPriceWhenPriceIsInRedis() {
        int expectedPrice = 120;
        when(valueOperations.get(PriceServiceImpl.REDIS_KEY)).thenReturn(expectedPrice);

        int actualPrice = priceService.getPrice();

        assertEquals(expectedPrice, actualPrice);
        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOperations, times(1)).get(PriceServiceImpl.REDIS_KEY);
    }

    @Test
    void testGetPriceWhenPriceIsNotInRedis() {
        int expectedPrice = 100; // Assuming this is the value of initialPrice
        when(valueOperations.get(PriceServiceImpl.REDIS_KEY)).thenReturn(null);

        int actualPrice = priceService.getPrice();

        assertEquals(expectedPrice, actualPrice);
        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOperations, times(1)).get(PriceServiceImpl.REDIS_KEY);
    }

    @Test
    void testSetPrice() {
        int newPrice = 130;

        priceService.setPrice(newPrice);

        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOperations, times(1)).set(PriceServiceImpl.REDIS_KEY, newPrice);
    }
}
