package com.example.assignment.schedule;

import com.example.assignment.service.IPriceService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTasks {
    private static final int INIT_NUM = 100;
    private static final int MAX_NUM = 460;
    @Getter
    private boolean isIncreased = true;
    @Getter
    private int price = INIT_NUM - 10;

    private final IPriceService priceService;

    public ScheduledTasks(IPriceService priceService) {
        this.priceService = priceService;
    }

    @Scheduled(fixedRate = 1000 * 5)
    public void setCurrentPrice() {
        if (isIncreased) {
            price += 10;
        } else {
            price -= 10;
        }
        if (price == MAX_NUM && isIncreased) {
            isIncreased = false;
        } else if (price == INIT_NUM && !isIncreased) {
            isIncreased = true;
        }
        log.info("Current price: {}", price);
        priceService.setPrice(price);
    }
}