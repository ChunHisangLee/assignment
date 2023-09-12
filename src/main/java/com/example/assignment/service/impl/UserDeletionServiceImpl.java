package com.example.assignment.service.impl;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.History;
import com.example.assignment.entity.User;
import com.example.assignment.service.IAccountService;
import com.example.assignment.service.IHistoryService;
import com.example.assignment.service.IUserService;
import com.example.assignment.service.exception.InsertException;
import com.example.assignment.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDeletionServiceImpl implements UserDeletionService {
    @Autowired
    private IUserService userService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IHistoryService historyService;

    @Override
    public void deleteUser(User user) {
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
    }
}
