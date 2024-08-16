package com.example.assignment.service.impl;

import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.entity.Users;
import com.example.assignment.repository.TransactionRepository;
import com.example.assignment.repository.UserRepository;
import com.example.assignment.service.PriceService;
import com.example.assignment.service.TransactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final PriceService priceService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository, PriceService priceService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.priceService = priceService;
    }

    @Override
    public Transaction createTransaction(Long userId, double btcAmount, TransactionType transactionType) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Users not found"));
        double currentPrice = priceService.getPrice();
        double totalValue = currentPrice * btcAmount;

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
        transaction.setPriceAtTransaction(currentPrice);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setTransactionType(transactionType);
        userRepository.save(users);
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getUserTransactionHistory(Long userId) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Users not found"));
        return transactionRepository.findByUsers(users);
    }
}
