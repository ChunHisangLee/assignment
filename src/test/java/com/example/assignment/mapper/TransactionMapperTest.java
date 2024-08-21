package com.example.assignment.mapper;

import com.example.assignment.dto.TransactionDTO;
import com.example.assignment.entity.BTCPriceHistory;
import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.entity.Users;
import com.example.assignment.entity.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionMapperTest {

    private TransactionMapper transactionMapper;
    private BTCPriceHistoryMapper btcPriceHistoryMapper;
    private UsersMapper usersMapper;

    @BeforeEach
    void setUp() {
        btcPriceHistoryMapper = mock(BTCPriceHistoryMapper.class);
        usersMapper = mock(UsersMapper.class);
        transactionMapper = new TransactionMapper(btcPriceHistoryMapper, usersMapper);
    }

    @Test
    void toDto_ShouldMapTransactionToDto() {
        // Given
        BTCPriceHistory btcPriceHistory = BTCPriceHistory.builder()
                .id(1L)
                .price(450.0)
                .timestamp(LocalDateTime.of(2023, 1, 1, 12, 0))
                .build();

        Users user = Users.builder()
                .id(1L)
                .name("Jack Lee")
                .email("jacklee@example.com")
                .build();

        Wallet wallet = Wallet.builder()
                .usdBalance(1200.0)
                .btcBalance(0.6)
                .users(user)
                .build();

        user.setWallet(wallet);

        Transaction transaction = Transaction.builder()
                .id(1L)
                .btcAmount(0.01)
                .transactionTime(LocalDateTime.of(2023, 1, 1, 12, 0))
                .transactionType(TransactionType.BUY)
                .btcPriceHistory(btcPriceHistory)
                .users(user)
                .build();

        TransactionDTO expectedTransactionDTO = TransactionDTO.builder()
                .id(1L)
                .userId(1L)
                .btcAmount(0.01)
                .transactionTime(LocalDateTime.of(2023, 1, 1, 12, 0))
                .transactionType(TransactionType.BUY)
                .usdBalanceBefore(1000.0)
                .btcBalanceBefore(0.5)
                .usdBalanceAfter(1200.0)
                .btcBalanceAfter(0.6)
                .build();

        when(btcPriceHistoryMapper.toDto(btcPriceHistory)).thenReturn(null); // Mock return value
        when(usersMapper.toDto(user)).thenReturn(null); // Mock return value

        // When
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction, 1000.0, 0.5);

        // Then
        assertThat(transactionDTO).usingRecursiveComparison()
                .isEqualTo(expectedTransactionDTO);
    }
}
