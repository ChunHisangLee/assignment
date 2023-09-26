package com.example.assignment.controller;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.Coin;
import com.example.assignment.entity.History;
import com.example.assignment.entity.TradeDirection;
import com.example.assignment.service.*;
import com.example.assignment.service.exception.ServiceException;
import com.example.assignment.service.exception.UserNotFoundException;
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

    private final IUserService userService;
    private final IAccountService accountService;
    private final ICoinService coinService;
    private final IPriceService priceService;
    private final IHistoryService historyService;

    public TradeController(IUserService userService, IAccountService accountService, ICoinService coinService, IPriceService priceService, IHistoryService historyService) {
        this.userService = userService;
        this.accountService = accountService;
        this.coinService = coinService;
        this.priceService = priceService;
        this.historyService = historyService;
    }

    private void validateUser(String userId) {
        if (userService.getUser(userId) == null) {
            throw new UserNotFoundException("The user data doesn't exist!");
        }
    }

    private Coin validateCoin(Integer coinId) {
        Coin coin = coinService.getCoin(coinId);
        if (coin == null) {
            throw new ServiceException("The coin data doesn't exist!");
        }
        return coin;
    }

    private void validateAccount(String userId, Integer coinId, Coin coin) {
        if (accountService.getAccount(userId, coinId) == null) {
            throw new ServiceException("The user doesn't have a(n) " + coin.getCoinName() + " account!");
        }
    }

    private void updateHistory(History history) {
        int currentPrice = priceService.getPrice();
        history.setTransPrice(BigDecimal.valueOf(currentPrice));

        updateBalance(history);
        updateUSD(history);

        history.setTransTime(Date.from(Instant.now()));
    }

    private void updateBalance(History history) {
        Account accountQuery = accountService.getAccount(history.getUserId(), history.getCoinId());
        history.setBeforeBalance(accountQuery.getNetValue());
        BigDecimal changeAmount = getChangeAmount(history, history.getTransQuantity());
        history.setAfterBalance(history.getBeforeBalance().add(changeAmount));
    }

    private void updateUSD(History history) {
        Coin coinUSD = coinService.getCoin("USD");
        Account accountUSD = accountService.getAccount(history.getUserId(), coinUSD.getCoinId());
        history.setBeforeBalanceUSD(accountUSD.getNetValue());
        BigDecimal changeAmountUSD = getChangeAmountUSD(history, history.getTransQuantity());
        history.setAfterBalanceUSD(history.getBeforeBalanceUSD().add(changeAmountUSD));
    }

    private BigDecimal getChangeAmount(History history, BigDecimal quantity) {
        return history.getTransType().equals(TradeDirection.BUY.name())
                ? quantity
                : quantity.negate();
    }

    private BigDecimal getChangeAmountUSD(History history, BigDecimal quantity) {
        int price = priceService.getPrice();
        BigDecimal priceBD = BigDecimal.valueOf(price);

        return history.getTransType().equals(TradeDirection.BUY.name())
                ? quantity.multiply(priceBD.negate())
                : quantity.multiply(priceBD);
    }

    @PostMapping("/save")
    public Integer createTrade(@RequestBody History history) {
        validateUser(history.getUserId());
        Coin coin = validateCoin(history.getCoinId());
        validateAccount(history.getUserId(), history.getCoinId(), coin);
        updateHistory(history);
        historyService.createHistory(history);
        return 1;
    }
}
