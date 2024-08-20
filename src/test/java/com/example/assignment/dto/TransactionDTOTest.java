package com.example.assignment.dto;

import com.example.assignment.entity.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionDTOTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Initialize ObjectMapper and register the JavaTimeModule to handle LocalDateTime
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Ensure dates are written as strings
    }

    @Test
    void testSerialization() throws Exception {
        // Arrange
        LocalDateTime transactionTime = LocalDateTime.of(2023, 1, 1, 12, 0);

        BTCPriceHistoryDTO btcPriceHistoryDTO = BTCPriceHistoryDTO.builder()
                .id(1L)
                .price(450.0)
                .timestamp(transactionTime)
                .build();

        UsersDTO usersDTO = UsersDTO.builder()
                .id(1L)
                .name("Jack")
                .email("jack@example.com")
                .build();

        TransactionDTO transactionDTO = TransactionDTO.builder()
                .id(1L)
                .userId(1L)
                .btcAmount(0.01)
                .transactionTime(transactionTime)
                .transactionType(TransactionType.BUY)
                .btcPriceHistory(btcPriceHistoryDTO)
                .users(usersDTO)
                .usdBalanceBefore(1000.0)
                .btcBalanceBefore(0.5)
                .usdBalanceAfter(1200.0)
                .btcBalanceAfter(0.6)
                .build();

        // Act
        String json = objectMapper.writeValueAsString(transactionDTO);

        // Assert
        assertThat(json).isNotNull();
        assertThat(json).contains("\"transactionTime\":\"2023-01-01T12:00:00\"");
        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"userId\":1");
        assertThat(json).contains("\"btcAmount\":0.01");
        assertThat(json).contains("\"transactionType\":\"BUY\"");
        assertThat(json).contains("\"usdBalanceBefore\":1000.0");
        assertThat(json).contains("\"btcBalanceBefore\":0.5");
        assertThat(json).contains("\"usdBalanceAfter\":1200.0");
        assertThat(json).contains("\"btcBalanceAfter\":0.6");
        assertThat(json).contains("\"btcPriceHistory\"");
        assertThat(json).contains("\"users\"");
    }
}
