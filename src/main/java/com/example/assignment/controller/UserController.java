package com.example.assignment.controller;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.Coin;
import com.example.assignment.entity.User;
import com.example.assignment.service.IAccountService;
import com.example.assignment.service.ICoinService;
import com.example.assignment.service.IUserService;
import com.example.assignment.service.ex.InsertException;
import com.example.assignment.service.ex.ServiceException;
import com.example.assignment.service.ex.UserNameDuplicatedException;
import com.example.assignment.service.ex.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private ICoinService coinService;

    @PostMapping("/register")
    public Integer register(User user) {
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
    public Integer deleteUser(User user) {
        User userQuery = userService.getUser(user);
        if (userQuery == null) {
            throw new UserNotFoundException("The userName doesn't exist!");
        }
        Integer rows = userService.deleteUser(user);
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
        }
        return 1;
    }
}
