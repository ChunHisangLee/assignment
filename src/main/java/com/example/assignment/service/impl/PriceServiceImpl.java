package com.example.assignment.service.impl;

import com.example.assignment.service.IPriceService;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements IPriceService {
    final int initNum = 100;
    final int maxNum = 460;

    @Override
    public Integer getPrice(Long time) {
        int num = (int) (time % 72);
        int res;
        if (num <= 36) {
            res = initNum + num * 10;
        } else {
            res = maxNum - (num - 36) * 10;
        }
        return res;
    }
}
