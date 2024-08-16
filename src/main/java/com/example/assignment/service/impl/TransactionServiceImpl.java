package com.example.assignment.service.impl;

import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.entity.User;
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
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        double currentPrice = priceService.getPrice();
        double totalValue = currentPrice * btcAmount;

        if (transactionType == TransactionType.BUY) {
            if (user.getWallet().getUsdBalance() < totalValue) {
                throw new IllegalArgumentException("Insufficient USD balance");
            }

            user.getWallet().setUsdBalance(user.getWallet().getUsdBalance() - totalValue);
            user.getWallet().setBtcBalance(user.getWallet().getBtcBalance() + btcAmount);
        } else if (transactionType == TransactionType.SELL) {
            if (user.getWallet().getBtcBalance() < btcAmount) {
                throw new IllegalArgumentException("Insufficient BTC balance");
            }

            user.getWallet().setBtcBalance(user.getWallet().getBtcBalance() - btcAmount);
            user.getWallet().setUsdBalance(user.getWallet().getUsdBalance() + totalValue);
        }

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setBtcAmount(btcAmount);
        transaction.setPriceAtTransaction(currentPrice);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setTransactionType(transactionType);
        userRepository.save(user);
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getUserTransactionHistory(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return transactionRepository.findByUser(user);
    }
}
