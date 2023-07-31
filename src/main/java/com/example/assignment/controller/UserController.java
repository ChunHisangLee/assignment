package com.example.assignment.controller;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.Coin;
import com.example.assignment.entity.History;
import com.example.assignment.entity.User;
import com.example.assignment.service.IAccountService;
import com.example.assignment.service.ICoinService;
import com.example.assignment.service.IHistoryService;
import com.example.assignment.service.IUserService;
import com.example.assignment.service.exception.InsertException;
import com.example.assignment.service.exception.ServiceException;
import com.example.assignment.service.exception.UserNameDuplicatedException;
import com.example.assignment.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private ICoinService coinService;
    @Autowired
    private IHistoryService historyService;

    @PostMapping("/register")
    public Integer register(@RequestBody User user) {
        User userQuery = userService.getUser(user);
        if (userQuery != null) {
            throw new UserNameDuplicatedException("The userName has been registered!");
        }
        Coin coinUSD = coinService.getCoin("USD");
        if (coinUSD == null) {
            throw new ServiceException("The coin data doesn't exist!");
        }
        Coin coinBTC = coinService.getCoin("BTC");
        if (coinBTC == null) {
            throw new ServiceException("The coin data doesn't exist!");
        }
        userService.register(user);

        Account accountUSD = accountService.getAccount(user.getUserId(), coinUSD.getCoinId());
        if (accountUSD != null) {
            throw new UserNameDuplicatedException("The user already has a(n) " + coinUSD.getCoinName() + " account!");
        }
        accountService.createAccount(user, coinUSD);

        Account accountBTC = accountService.getAccount(user.getUserId(), coinBTC.getCoinId());
        if (accountBTC != null) {
            throw new UserNameDuplicatedException("The user already has a(n) " + coinBTC.getCoinName() + " account!");
        }
        accountService.createAccount(user, coinBTC);
        return 1;
    }

    @PostMapping("/delete")
    public Integer deleteUser(@RequestBody User user) {
        User userQuery = userService.getUser(user);
        if (userQuery == null) {
            throw new UserNotFoundException("The userName doesn't exist!");
        }
        Integer rows = userService.deleteUser(user);
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
        }
        List<Account> accountList = accountService.getAccounts(user.getUserId());
        if (accountList == null) {
            throw new UserNotFoundException("The user doesn't have any account!");
        }
        rows = accountService.deleteAccount(accountList.get(0).getUserId());
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
        }
        List<History> historyList = historyService.getHistories(user.getUserId());
        if (historyList == null) {
            throw new UserNotFoundException("The user doesn't have any trading record!");
        }
        rows = historyService.deleteHistory(historyList.get(0).getUserId());
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
        }
        return 1;
    }
}
