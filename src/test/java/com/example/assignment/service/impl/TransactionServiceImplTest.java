package com.example.assignment.service.impl;

import com.example.assignment.dto.CreateTransactionRequest;
import com.example.assignment.dto.TransactionDTO;
import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.entity.Users;
import com.example.assignment.entity.Wallet;
import com.example.assignment.mapper.TransactionMapper;
import com.example.assignment.repository.TransactionRepository;
import com.example.assignment.repository.UsersRepository;
import com.example.assignment.service.PriceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private PriceService priceService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void createTransaction_BuySuccess() {
        // Arrange
        Long userId = 1L;
        double btcAmount = 0.5;
        int currentPrice = 100;
        double usdBalance = 200.0;

        Users user = new Users();
        user.setId(userId);
        Wallet wallet = new Wallet(userId, usdBalance, 0.0, user);
        user.setWallet(wallet);

        CreateTransactionRequest request = new CreateTransactionRequest(userId, btcAmount);
        TransactionDTO transactionDTO = new TransactionDTO();

        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(priceService.getPrice()).thenReturn(currentPrice);
        when(transactionMapper.toDto(any(Transaction.class), anyDouble(), anyDouble())).thenReturn(transactionDTO);

        // Act
        TransactionDTO result = transactionService.createTransaction(request, TransactionType.BUY);

        // Assert
        assertNotNull(result);
        verify(usersRepository, times(1)).findById(userId);
        verify(priceService, times(1)).getPrice();
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(transactionMapper, times(1)).toDto(any(Transaction.class), anyDouble(), anyDouble());

        assertEquals(usdBalance - btcAmount * currentPrice, wallet.getUsdBalance());
        assertEquals(btcAmount, wallet.getBtcBalance());
    }

    @Test
    void createTransaction_SellSuccess() {
        // Arrange
        Long userId = 1L;
        double btcAmount = 0.5;
        int currentPrice = 100;
        double btcBalance = 1.0;

        Users user = new Users();
        user.setId(userId);
        Wallet wallet = new Wallet(userId, 0.0, btcBalance, user);
        user.setWallet(wallet);

        CreateTransactionRequest request = new CreateTransactionRequest(userId, btcAmount);
        TransactionDTO transactionDTO = new TransactionDTO();

        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(priceService.getPrice()).thenReturn(currentPrice);
        when(transactionMapper.toDto(any(Transaction.class), anyDouble(), anyDouble())).thenReturn(transactionDTO);

        // Act
        TransactionDTO result = transactionService.createTransaction(request, TransactionType.SELL);

        // Assert
        assertNotNull(result);
        verify(usersRepository, times(1)).findById(userId);
        verify(priceService, times(1)).getPrice();
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(transactionMapper, times(1)).toDto(any(Transaction.class), anyDouble(), anyDouble());

        assertEquals(btcBalance - btcAmount, wallet.getBtcBalance());
        assertEquals(btcAmount * currentPrice, wallet.getUsdBalance());
    }

    @Test
    void createTransaction_UserNotFound() {
        // Arrange
        Long userId = 1L;
        CreateTransactionRequest request = new CreateTransactionRequest(userId, 0.5);

        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.createTransaction(request, TransactionType.BUY));

        assertEquals("User not found", exception.getMessage());
        verify(usersRepository, times(1)).findById(userId);
        verify(priceService, times(0)).getPrice();
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    void getUserTransactionHistory_Success() {
        // Arrange
        Long userId = 1L;
        Users user = new Users();
        user.setId(userId);

        Wallet wallet = new Wallet(userId, 1000.0, 0.5, user);
        user.setWallet(wallet);

        Transaction transaction = new Transaction();
        transaction.setUsers(user);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> transactionPage = new PageImpl<>(List.of(transaction));

        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findByUsers(user, pageable)).thenReturn(transactionPage);
        when(transactionMapper.toDto(any(Transaction.class), anyDouble(), anyDouble()))
                .thenReturn(new TransactionDTO());

        // Act
        Page<TransactionDTO> result = transactionService.getUserTransactionHistory(userId, pageable);

        // Assert
        assertNotNull(result);
        verify(usersRepository, times(1)).findById(userId);
        verify(transactionRepository, times(1)).findByUsers(user, pageable);
        verify(transactionMapper, times(1)).toDto(any(Transaction.class), anyDouble(), anyDouble());
    }

}
