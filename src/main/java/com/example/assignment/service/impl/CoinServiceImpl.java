package com.example.assignment.service.impl;

import com.example.assignment.entity.Coin;
import com.example.assignment.mapper.CoinMapper;
import com.example.assignment.service.ICoinService;
import com.example.assignment.service.ex.InsertException;
import com.example.assignment.service.ex.UserNameDuplicatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoinServiceImpl implements ICoinService {
    @Autowired
    private CoinMapper coinMapper;

    @Override
    public void createCoin(Coin coin) {
        String name = coin.getName();
        Coin coinQuery = coinMapper.findByName(name);
        if (coinQuery != null) {
            throw new UserNameDuplicatedException("The coin data has exist!");
        }
        Integer rows = coinMapper.insert(coin);
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
        }
    }
}
