package com.example.assignment.service.impl;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.History;
import com.example.assignment.entity.User;
import com.example.assignment.service.IAccountService;
import com.example.assignment.service.IHistoryService;
import com.example.assignment.service.IUserService;
import com.example.assignment.service.exception.InsertException;
import com.example.assignment.service.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserDeletionServiceImplTest {

    @InjectMocks
    private UserDeletionServiceImpl userDeletionService;

    @Mock
    private IUserService userService;

    @Mock
    private IAccountService accountService;

    @Mock
    private IHistoryService historyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteUserSuccessfully() {
        User user = new User();
        user.setUserId("userId123");

        Account account = new Account();
        account.setUserId(user.getUserId());

        History history = new History();
        history.setUserId(user.getUserId());

        when(userService.getUser(user)).thenReturn(user);
        when(userService.deleteUser(user)).thenReturn(1);
        when(accountService.getAccounts(user.getUserId())).thenReturn(List.of(account));
        when(accountService.deleteAccount(account.getUserId())).thenReturn(1);
        when(historyService.getHistories(user.getUserId())).thenReturn(List.of(history));
        when(historyService.deleteHistory(history.getUserId())).thenReturn(1);

        userDeletionService.deleteUser(user);

        verify(userService).deleteUser(user);
        verify(accountService).deleteAccount(account.getUserId());
        verify(historyService).deleteHistory(history.getUserId());
    }

    @Test
    public void testDeleteUserUserNotFound() {
        User user = new User();

        when(userService.getUser(user)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userDeletionService.deleteUser(user));
    }

    @Test
    public void testDeleteUserInsertExceptionOnUserDeletion() {
        User user = new User();
        user.setUserId("userId123");

        when(userService.getUser(user)).thenReturn(user);
        when(userService.deleteUser(user)).thenReturn(0);

        assertThrows(InsertException.class, () -> userDeletionService.deleteUser(user));
    }

    // Additional tests can be added for other exceptions and scenarios
}
