package com.example.assignment.service.impl;

import com.example.assignment.entity.*;
import com.example.assignment.repository.BTCPriceHistoryRepository;
import com.example.assignment.repository.TransactionRepository;
import com.example.assignment.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BTCPriceHistoryRepository btcPriceHistoryRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void testCreateTransaction_Buy_Success() {
        Users sampleUser = createSampleUser();
        BTCPriceHistory samplePriceHistory = createSamplePriceHistory();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(sampleUser));
        when(btcPriceHistoryRepository.findTopByOrderByTimestampDesc()).thenReturn(Optional.of(samplePriceHistory));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        double btcAmount = 0.01;
        Transaction transaction = transactionService.createTransaction(1L, btcAmount, TransactionType.BUY);

        assertNotNull(transaction);
        assertEquals(sampleUser, transaction.getUsers());
        assertEquals(btcAmount, transaction.getBtcAmount());
        assertEquals(samplePriceHistory, transaction.getBtcPriceHistory());
        assertEquals(TransactionType.BUY, transaction.getTransactionType());

        verify(userRepository, times(1)).save(sampleUser);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testCreateTransaction_Sell_Success() {
        Users sampleUser = createSampleUser();
        BTCPriceHistory samplePriceHistory = createSamplePriceHistory();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(sampleUser));
        when(btcPriceHistoryRepository.findTopByOrderByTimestampDesc()).thenReturn(Optional.of(samplePriceHistory));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        double btcAmount = 1.0;
        Transaction transaction = transactionService.createTransaction(1L, btcAmount, TransactionType.SELL);

        assertNotNull(transaction);
        assertEquals(sampleUser, transaction.getUsers());
        assertEquals(btcAmount, transaction.getBtcAmount());
        assertEquals(samplePriceHistory, transaction.getBtcPriceHistory());
        assertEquals(TransactionType.SELL, transaction.getTransactionType());

        verify(userRepository, times(1)).save(sampleUser);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testCreateTransaction_Buy_InsufficientFunds() {
        Users sampleUser = createSampleUser();
        BTCPriceHistory samplePriceHistory = createSamplePriceHistory();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(sampleUser));
        when(btcPriceHistoryRepository.findTopByOrderByTimestampDesc()).thenReturn(Optional.of(samplePriceHistory));

        double btcAmount = 1.0; // This will require more USD than available

        Exception exception = assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(1L, btcAmount, TransactionType.BUY));

        assertEquals("Insufficient USD balance", exception.getMessage());

        verify(userRepository, never()).save(any(Users.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_Sell_InsufficientBTC() {
        Users sampleUser = createSampleUser();
        BTCPriceHistory samplePriceHistory = createSamplePriceHistory();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(sampleUser));
        when(btcPriceHistoryRepository.findTopByOrderByTimestampDesc()).thenReturn(Optional.of(samplePriceHistory));

        double btcAmount = 3.0; // More BTC than available

        Exception exception = assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(1L, btcAmount, TransactionType.SELL));

        assertEquals("Insufficient BTC balance", exception.getMessage());

        verify(userRepository, never()).save(any(Users.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    private Users createSampleUser() {
        Users sampleUser = new Users();
        sampleUser.setId(1L);
        sampleUser.setName("Jack Lee");
        sampleUser.setEmail("jacklee@example.com");
        sampleUser.setPassword("plainPassword");

        Wallet sampleWallet = new Wallet();
        sampleWallet.setId(1L);
        sampleWallet.setUsdBalance(1000.0);
        sampleWallet.setUsers(sampleUser);

        sampleUser.setWallet(sampleWallet);

        return sampleUser;
    }

    private BTCPriceHistory createSamplePriceHistory() {
        BTCPriceHistory samplePriceHistory = new BTCPriceHistory();
        samplePriceHistory.setId(1L);
        samplePriceHistory.setPrice(50000.0);
        samplePriceHistory.setTimestamp(LocalDateTime.now());
        return samplePriceHistory;
    }
}
