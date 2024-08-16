package com.example.assignment.service.impl;

import com.example.assignment.service.PriceService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class PriceServiceImpl implements PriceService {

    @Value("${initial.price:100}")
    private int price;

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public void setPrice(int price) {
        this.price = price;
    }
}
