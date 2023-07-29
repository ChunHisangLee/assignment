package com.example.assignment.controller;

import com.example.assignment.entity.*;
import com.example.assignment.service.IAccountService;
import com.example.assignment.service.ICoinService;
import com.example.assignment.service.IPriceService;
import com.example.assignment.service.IUserService;
import com.example.assignment.service.ex.ServiceException;
import com.example.assignment.service.ex.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@RestController
@RequestMapping("trade")
public class TradeController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private ICoinService coinService;
    @Autowired
    private IPriceService priceService;

    @PostMapping("/save")
    public Integer createTrade(Trade trade) {
        User userQuery = userService.getUser(trade.getUserId());
        if (userQuery == null) {
            throw new UserNotFoundException("The user data doesn't exist!");
        }
        Coin coinQuery = coinService.getCoin(trade.getCoinId());
        if (coinQuery == null) {
            throw new ServiceException("The coin data doesn't exist!");
        }
        Account accountQuery = accountService.getAccount(trade.getUserId(),trade.getCoinId());
        if (accountQuery == null) {
            throw new ServiceException("The user doesn't have a(n) " + coinQuery.getCoinName() + " account!");
        }
        Instant instant = Instant.now();
        int price = priceService.getPrice();
        trade.setTransPrice(BigDecimal.valueOf(price));
        trade.setBeforeBalance(accountQuery.getNetValue());
        BigDecimal changeAmount;
        if (trade.getTransType().equals(String.valueOf(TradeDirection.BUY))) {
            changeAmount = trade.getTransQuantity();
        } else {
            changeAmount = trade.getTransQuantity().multiply(BigDecimal.valueOf(-1));
        }
        trade.setAfterBalance(trade.getBeforeBalance().add(changeAmount));
        trade.setTransTime(LocalDateTime.from(instant));
        return 1;
    }
}
