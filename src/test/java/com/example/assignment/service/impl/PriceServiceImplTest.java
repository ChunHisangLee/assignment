package com.example.assignment.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.example.assignment.schedule.ScheduledTasks;
import java.math.BigDecimal;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PriceServiceImplTest {

  @Mock private RedisTemplate<String, Object> redisTemplate;

  @Mock private ValueOperations<String, Object> valueOperations;

  @InjectMocks private PriceServiceImpl priceService;

  @BeforeEach
  void setUp() {
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    // Set initialPrice field explicitly for the test as a BigDecimal
    ReflectionTestUtils.setField(priceService, "initialPrice", BigDecimal.valueOf(100));
  }

  @Test
  void testGetPriceWhenPriceIsInRedis() {
    double expectedPrice = 120;
    when(valueOperations.get(PriceServiceImpl.REDIS_KEY)).thenReturn(expectedPrice);

    BigDecimal actualPrice = priceService.getPrice();

    assertEquals(BigDecimal.valueOf(expectedPrice), actualPrice);
    verify(redisTemplate, times(1)).opsForValue();
    verify(valueOperations, times(1)).get(PriceServiceImpl.REDIS_KEY);
  }

  @Test
  void testGetPriceWhenPriceIsNotInRedis() {
    int expectedPrice = 100; // initialPrice value
    when(valueOperations.get(PriceServiceImpl.REDIS_KEY)).thenReturn(null);

    BigDecimal actualPrice = priceService.getPrice();

    assertEquals(BigDecimal.valueOf(expectedPrice), actualPrice);
    verify(redisTemplate, times(1)).opsForValue();
    verify(valueOperations, times(1)).get(PriceServiceImpl.REDIS_KEY);
  }

  @Test
  void testSetPrice() {
    int newPrice = 130;
    Duration ttl = Duration.ofMillis(ScheduledTasks.SCHEDULE_RATE_MS);

    priceService.setPrice(BigDecimal.valueOf(newPrice));

    verify(redisTemplate, times(1)).opsForValue();
    verify(valueOperations, times(1))
        .set(PriceServiceImpl.REDIS_KEY, BigDecimal.valueOf(newPrice), ttl);
  }
}
