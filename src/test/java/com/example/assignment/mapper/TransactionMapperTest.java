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
                .name("Jack")
                .email("jack@example.com")
                .build();

        Wallet wallet = Wallet.builder()
                .usdBalance(1000.0)
                .btcBalance(0.5)
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

        when(btcPriceHistoryMapper.toDto(btcPriceHistory)).thenReturn(null); // Mock the return value for simplicity
        when(usersMapper.toDto(user)).thenReturn(null); // Mock the return value for simplicity

        // When
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction, 1000.0, 0.5);

        // Then
        assertThat(transactionDTO).isNotNull();
        assertThat(transactionDTO.getId()).isEqualTo(1L);
        assertThat(transactionDTO.getBtcAmount()).isEqualTo(0.01);
        assertThat(transactionDTO.getTransactionTime()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
        assertThat(transactionDTO.getTransactionType()).isEqualTo(TransactionType.BUY);
        assertThat(transactionDTO.getUsdBalanceBefore()).isEqualTo(1000.0);
        assertThat(transactionDTO.getBtcBalanceBefore()).isEqualTo(0.5);
        assertThat(transactionDTO.getUsdBalanceAfter()).isEqualTo(1000.0);
        assertThat(transactionDTO.getBtcBalanceAfter()).isEqualTo(0.5);
    }
}
