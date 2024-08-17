package com.example.assignment.service.impl;

import com.example.assignment.entity.Users;
import com.example.assignment.entity.Wallet;
import com.example.assignment.repository.UserRepository;
import com.example.assignment.repository.WalletRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private Users sampleUser;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        sampleUser = new Users();
        sampleUser.setId(1L);
        sampleUser.setName("Jack Lee");
        sampleUser.setEmail("jacklee@example.com");
        sampleUser.setPassword("plainPassword");
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void testCreateUser() {
        Wallet wallet = new Wallet();
        wallet.setUsdBalance(1000.0);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Users createdUser = userService.createUser(sampleUser);

        assertNotNull(createdUser);
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals(1000.0, createdUser.getWallet().getUsdBalance());

        verify(userRepository, times(1)).save(any(Users.class));
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void testUpdateUser() {
        Users updatedUser = new Users();
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Users> result = userService.updateUser(1L, updatedUser);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
        assertEquals("updated@example.com", result.get().getEmail());
        assertEquals("encodedNewPassword", result.get().getPassword());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(Users.class));
    }

    @Test
    void testDeleteUser_UserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_UserDoesNotExist() {
        when(userRepository.existsById(1L)).thenReturn(false);

        boolean result = userService.deleteUser(1L);

        assertFalse(result);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        Optional<Users> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(sampleUser, result.get());

        verify(userRepository, times(1)).findById(1L);
    }
}
