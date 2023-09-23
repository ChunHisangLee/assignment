package com.example.assignment.service.impl;

import com.example.assignment.entity.Coin;
import com.example.assignment.mapper.CoinMapper;
import com.example.assignment.service.ICoinService;
import com.example.assignment.service.exception.InsertException;
import org.springframework.stereotype.Service;

@Service
public class CoinServiceImpl implements ICoinService {
    private final CoinMapper coinMapper;

    public CoinServiceImpl(CoinMapper coinMapper) {
        this.coinMapper = coinMapper;
    }

    @Override
    public void createCoin(Coin coin) {
        Integer rows = coinMapper.insert(coin);
        if (rows != 1) {
            throw new InsertException("Error inserting coin data!");
        }
    }

    @Override
    public Coin getCoin(String coinName) {
        return coinMapper.getCoin(coinName);
    }

    @Override
    public Coin getCoin(Integer coinId) {
        return coinMapper.getCoin(coinId);
    }
}
