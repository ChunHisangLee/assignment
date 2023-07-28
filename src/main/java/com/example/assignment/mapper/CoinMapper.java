package com.example.assignment.mapper;

import com.example.assignment.entity.Coin;


public interface CoinMapper {
    /**
     * Insert coin data
     *
     * @param coin coin data
     * @return the row to be inserted
     */
    Integer insert(Coin coin);
    /**
     * Query coin data by coin name
     *
     * @param name coin name
     * @return if not find, return null value
     */
    Coin findByName(String name);
}
