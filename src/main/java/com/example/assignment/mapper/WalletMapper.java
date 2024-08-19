package com.example.assignment.mapper;

import com.example.assignment.dto.WalletDTO;
import com.example.assignment.entity.Wallet;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WalletMapper {

    /**
     * Converts a Wallet entity to a WalletDTO.
     *
     * @param wallet the Wallet entity
     * @return the corresponding WalletDTO
     */
    public WalletDTO toDto(Wallet wallet) {
        return Optional.ofNullable(wallet)
                .map(entity -> new WalletDTO(
                        entity.getId(),
                        entity.getUsdBalance(),
                        entity.getBtcBalance()
                ))
                .orElse(null);
    }

    /**
     * Converts a WalletDTO to a Wallet entity.
     *
     * @param walletDTO the WalletDTO
     * @return the corresponding Wallet entity
     */
    public Wallet toEntity(WalletDTO walletDTO) {
        return Optional.ofNullable(walletDTO)
                .map(dto -> new Wallet(
                        dto.getUsdBalance(),
                        dto.getBtcBalance(),
                        null // Assuming the Users reference is set elsewhere
                ))
                .orElse(null);
    }
}
