package com.example.assignment.service.impl;

import com.example.assignment.entity.Wallet;
import com.example.assignment.entity.User;
import com.example.assignment.mapper.AccountMapper;
import com.example.assignment.service.exception.InsertException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountMapper accountMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAccountForUsd() {
        User user = new User();
        user.setUserId("testUser");
        Wallet coin = new Wallet();
        coin.setCoinId(1);
        coin.setCoinName("USD");

        when(accountMapper.insert(any(Account.class))).thenReturn(1);

        accountService.createAccount(user, coin);

        verify(accountMapper, times(1)).insert(any(Account.class));
    }

    @Test
    public void testCreateAccountInsertException() {
        User user = new User();
        user.setUserId("testUser");
        Wallet coin = new Wallet();
        coin.setCoinId(2);
        coin.setCoinName("BTC");

        when(accountMapper.insert(any(Account.class))).thenReturn(0);

        assertThrows(InsertException.class, () -> accountService.createAccount(user, coin));
    }

    @Test
    public void testGetAccount() {
        Account expectedAccount = new Account();
        expectedAccount.setUserId("testUser");
        expectedAccount.setCoinId(1);

        when(accountMapper.findByKey("testUser", 1)).thenReturn(expectedAccount);

        Account result = accountService.getAccount("testUser", 1);

        assertEquals(expectedAccount, result);
    }

    @Test
    public void testGetAccounts() {
        Account account = new Account();
        account.setUserId("testUser");
        account.setCoinId(1);

        when(accountMapper.findByUserId("testUser")).thenReturn(Collections.singletonList(account));

        List<Account> accounts = accountService.getAccounts("testUser");

        assertEquals(1, accounts.size());
        assertEquals(account, accounts.get(0));
    }

    @Test
    public void testDeleteAccount() {
        when(accountMapper.deleteAccount("testUser")).thenReturn(1);

        Integer result = accountService.deleteAccount("testUser");

        assertEquals(Integer.valueOf(1), result);
    }
}
