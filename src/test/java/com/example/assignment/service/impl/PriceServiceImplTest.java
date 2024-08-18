package com.example.assignment.service.impl;

import com.example.assignment.service.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceServiceImplTest {

    private PriceService priceService;

    @BeforeEach
    void setUp() {
        priceService = new PriceServiceImpl();
        // Setting the initial price using ReflectionTestUtils since @Value is not processed in unit tests
        ReflectionTestUtils.setField(priceService, "price", 100);
    }

    @Test
    void testGetPrice() {
        int expectedPrice = 100;
        int actualPrice = priceService.getPrice();
        assertEquals(expectedPrice, actualPrice, "The price should be 100");
    }

    @Test
    void testSetPrice() {
        int newPrice = 200;
        priceService.setPrice(newPrice);
        assertEquals(newPrice, priceService.getPrice(), "The price should be 200 after setting it");
    }
}
