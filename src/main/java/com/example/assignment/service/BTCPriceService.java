package com.example.assignment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BTCPriceService {
    private static final Logger logger = LoggerFactory.getLogger(BTCPriceService.class);

    private static final int MIN_NUM = 100;
    private static final int MAX_NUM = 460;
    private double btcPrice = 100.0;
    private boolean increasing = true;

    public double getBtcPrice() {
        return btcPrice;
    }

    @Scheduled(fixedRate = 5 * 1000)
    public void updatePrice() {
        if (increasing) {
            btcPrice += 10;
            if (btcPrice >= MAX_NUM) {
                increasing = false;
            }
        } else {
            btcPrice -= 10;
            if (btcPrice <= MIN_NUM) {
                increasing = true;
            }
        }

        logger.info("Updated BTC Price: {}", btcPrice);
    }
}
