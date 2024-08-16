package com.example.assignment.service.impl;

import com.example.assignment.entity.Wallet;
import com.example.assignment.entity.User;
import com.example.assignment.service.IAccountService;
import com.example.assignment.service.ICoinService;
import com.example.assignment.service.IUserService;
import com.example.assignment.service.UserRegistrationService;
import com.example.assignment.service.exception.ServiceException;
import com.example.assignment.service.exception.UserNameDuplicatedException;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final IUserService userService;
    private final IAccountService accountService;
    private final ICoinService coinService;

    public UserRegistrationServiceImpl(IUserService userService, IAccountService accountService, ICoinService coinService) {
        this.userService = userService;
        this.accountService = accountService;
        this.coinService = coinService;
    }

    @Override
    public void registerUser(User user) {
        validateUserNotRegistered(user);
        registerNewUser(user);
        setupInitialAccounts(user);
    }

    private void validateUserNotRegistered(User user) {
        User userQuery = userService.getUser(user);
        if (userQuery != null) {
            throw new UserNameDuplicatedException("The userName has been registered!");
        }
    }

    private void registerNewUser(User user) {
        userService.register(user);
    }

    private void setupInitialAccounts(User user) {
        createAccountForCoin(user, "USD");
        createAccountForCoin(user, "BTC");
    }

    private void createAccountForCoin(User user, String coinName) {
        Wallet coin = coinService.getCoin(coinName);
        if (coin == null) {
            throw new ServiceException("The coin data doesn't exist!");
        }

        Account existingAccount = accountService.getAccount(user.getUserId(), coin.getCoinId());
        if (existingAccount != null) {
            throw new UserNameDuplicatedException("The user already has a(n) " + coin.getCoinName() + " account!");
        }
        accountService.createAccount(user, coin);
    }
}
