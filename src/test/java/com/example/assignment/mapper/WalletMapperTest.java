package com.example.assignment.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.assignment.dto.WalletDto;
import com.example.assignment.entity.Wallet;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WalletMapperTest {

  private WalletMapper walletMapper;

  @BeforeEach
  void setUp() {
    walletMapper = new WalletMapper();
  }

  @Test
  void toDto_shouldMapWalletToWalletDto() {
    // Arrange
    Wallet wallet = Wallet.builder().id(1L).usdBalance(BigDecimal.valueOf(1000.0)).btcBalance(BigDecimal.valueOf(0.5)).build();

    // Act
    WalletDto walletDto = walletMapper.toDto(wallet);

    // Assert
    assertThat(walletDto).isNotNull();
    assertThat(walletDto.getId()).isEqualTo(wallet.getId());
    assertThat(walletDto.getUsdBalance()).isEqualTo(wallet.getUsdBalance());
    assertThat(walletDto.getBtcBalance()).isEqualTo(wallet.getBtcBalance());
  }

  @Test
  void toDto_shouldReturnNullWhenWalletIsNull() {
    // Act
    WalletDto walletDto = walletMapper.toDto(null);

    // Assert
    assertThat(walletDto).isNull();
  }

  @Test
  void toEntity_shouldMapWalletDtoToWallet() {
    // Arrange
    WalletDto walletDto =
        WalletDto.builder()
            .id(1L)
            .usdBalance(BigDecimal.valueOf(1000.0))
            .btcBalance(BigDecimal.valueOf(0.5))
            .build();

    // Act
    Wallet wallet = walletMapper.toEntity(walletDto);

    // Assert
    assertThat(wallet).isNotNull();
    assertThat(wallet.getId()).isNull(); // ID should not be set when converting from Dto to Entity
    assertThat(wallet.getUsdBalance()).isEqualTo(walletDto.getUsdBalance());
    assertThat(wallet.getBtcBalance()).isEqualTo(walletDto.getBtcBalance());
  }

  @Test
  void toEntity_shouldReturnNullWhenWalletDtoIsNull() {
    // Act
    Wallet wallet = walletMapper.toEntity(null);

    // Assert
    assertThat(wallet).isNull();
  }
}
