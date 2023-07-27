package com.example.assignment.service.impl;

import com.example.assignment.service.IPriceService;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements IPriceService {
    final int initNum = 100;
    int[] priceList = new int[72];

    @Override
    public Integer getPrice(Long time) {
        int num = (int) (time % 72);
        return priceList[num];
    }

    @Override
    public void setPrice() {
        priceList[0] = initNum;
        for (int i = 1; i < 72; i++) {
            if (i < 36) {
                priceList[i] = priceList[i - 1] + 10;
            } else {
                priceList[i] = priceList[i - 1] - 10;
            }
        }
    }
}
