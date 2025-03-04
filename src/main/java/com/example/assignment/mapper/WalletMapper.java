package com.example.assignment.mapper;

import com.example.assignment.dto.WalletDto;
import com.example.assignment.entity.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

  public WalletDto toDto(Wallet wallet) {
    if (wallet == null) {
      return null;
    }
    return WalletDto.builder()
        .id(wallet.getId())
        .usdBalance(wallet.getUsdBalance())
        .btcBalance(wallet.getBtcBalance())
        .build();
  }

  public Wallet toEntity(WalletDto walletDto) {
    if (walletDto == null) {
      return null;
    }
    return Wallet.builder()
        .usdBalance(walletDto.getUsdBalance())
        .btcBalance(walletDto.getBtcBalance())
        .build();
  }
}
