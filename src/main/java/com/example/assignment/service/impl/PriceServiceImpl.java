package com.example.assignment.service.impl;

import com.example.assignment.schedule.ScheduledTasks;
import com.example.assignment.service.PriceService;
import java.math.BigDecimal;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PriceServiceImpl implements PriceService {
  public static final String REDIS_KEY = "BTC_CURRENT_PRICE";
  private final RedisTemplate<String, Object> redisTemplate;

  @Value("${initial.price:100}")
  private BigDecimal initialPrice;

  public PriceServiceImpl(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public BigDecimal getPrice() {
    log.info("Fetching current BTC price from Redis with key: {}", REDIS_KEY);
    Object priceObject = redisTemplate.opsForValue().get(REDIS_KEY);
    BigDecimal price = null;

    if (priceObject != null) {
      switch (priceObject) {
        case BigDecimal bigDecimal -> price = bigDecimal;
        case Number number -> price = BigDecimal.valueOf(number.doubleValue());
        case String string -> {
          try {
            price = new BigDecimal(string);
          } catch (NumberFormatException e) {
            log.error("Failed to parse price from Redis: {}", priceObject, e);
          }
        }
        default ->
            log.error("Unexpected type for price from Redis: {}", priceObject.getClass().getName());
      }

      log.info("Current BTC price retrieved from Redis: {}", price);
    } else {
      log.warn("BTC price not found in Redis. Falling back to initial price: {}", initialPrice);
    }

    return price != null ? price : initialPrice;
  }

  @Override
  public void setPrice(BigDecimal price) {
    log.info("Setting current BTC price in Redis with key: {} to value: {}", REDIS_KEY, price);
    redisTemplate
        .opsForValue()
        .set(REDIS_KEY, price, Duration.ofMillis(ScheduledTasks.SCHEDULE_RATE_MS));
    log.info("BTC price set successfully in Redis.");
  }
}
