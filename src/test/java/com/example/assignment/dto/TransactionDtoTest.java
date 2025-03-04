package com.example.assignment.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.assignment.entity.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionDtoTest {

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    // Initialize ObjectMapper and register the JavaTimeModule to handle LocalDateTime
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Write dates as strings
  }

  @Test
  void testSerialization() throws Exception {
    // Arrange
    LocalDateTime transactionTime = LocalDateTime.of(2023, 1, 1, 12, 0);

    BTCPriceHistoryDto btcPriceHistoryDTO =
        BTCPriceHistoryDto.builder()
            .id(1L)
            .price(BigDecimal.valueOf(450.0))
            .timestamp(transactionTime)
            .build();

    UsersDto usersDTO = UsersDto.builder().id(1L).name("Jack").email("jack@example.com").build();

    TransactionDto transactionDTO =
        TransactionDto.builder()
            .id(1L)
            .userId(1L)
            .btcAmount(BigDecimal.valueOf(0.01))
            .transactionTime(transactionTime)
            .transactionType(TransactionType.BUY)
            .btcPriceHistory(btcPriceHistoryDTO)
            .users(usersDTO)
            .usdBalanceBefore(BigDecimal.valueOf(1000.0))
            .btcBalanceBefore(BigDecimal.valueOf(0.5))
            .usdBalanceAfter(BigDecimal.valueOf(1200.0))
            .btcBalanceAfter(BigDecimal.valueOf(0.6))
            .build();

    // Act
    String json = objectMapper.writeValueAsString(transactionDTO);

    // Assert
    assertThat(json)
        .isNotNull()
        .contains("\"transactionTime\":\"2023-01-01T12:00:00\"")
        .contains("\"id\":1")
        .contains("\"userId\":1")
        .contains("\"btcAmount\":0.01")
        .contains("\"transactionType\":\"BUY\"")
        .contains("\"usdBalanceBefore\":1000.0")
        .contains("\"btcBalanceBefore\":0.5")
        .contains("\"usdBalanceAfter\":1200.0")
        .contains("\"btcBalanceAfter\":0.6")
        .contains("\"btcPriceHistory\":")
        .contains("\"users\":");
  }
}
