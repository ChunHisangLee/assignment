package com.example.assignment.service.impl;

import com.example.assignment.entity.BTCPriceHistory;
import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.entity.Users;
import com.example.assignment.repository.BTCPriceHistoryRepository;
import com.example.assignment.repository.TransactionRepository;
import com.example.assignment.repository.UsersRepository;
import com.example.assignment.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UsersRepository usersRepository;
    private final BTCPriceHistoryRepository btcPriceHistoryRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UsersRepository usersRepository, BTCPriceHistoryRepository btcPriceHistoryRepository) {
        this.transactionRepository = transactionRepository;
        this.usersRepository = usersRepository;
        this.btcPriceHistoryRepository = btcPriceHistoryRepository;
    }

    @Override
    public Transaction createTransaction(Long userId, double btcAmount, TransactionType transactionType) {
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Get the latest BTC price from BTCPriceHistory
        BTCPriceHistory currentPriceHistory = btcPriceHistoryRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new IllegalArgumentException("No BTC price history found"));

        Transaction transaction = new Transaction(users, currentPriceHistory, btcAmount, LocalDateTime.now(), transactionType);

        // Perform the transaction (this will update the user's wallet balance)
        transaction.performTransaction();

        // Save the updated user and the new transaction
        usersRepository.save(users);
        return transactionRepository.save(transaction);
    }

    @Override
    public Page<Transaction> getUserTransactionHistory(Long userId, Pageable pageable) {
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return transactionRepository.findByUsers(users, pageable);
    }

}
