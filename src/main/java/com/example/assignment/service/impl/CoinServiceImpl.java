package com.example.assignment.service.impl;

import com.example.assignment.entity.Coin;
import com.example.assignment.mapper.CoinMapper;
import com.example.assignment.service.ICoinService;
import com.example.assignment.service.ex.InsertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoinServiceImpl implements ICoinService {
    @Autowired
    private CoinMapper coinMapper;

    @Override
    public void createCoin(Coin coin) {
        Integer rows = coinMapper.insert(coin);
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
        }
    }

    @Override
    public Coin getCoin(Coin coin) {
        return coinMapper.getCoin(coin);
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
