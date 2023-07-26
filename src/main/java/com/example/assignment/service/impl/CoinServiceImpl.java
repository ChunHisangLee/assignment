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
        Coin result = coinMapper.findByName(name);
        if (result != null) {
            throw new UserNameDuplicatedException("該幣別資料已經存在!");
        }
        coin.setBaseAmount(coin.getBaseAmount());
        coin.setMinAmount(coin.getMinAmount());
        coin.setMaxAmount(coin.getMaxAmount());
        coin.setDayMaxAmount(coin.getDayMaxAmount());
        Integer rows = coinMapper.insert(coin);
        if (rows != 1) {
            throw new InsertException("在新增的過程中產生了未知的異常");
        }
    }
}
