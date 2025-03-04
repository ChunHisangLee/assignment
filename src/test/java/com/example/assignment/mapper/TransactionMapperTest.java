package com.example.assignment.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.assignment.dto.TransactionDto;
import com.example.assignment.entity.BTCPriceHistory;
import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.entity.Users;
import com.example.assignment.entity.Wallet;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    BTCPriceHistory btcPriceHistory =
        BTCPriceHistory.builder()
            .id(1L)
            .price(BigDecimal.valueOf(450.0))
            .timestamp(LocalDateTime.of(2023, 1, 1, 12, 0))
            .build();

    Users user = Users.builder().id(1L).name("Jack Lee").email("jacklee@example.com").build();

    Wallet wallet =
        Wallet.builder()
            .usdBalance(BigDecimal.valueOf(1200.0))
            .btcBalance(BigDecimal.valueOf(0.6))
            .users(user)
            .build();

    user.setWallet(wallet);

    Transaction transaction =
        Transaction.builder()
            .id(1L)
            .btcAmount(BigDecimal.valueOf(0.01))
            .transactionTime(LocalDateTime.of(2023, 1, 1, 12, 0))
            .transactionType(TransactionType.BUY)
            .btcPriceHistory(btcPriceHistory)
            .users(user)
            .build();

    TransactionDto expectedTransactionDto =
        TransactionDto.builder()
            .id(1L)
            .userId(1L)
            .btcAmount(BigDecimal.valueOf(0.01))
            .transactionTime(LocalDateTime.of(2023, 1, 1, 12, 0))
            .transactionType(TransactionType.BUY)
            .usdBalanceBefore(BigDecimal.valueOf(1000.0))
            .btcBalanceBefore(BigDecimal.valueOf(0.5))
            .usdBalanceAfter(BigDecimal.valueOf(1200.0))
            .btcBalanceAfter(BigDecimal.valueOf(0.6))
            .build();

    when(btcPriceHistoryMapper.toDto(btcPriceHistory)).thenReturn(null); // Mock return value
    when(usersMapper.toDto(user)).thenReturn(null); // Mock return value

    // When
    TransactionDto transactionDto =
        transactionMapper.toDto(transaction, BigDecimal.valueOf(1000.0), BigDecimal.valueOf(0.5));

    // Then
    assertThat(transactionDto).usingRecursiveComparison().isEqualTo(expectedTransactionDto);
  }
}
