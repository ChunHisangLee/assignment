package com.example.assignment.service.impl;

import com.example.assignment.entity.BTCPriceHistory;
import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.entity.Users;
import com.example.assignment.repository.BTCPriceHistoryRepository;
import com.example.assignment.repository.TransactionRepository;
import com.example.assignment.repository.UserRepository;
import com.example.assignment.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BTCPriceHistoryRepository btcPriceHistoryRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository, BTCPriceHistoryRepository btcPriceHistoryRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.btcPriceHistoryRepository = btcPriceHistoryRepository;
    }

    @Override
    public Transaction createTransaction(Long userId, double btcAmount, TransactionType transactionType) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Get the latest BTC price from BTCPriceHistory
        BTCPriceHistory currentPriceHistory = btcPriceHistoryRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new IllegalArgumentException("No BTC price history found"));

        double totalValue = currentPriceHistory.getPrice() * btcAmount;

        if (transactionType == TransactionType.BUY) {
            if (users.getWallet().getUsdBalance() < totalValue) {
                throw new IllegalArgumentException("Insufficient USD balance");
            }

            users.getWallet().setUsdBalance(users.getWallet().getUsdBalance() - totalValue);
            users.getWallet().setBtcBalance(users.getWallet().getBtcBalance() + btcAmount);
        } else if (transactionType == TransactionType.SELL) {
            if (users.getWallet().getBtcBalance() < btcAmount) {
                throw new IllegalArgumentException("Insufficient BTC balance");
            }

            users.getWallet().setBtcBalance(users.getWallet().getBtcBalance() - btcAmount);
            users.getWallet().setUsdBalance(users.getWallet().getUsdBalance() + totalValue);
        }

        Transaction transaction = new Transaction();
        transaction.setUsers(users);
        transaction.setBtcAmount(btcAmount);
        transaction.setBtcPriceHistory(currentPriceHistory); // Associate the transaction with the current BTC price history
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setTransactionType(transactionType);

        userRepository.save(users);
        return transactionRepository.save(transaction);
    }

    @Override
    public Page<Transaction> getUserTransactionHistory(Long userId, Pageable pageable) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return transactionRepository.findByUsers(users, pageable);
    }

}
