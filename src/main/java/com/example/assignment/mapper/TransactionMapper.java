package com.example.assignment.mapper;

import com.example.assignment.dto.BTCPriceHistoryDTO;
import com.example.assignment.dto.TransactionDTO;
import com.example.assignment.dto.UsersDTO;
import com.example.assignment.entity.BTCPriceHistory;
import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDTO convertToDto(Transaction transaction, double usdBalanceBefore, double btcBalanceBefore) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setBtcAmount(transaction.getBtcAmount());
        dto.setTransactionTime(transaction.getTransactionTime());
        dto.setTransactionType(transaction.getTransactionType()); // Use enum directly

        // Map the BTC price history to its DTO
        dto.setBtcPriceHistory(mapToBTCPriceHistoryDTO(transaction.getBtcPriceHistory()));

        // Map the user details to its DTO
        dto.setUsers(mapToUsersDTO(transaction.getUsers()));

        // Set the balances before and after the transaction
        dto.setUsdBalanceBefore(usdBalanceBefore);
        dto.setBtcBalanceBefore(btcBalanceBefore);
        dto.setUsdBalanceAfter(transaction.getUsers().getWallet().getUsdBalance());
        dto.setBtcBalanceAfter(transaction.getUsers().getWallet().getBtcBalance());

        return dto;
    }

    private BTCPriceHistoryDTO mapToBTCPriceHistoryDTO(BTCPriceHistory btcPriceHistory) {
        return new BTCPriceHistoryDTO(
                btcPriceHistory.getId(),
                btcPriceHistory.getPrice(),
                btcPriceHistory.getTimestamp()
        );
    }

    private UsersDTO mapToUsersDTO(Users users) {
        return new UsersDTO(
                users.getId(),
                users.getName(),
                users.getEmail()
        );
    }
}
