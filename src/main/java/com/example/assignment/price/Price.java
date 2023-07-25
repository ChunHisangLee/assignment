package com.example.assignment.price;

import lombok.Data;

import java.time.Instant;
@Data
public class Price {
    final int initNum = 100;
    int price = initNum;
    int[] priceList = new int[72];

    public Price() {
        for (int i = 0; i < 72; i++) {
            if (i < 36) {
                priceList[i] = price;
                price += 10;
            } else {
                priceList[i] = price;
                price -= 10;
            }
        }
    }

    public int getPrice(int time) {
        return priceList[time % 72];
    }

    private void setPrice() {

        for (int i = 0; i < 72; i++) {
            if (i < 36) {
                priceList[i] = price;
                price += 10;
            } else {
                priceList[i] = price;
                price -= 10;
            }
        }
    }
}
