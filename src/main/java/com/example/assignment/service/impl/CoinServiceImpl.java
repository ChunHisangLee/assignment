package com.example.assignment.service.impl;

import com.example.assignment.entity.Wallet;
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
    public void createCoin(Wallet coin) {
        Integer rows = coinMapper.insert(coin);
        if (rows != 1) {
            throw new InsertException("Error inserting coin data!");
        }
    }

    @Override
    public Wallet getCoin(String coinName) {
        return coinMapper.getCoin(coinName);
    }

    @Override
    public Wallet getCoin(Integer coinId) {
        return coinMapper.getCoin(coinId);
    }
}
