package com.example.assignment.service.impl;

import com.example.assignment.entity.Wallet;
import com.example.assignment.entity.User;
import com.example.assignment.service.IAccountService;
import com.example.assignment.service.ICoinService;
import com.example.assignment.service.IUserService;
import com.example.assignment.service.exception.ServiceException;
import com.example.assignment.service.exception.UserNameDuplicatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserRegistrationServiceImplTest {

    @InjectMocks
    private UserRegistrationServiceImpl userRegistrationService;

    @Mock
    private IUserService userService;

    @Mock
    private IAccountService accountService;

    @Mock
    private ICoinService coinService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUserSuccessfully() {
        User user = new User();
        user.setUserId("testUser");

        Wallet coinUSD = new Wallet();
        coinUSD.setCoinName("USD");
        coinUSD.setCoinId(1);

        Wallet coinBTC = new Wallet();
        coinBTC.setCoinName("BTC");
        coinBTC.setCoinId(2);

        when(userService.getUser(user)).thenReturn(null);
        when(coinService.getCoin("USD")).thenReturn(coinUSD);
        when(coinService.getCoin("BTC")).thenReturn(coinBTC);
        when(accountService.getAccount(user.getUserId(), coinUSD.getCoinId())).thenReturn(null);
        when(accountService.getAccount(user.getUserId(), coinBTC.getCoinId())).thenReturn(null);

        userRegistrationService.registerUser(user);

        verify(userService).register(user);
        verify(accountService).createAccount(user, coinUSD);
        verify(accountService).createAccount(user, coinBTC);
    }

    @Test
    public void testRegisterUserWithExistingUserName() {
        User user = new User();
        user.setUserId("testUser");

        when(userService.getUser(user)).thenReturn(user);

        assertThrows(UserNameDuplicatedException.class, () -> userRegistrationService.registerUser(user));
    }

    @Test
    public void testRegisterUserWithoutCoinUSDData() {
        User user = new User();
        user.setUserId("testUser");

        when(userService.getUser(user)).thenReturn(null);
        when(coinService.getCoin("USD")).thenReturn(null);

        assertThrows(ServiceException.class, () -> userRegistrationService.registerUser(user));
    }

    // Similarly, you can add tests for other scenarios and exceptions.
}
