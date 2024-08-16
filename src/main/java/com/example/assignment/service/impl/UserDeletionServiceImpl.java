package com.example.assignment.service.impl;

import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.User;
import com.example.assignment.service.IAccountService;
import com.example.assignment.service.IHistoryService;
import com.example.assignment.service.IUserService;
import com.example.assignment.service.UserDeletionService;
import com.example.assignment.service.exception.InsertException;
import com.example.assignment.service.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDeletionServiceImpl implements UserDeletionService {

    private final IUserService userService;
    private final IAccountService accountService;
    private final IHistoryService historyService;

    public UserDeletionServiceImpl(IUserService userService, IAccountService accountService, IHistoryService historyService) {
        this.userService = userService;
        this.accountService = accountService;
        this.historyService = historyService;
    }

    @Override
    public void deleteUser(User user) {
        validateUserExists(user);
        deleteUserFromDatabase(user);
        deleteAssociatedAccounts(user);
        deleteAssociatedHistories(user);
    }

    private void validateUserExists(User user) {
        User userQuery = userService.getUser(user);
        if (userQuery == null) {
            throw new UserNotFoundException("The userName doesn't exist!");
        }
    }

    private void deleteUserFromDatabase(User user) {
        Integer rows = userService.deleteUser(user);
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
        }
    }

    private void deleteAssociatedAccounts(User user) {
        List<Account> accountList = accountService.getAccounts(user.getUserId());
        if (accountList == null || accountList.isEmpty()) {
            throw new UserNotFoundException("The user doesn't have any account!");
        }
        Integer rows = accountService.deleteAccount(accountList.get(0).getUserId());
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
        }
    }

    private void deleteAssociatedHistories(User user) {
        List<Transaction> historyList = historyService.getHistories(user.getUserId());
        if (historyList == null || historyList.isEmpty()) {
            throw new UserNotFoundException("The user doesn't have any trading record!");
        }
        Integer rows = historyService.deleteHistory(historyList.get(0).getUserId());
        if (rows != 1) {
            throw new InsertException("Unknown exception!");
        }
    }
}
