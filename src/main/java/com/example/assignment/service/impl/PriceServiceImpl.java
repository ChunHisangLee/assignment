package com.example.assignment.service.impl;

import com.example.assignment.service.PriceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements PriceService {

    private static final String REDIS_KEY = "BTC_CURRENT_PRICE";

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${initial.price:100}")
    private int initialPrice;

    public PriceServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public int getPrice() {
        Integer price = (Integer) redisTemplate.opsForValue().get(REDIS_KEY);
        return price != null ? price : initialPrice;
    }

    @Override
    public void setPrice(int price) {
        redisTemplate.opsForValue().set(REDIS_KEY, price);
    }
}
