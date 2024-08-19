package com.example.assignment.service.impl;

import com.example.assignment.service.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = "initial.price=100")
class PriceServiceImplTest {

    @MockBean
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PriceService priceService;

    @Test
    void getPrice_PriceNotInRedis() {
        when(redisTemplate.opsForValue().get("BTC_CURRENT_PRICE")).thenReturn(null);
        int price = priceService.getPrice();
        assertEquals(100, price, "The initial price should be 100 if not found in Redis.");
    }

    @Test
    void getPrice_PriceInRedis() {
        when(redisTemplate.opsForValue().get("BTC_CURRENT_PRICE")).thenReturn(200);
        int price = priceService.getPrice();
        assertEquals(200, price, "The price from Redis should be 200.");
    }

    @Test
    void setPrice() {
        priceService.setPrice(300);
        when(redisTemplate.opsForValue().get("BTC_CURRENT_PRICE")).thenReturn(300);
        int price = priceService.getPrice();
        assertEquals(300, price, "The price in Redis should be 300.");
    }
}
