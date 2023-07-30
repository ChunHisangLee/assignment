package com.example.assignment.schedule;

import com.example.assignment.service.IPriceService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@Data
public class ScheduledTasks {
    final int initNum = 100;
    final int maxNum = 460;
    @Autowired
    private IPriceService priceService;

    @Scheduled(fixedRate = 1000 * 5)
    public void setCurrentPrice() {
        Instant instant = Instant.now();
        int index = (int) ((instant.getEpochSecond() / 5) % 72);
        int res;
        if (index <= 36) {
            res = initNum + index * 10;
        } else {
            res = maxNum - (index - 36) * 10;
        }
        priceService.setPrice(res);
    }
}
