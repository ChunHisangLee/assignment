package com.example.assignment.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceServiceImplTest {

    private PriceServiceImpl priceService;

    @BeforeEach
    public void setUp() {
        priceService = new PriceServiceImpl();
    }

    @Test
    public void testGetPrice() {
        priceService.setPrice(100);
        int result = priceService.getPrice();
        assertEquals(100, result);
    }

    @Test
    public void testSetPrice() {
        priceService.setPrice(200);
        int result = priceService.getPrice();
        assertEquals(200, result);
    }
}
