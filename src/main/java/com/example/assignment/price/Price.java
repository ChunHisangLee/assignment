package com.example.assignment.price;

import lombok.Data;

import java.time.Instant;

@Data
public class Price {
    final int initNum = 100;
    int[] priceList = new int[72];

    public Price() {
        priceList[0] = initNum;
        for (int i = 1; i < 72; i++) {
            if (i < 36) {
                priceList[i] = priceList[i - 1] + 10;
            } else {
                priceList[i] = priceList[i - 1] - 10;
            }
        }
    }

    public int getPrice(int time) {
        return priceList[time % 72];
    }
}
