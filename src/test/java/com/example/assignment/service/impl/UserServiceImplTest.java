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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
    private Wallet sampleWallet;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        sampleUser = new Users();
        sampleUser.setId(1L);
        sampleUser.setName("Jack Lee");
        sampleUser.setEmail("jacklee@example.com");
        sampleUser.setPassword("plainPassword");

        sampleWallet = new Wallet();
        sampleWallet.setId(1L);
        sampleWallet.setUsdBalance(1000.0);
        sampleWallet.setUsers(sampleUser);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void testCreateUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(Users.class))).thenReturn(sampleUser);
        when(walletRepository.save(any(Wallet.class))).thenReturn(sampleWallet);

        Users createdUser = userService.createUser(sampleUser);

        assertNotNull(createdUser);
        assertEquals("encodedPassword", createdUser.getPassword());
        assertNotNull(createdUser.getWallet());
        assertEquals(1000.0, createdUser.getWallet().getUsdBalance());

        verify(userRepository, times(1)).save(any(Users.class));
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void testUpdateUser_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(Users.class))).thenReturn(sampleUser);

        Users updatedUser = new Users();
        updatedUser.setName("Jack Lee Updated");
        updatedUser.setEmail("jacklee.updated@example.com");
        updatedUser.setPassword("newpassword");

        Optional<Users> result = userService.updateUser(1L, updatedUser);

        assertTrue(result.isPresent());
        assertEquals("Jack Lee Updated", result.get().getName());
        assertEquals("jacklee.updated@example.com", result.get().getEmail());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any(Users.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Users updatedUser = new Users();
        updatedUser.setName("Jack Lee Updated");
        updatedUser.setEmail("jacklee.updated@example.com");

        Optional<Users> result = userService.updateUser(1L, updatedUser);

        assertFalse(result.isPresent());
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(userRepository).deleteById(anyLong());

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        boolean result = userService.deleteUser(1L);

        assertFalse(result);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(sampleUser));

        Optional<Users> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(sampleUser, result.get());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Users> result = userService.getUserById(1L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindByEmail_Found() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(sampleUser));

        Optional<Users> result = userService.findByEmail("jacklee@example.com");

        assertTrue(result.isPresent());
        assertEquals(sampleUser, result.get());
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void testFindByEmail_NotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Optional<Users> result = userService.findByEmail("jacklee@example.com");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void testVerifyPassword_Success() {
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        boolean result = userService.verifyPassword("password", "encodedPassword");

        assertTrue(result);
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }

    @Test
    void testVerifyPassword_Failure() {
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        boolean result = userService.verifyPassword("password", "wrongEncodedPassword");

        assertFalse(result);
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }
}
