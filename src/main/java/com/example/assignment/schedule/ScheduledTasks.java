package com.example.assignment.schedule;

import com.example.assignment.service.IPriceService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
public class ScheduledTasks {
    @Autowired
    private IPriceService priceService;
    @Scheduled(fixedRate = 1000 * 5)
    public void getCurrentPrice() {
        priceService.getPrice();
    }
}
