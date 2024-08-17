package com.example.assignment.schedule;

import com.example.assignment.entity.BTCPriceHistory;
import com.example.assignment.repository.BTCPriceHistoryRepository;
import com.example.assignment.service.PriceService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class ScheduledTasks {
    private static final int MIN_PRICE = 100;
    private static final int MAX_PRICE = 460;
    private static final int PRICE_INCREMENT = 10;
    private static final int SCHEDULE_RATE_MS = 5 * 1000;

    @Getter
    private boolean isIncreasing = true;

    @Getter
    private int currentPrice = MIN_PRICE;

    private final PriceService priceService;
    private final BTCPriceHistoryRepository btcPriceHistoryRepository;

    public ScheduledTasks(PriceService priceService, BTCPriceHistoryRepository btcPriceHistoryRepository) {
        this.priceService = priceService;
        this.btcPriceHistoryRepository = btcPriceHistoryRepository;
    }

    @Scheduled(fixedRate = SCHEDULE_RATE_MS)
    public void updateCurrentPrice() {
        if (isIncreasing) {
            currentPrice += PRICE_INCREMENT;
            if (currentPrice >= MAX_PRICE) {
                isIncreasing = false;
            }
        } else {
            currentPrice -= PRICE_INCREMENT;
            if (currentPrice <= MIN_PRICE) {
                isIncreasing = true;
            }
        }

        log.info("Updated BTC Price: {}", currentPrice);
        priceService.setPrice(currentPrice);

        BTCPriceHistory priceHistory = new BTCPriceHistory();
        priceHistory.setPrice(currentPrice);
        priceHistory.setTimestamp(LocalDateTime.now());
        btcPriceHistoryRepository.save(priceHistory);
    }
}
