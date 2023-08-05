package com.example.assignment.schedule;

import com.example.assignment.service.IPriceService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
public class ScheduledTasks {
    final int initNum = 100;
    final int maxNum = 460;
    boolean isIncreased = true;
    int price = initNum - 10;

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
        if (price == maxNum && isIncreased) {
            isIncreased = false;
        } else if (price == initNum && !isIncreased) {
            isIncreased = true;
        }
        System.out.println(price);
        priceService.setPrice(price);
    }
}