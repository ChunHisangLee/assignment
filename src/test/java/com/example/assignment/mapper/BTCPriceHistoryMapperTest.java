package com.example.assignment.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.assignment.dto.BTCPriceHistoryDto;
import com.example.assignment.entity.BTCPriceHistory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BTCPriceHistoryMapperTest {

  private BTCPriceHistoryMapper btcPriceHistoryMapper;

  @BeforeEach
  void setUp() {
    btcPriceHistoryMapper = new BTCPriceHistoryMapper();
  }

  @Test
  void toDto_ShouldMapEntityToDto() {
    // Given
    BTCPriceHistory btcPriceHistory =
        BTCPriceHistory.builder()
            .id(1L)
            .price(BigDecimal.valueOf(450.0))
            .timestamp(LocalDateTime.of(2023, 1, 1, 12, 0))
            .build();

    // When
    BTCPriceHistoryDto btcPriceHistoryDto = btcPriceHistoryMapper.toDto(btcPriceHistory);

    // Then
    assertThat(btcPriceHistoryDto).isNotNull();
    assertThat(btcPriceHistoryDto.getId()).isEqualTo(1L);
    assertThat(btcPriceHistoryDto.getPrice()).isEqualTo(BigDecimal.valueOf(450.0));
    assertThat(btcPriceHistoryDto.getTimestamp()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
  }

  @Test
  void toEntity_ShouldMapDtoToEntity() {
    // Given
    BTCPriceHistoryDto btcPriceHistoryDto =
        BTCPriceHistoryDto.builder()
            .id(1L)
            .price(BigDecimal.valueOf(450.0))
            .timestamp(LocalDateTime.of(2023, 1, 1, 12, 0))
            .build();

    // When
    BTCPriceHistory btcPriceHistory = btcPriceHistoryMapper.toEntity(btcPriceHistoryDto);

    // Then
    assertThat(btcPriceHistory).isNotNull();
    assertThat(btcPriceHistory.getPrice()).isEqualTo(BigDecimal.valueOf(450.0));
    assertThat(btcPriceHistory.getTimestamp()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
  }

  @Test
  void toEntity_ShouldUseCurrentTimestampWhenTimestampIsNull() {
    // Given
    BTCPriceHistoryDto btcPriceHistoryDto =
        BTCPriceHistoryDto.builder()
            .id(1L)
            .price(BigDecimal.valueOf(450.0))
            .timestamp(null)
            .build();

    // Capture time before mapping
    LocalDateTime beforeMapping = LocalDateTime.now();

    // When
    BTCPriceHistory btcPriceHistory = btcPriceHistoryMapper.toEntity(btcPriceHistoryDto);

    // Capture time after mapping
    LocalDateTime afterMapping = LocalDateTime.now();

    // Then
    assertThat(btcPriceHistory).isNotNull();
    assertThat(btcPriceHistory.getPrice()).isEqualTo(BigDecimal.valueOf(450.0));
    assertThat(btcPriceHistory.getTimestamp()).isNotNull();
    // Assert that the timestamp is between beforeMapping and afterMapping
    assertThat(btcPriceHistory.getTimestamp()).isAfterOrEqualTo(beforeMapping);
    assertThat(btcPriceHistory.getTimestamp()).isBeforeOrEqualTo(afterMapping);
  }
}
