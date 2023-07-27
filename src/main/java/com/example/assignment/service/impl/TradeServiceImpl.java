package com.example.assignment.service.impl;

import com.example.assignment.entity.*;
import com.example.assignment.mapper.AccountMapper;
import com.example.assignment.mapper.CoinMapper;
import com.example.assignment.mapper.UserMapper;
import com.example.assignment.service.IPriceService;
import com.example.assignment.service.ITradeService;
import com.example.assignment.service.ex.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TradeServiceImpl implements ITradeService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private CoinMapper coinMapper;
    @Autowired
    private IPriceService priceService;

    @Override
    public List<Trade> createTrade(Double quantity, String direction, String userName, String coinName) {
        List<Trade> list = new ArrayList<>();
        Trade trade = new Trade();
        User userQuery = userMapper.findByUserName(userName);
        if (userQuery == null) {
            throw new UserNotFoundException("無該筆資料!");
        }
        Coin coinQuery = coinMapper.findByName(coinName);
        if (coinQuery == null) {
            throw new UserNotFoundException("無該幣別資料!");
        }
        Account accountQuery = accountMapper.findByKey(userQuery.getUserId(), coinQuery.getCoinId());
        if (accountQuery == null) {
            throw new UserNotFoundException("該用戶無 " + coinName + " 帳戶資料!");
        }
        Instant instant = Instant.now();
        long seconds = instant.getEpochSecond() / 5;
        Integer price = priceService.getPrice(seconds);
        trade.setUserId(userQuery.getUserId());
        trade.setCoinId(coinQuery.getCoinId());
        trade.setTransPrice(BigDecimal.valueOf(price));
        trade.setTransType(direction);
        trade.setTransQuantity(BigDecimal.valueOf(quantity));
        trade.setBeforeBalance(accountQuery.getNetValue());
        BigDecimal changeAmount;
        if (direction.equals(String.valueOf(TradeDirection.BUY))) {
            changeAmount = BigDecimal.valueOf(quantity);
        } else {
            changeAmount = BigDecimal.valueOf(-1 * quantity);
        }
        trade.setAfterBalance(trade.getBeforeBalance().add(changeAmount));
        trade.setTransTime(Date.from(instant));
        list.add(trade);

        Coin coinUSD = coinMapper.findByName("USD");
        if (coinUSD == null) {
            throw new UserNotFoundException("無該幣別資料!");
        }
        Account accountUSD = accountMapper.findByKey(userQuery.getUserId(), coinUSD.getCoinId());
        if (accountUSD == null) {
            throw new UserNotFoundException("該用戶無 USD 帳戶資料!");
        }
        trade.setUserId(userQuery.getUserId());
        trade.setCoinId(coinUSD.getCoinId());
        trade.setTransPrice(BigDecimal.valueOf(1));
        if (direction.equals(String.valueOf(TradeDirection.BUY))) {
            direction = String.valueOf(TradeDirection.SELL);
        } else {
            direction = String.valueOf(TradeDirection.BUY);
        }
        trade.setTransType(direction);
        trade.setTransQuantity(BigDecimal.valueOf(price * quantity));
        trade.setBeforeBalance(accountQuery.getNetValue());
        if (direction.equals(String.valueOf(TradeDirection.BUY))) {
            changeAmount = BigDecimal.valueOf(price * quantity);
        } else {
            changeAmount = BigDecimal.valueOf(-1 * price * quantity);
        }
        trade.setAfterBalance(trade.getBeforeBalance().add(changeAmount));
        trade.setTransTime(Date.from(instant));
        list.add(trade);
        return list;
    }
}
