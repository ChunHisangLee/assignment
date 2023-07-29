package com.example.assignment.service.impl;

import com.example.assignment.service.IPriceService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PriceServiceImpl implements IPriceService {
    private final AtomicInteger price = new AtomicInteger(100);
    @Override
    public int getPrice() {
        Instant instant = Instant.now();
        int num = (int) ((instant.getEpochSecond() / 5) % 72);
        if (num <= 36) {
            price.addAndGet(10);

        } else {
            price.addAndGet(-10);
        }
        return price.get();
    }
}
