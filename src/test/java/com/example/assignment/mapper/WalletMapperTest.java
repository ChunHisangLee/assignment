package com.example.assignment.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.assignment.dto.WalletDto;
import com.example.assignment.entity.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class WalletMapperTest {

  private WalletMapper walletMapper;

  @BeforeEach
  void setUp() {
    walletMapper = new WalletMapper();
  }

  @Test
  void toDto_shouldMapWalletToWalletDTO() {
    // Arrange
    Wallet wallet = Wallet.builder().id(1L).usdBalance(BigDecimal.valueOf(1000.0)).btcBalance(BigDecimal.valueOf(0.5)).build();

    // Act
    WalletDto walletDTO = walletMapper.toDto(wallet);

    // Assert
    assertThat(walletDTO).isNotNull();
    assertThat(walletDTO.getId()).isEqualTo(wallet.getId());
    assertThat(walletDTO.getUsdBalance()).isEqualTo(wallet.getUsdBalance());
    assertThat(walletDTO.getBtcBalance()).isEqualTo(wallet.getBtcBalance());
  }

  @Test
  void toDto_shouldReturnNullWhenWalletIsNull() {
    // Act
    WalletDto walletDTO = walletMapper.toDto(null);

    // Assert
    assertThat(walletDTO).isNull();
  }

  @Test
  void toEntity_shouldMapWalletDTOToWallet() {
    // Arrange
    WalletDto walletDTO = WalletDto.builder().id(1L).usdBalance(1000.0).btcBalance(0.5).build();

    // Act
    Wallet wallet = walletMapper.toEntity(walletDTO);

    // Assert
    assertThat(wallet).isNotNull();
    assertThat(wallet.getId()).isNull(); // ID should not be set when converting from DTO to Entity
    assertThat(wallet.getUsdBalance()).isEqualTo(walletDTO.getUsdBalance());
    assertThat(wallet.getBtcBalance()).isEqualTo(walletDTO.getBtcBalance());
  }

  @Test
  void toEntity_shouldReturnNullWhenWalletDTOIsNull() {
    // Act
    Wallet wallet = walletMapper.toEntity(null);

    // Assert
    assertThat(wallet).isNull();
  }
}
