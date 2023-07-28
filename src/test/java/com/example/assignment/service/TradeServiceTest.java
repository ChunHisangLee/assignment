package com.example.assignment.service;

import com.example.assignment.entity.*;
import com.example.assignment.mapper.AccountMapper;
import com.example.assignment.mapper.CoinMapper;
import com.example.assignment.mapper.UserMapper;
import com.example.assignment.service.ex.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeServiceTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private CoinMapper coinMapper;
    @Autowired
    private IPriceService priceService;

    @Test
    public void createTrade() {
        Double quantity = 1.25;
        String direction = String.valueOf(TradeDirection.BUY);
        String userName = "JackIsGood7";
        String coinName = "BTC";
        List<Trade> list = new ArrayList<>();
        User userQuery = userMapper.findByUserName(userName);
        if (userQuery == null) {
            throw new UserNotFoundException("The user data doesn't exist!");
        }
        Coin coinQuery = coinMapper.findByName(coinName);
        if (coinQuery == null) {
            throw new UserNotFoundException("The coin data doesn't exist!");
        }
        Account accountQuery = accountMapper.findByKey(userQuery.getUserId(), coinQuery.getCoinId());
        if (accountQuery == null) {
            throw new UserNotFoundException("The user doesn't have a(n) " + coinName + " account!");
        }
        Trade trade = new Trade();
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
        synchronized (trade) {
            list.add(trade);
        }

        Trade tradeUSD = new Trade();
        Coin coinUSD = coinMapper.findByName("USD");
        if (coinUSD == null) {
            throw new UserNotFoundException("The coin data has existed!");
        }
        Account accountUSD = accountMapper.findByKey(userQuery.getUserId(), coinUSD.getCoinId());
        if (accountUSD == null) {
            throw new UserNotFoundException("The user doesn't have an USD account!");
        }
        tradeUSD.setUserId(userQuery.getUserId());
        tradeUSD.setCoinId(coinUSD.getCoinId());
        tradeUSD.setTransPrice(BigDecimal.valueOf(1));
        if (direction.equals(String.valueOf(TradeDirection.BUY))) {
            direction = String.valueOf(TradeDirection.SELL);
        } else {
            direction = String.valueOf(TradeDirection.BUY);
        }
        tradeUSD.setTransType(direction);
        tradeUSD.setTransQuantity(BigDecimal.valueOf(price * quantity));
        tradeUSD.setBeforeBalance(accountUSD.getNetValue());
        if (direction.equals(String.valueOf(TradeDirection.BUY))) {
            changeAmount = BigDecimal.valueOf(price * quantity);
        } else {
            changeAmount = BigDecimal.valueOf(-1 * price * quantity);
        }
        tradeUSD.setAfterBalance(tradeUSD.getBeforeBalance().add(changeAmount));
        tradeUSD.setTransTime(Date.from(instant));
        synchronized (tradeUSD) {
            list.add(tradeUSD);
        }
        System.out.println(list);
    }
}
