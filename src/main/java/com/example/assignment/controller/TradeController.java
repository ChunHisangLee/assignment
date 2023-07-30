package com.example.assignment.controller;

import com.example.assignment.entity.*;
import com.example.assignment.service.*;
import com.example.assignment.service.exception.ServiceException;
import com.example.assignment.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

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
    @Autowired
    private IHistoryService historyService;

    @PostMapping("/save")
    public Integer createTrade(@RequestBody History history) {
        User userQuery = userService.getUser(history.getUserId());
        if (userQuery == null) {
            throw new UserNotFoundException("The user data doesn't exist!");
        }
        Coin coinQuery = coinService.getCoin(history.getCoinId());
        if (coinQuery == null) {
            throw new ServiceException("The coin data doesn't exist!");
        }
        Coin coinUSD = coinService.getCoin("USD");
        if (coinUSD == null) {
            throw new ServiceException("The coin data doesn't exist!");
        }
        Account accountQuery = accountService.getAccount(history.getUserId(), history.getCoinId());
        if (accountQuery == null) {
            throw new ServiceException("The user doesn't have a(n) " + coinQuery.getCoinName() + " account!");
        }
        Account accountUSD = accountService.getAccount(history.getUserId(), coinUSD.getCoinId());
        if (accountUSD == null) {
            throw new UserNotFoundException("The user doesn't have an USD account!");
        }
        Instant instant = Instant.now();
        int price = priceService.getPrice();
        history.setTransPrice(BigDecimal.valueOf(price));
        BigDecimal changeAmount;
        if (history.getTransType().equals(String.valueOf(TradeDirection.BUY))) {
            changeAmount = history.getTransQuantity();
        } else {
            changeAmount = history.getTransQuantity().multiply(BigDecimal.valueOf(-1));
        }
        history.setBeforeBalance(accountQuery.getNetValue());
        history.setAfterBalance(history.getBeforeBalance().add(changeAmount));
        if (history.getTransType().equals(String.valueOf(TradeDirection.BUY))) {
            changeAmount = history.getTransQuantity().multiply(BigDecimal.valueOf(-1 * price));
        } else {
            changeAmount = history.getTransQuantity().multiply(BigDecimal.valueOf(price));
        }
        history.setBeforeBalanceUSD(accountUSD.getNetValue());
        history.setAfterBalanceUSD(history.getBeforeBalanceUSD().add(changeAmount));
        history.setTransTime(Date.from(instant));
        historyService.createHistory(history);
        return 1;
    }
}
