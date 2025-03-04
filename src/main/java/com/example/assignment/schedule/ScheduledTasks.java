package com.example.assignment.schedule;

import com.example.assignment.entity.BTCPriceHistory;
import com.example.assignment.repository.BTCPriceHistoryRepository;
import com.example.assignment.service.PriceService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTasks {

  public static final int SCHEDULE_RATE_MS = 5 * 1000;
  public static final BigDecimal MIN_PRICE = BigDecimal.valueOf(100);
  public static final BigDecimal MAX_PRICE = BigDecimal.valueOf(460);
  public static final BigDecimal PRICE_INCREMENT = BigDecimal.valueOf(10);
  private final PriceService priceService;
  private final BTCPriceHistoryRepository btcPriceHistoryRepository;

  @Getter private boolean isIncreasing = true;
  @Getter private BigDecimal currentPrice = MIN_PRICE;

  public ScheduledTasks(
      PriceService priceService, BTCPriceHistoryRepository btcPriceHistoryRepository) {
    this.priceService = priceService;
    this.btcPriceHistoryRepository = btcPriceHistoryRepository;
  }

  @PostConstruct
  private void init() {
    updatePriceInStore();
    savePriceHistory();
    log.info("Initialized BTC Price in Redis and database: {}", currentPrice);
  }

  @Scheduled(fixedRate = SCHEDULE_RATE_MS)
  @Transactional
  public void updateCurrentPrice() {
    adjustCurrentPrice();
    log.info("Updated BTC Price: {}", currentPrice);

    updatePriceInStore();
    savePriceHistory();
    log.info("Saved updated BTC Price to Redis and database: {}", currentPrice);
  }

  private void adjustCurrentPrice() {
    if (isIncreasing) {
      currentPrice = currentPrice.add(PRICE_INCREMENT);
      if (currentPrice.compareTo(MAX_PRICE) >= 0) {
        isIncreasing = false;
      }
    } else {
      currentPrice = currentPrice.subtract(PRICE_INCREMENT);
      if (currentPrice.compareTo(MIN_PRICE) <= 0) {
        isIncreasing = true;
      }
    }
  }

  private void updatePriceInStore() {
    priceService.setPrice(currentPrice);
  }

  private void savePriceHistory() {
    BTCPriceHistory priceHistory = new BTCPriceHistory();
    priceHistory.setPrice(currentPrice);
    priceHistory.setTimestamp(LocalDateTime.now());
    btcPriceHistoryRepository.save(priceHistory);
  }
}
