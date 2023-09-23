package com.example.assignment.service.impl;

import com.example.assignment.service.IPriceService;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements IPriceService {
    int price;

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public void setPrice(int num) {
        this.price = num;
    }
}
