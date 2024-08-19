package com.example.assignment.mapper;

import com.example.assignment.dto.WalletDTO;
import com.example.assignment.entity.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public WalletDTO convertToDto(Wallet wallet) {
        return new WalletDTO(wallet.getId(), wallet.getUsdBalance(), wallet.getBtcBalance());
    }
}
