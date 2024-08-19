package com.example.assignment.service.impl;

import com.example.assignment.dto.CreateTransactionRequest;
import com.example.assignment.dto.TransactionDTO;
import com.example.assignment.entity.*;
import com.example.assignment.mapper.TransactionMapper;
import com.example.assignment.repository.BTCPriceHistoryRepository;
import com.example.assignment.repository.TransactionRepository;
import com.example.assignment.repository.UsersRepository;
import com.example.assignment.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UsersRepository usersRepository;
    private final BTCPriceHistoryRepository btcPriceHistoryRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UsersRepository usersRepository,
                                  BTCPriceHistoryRepository btcPriceHistoryRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.usersRepository = usersRepository;
        this.btcPriceHistoryRepository = btcPriceHistoryRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @Transactional
    public TransactionDTO createTransaction(CreateTransactionRequest request, TransactionType transactionType) {
        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Fetch the latest BTC price from BTCPriceHistory
        BTCPriceHistory currentPriceHistory = btcPriceHistoryRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new IllegalArgumentException("No BTC price history found"));

        // Capture balances before the transaction
        Wallet wallet = user.getWallet();
        double usdBalanceBefore = wallet.getUsdBalance();
        double btcBalanceBefore = wallet.getBtcBalance();

        Transaction transaction = new Transaction(user, currentPriceHistory, request.getBtcAmount(),
                LocalDateTime.now(), transactionType);

        // Perform the transaction (this updates the user's wallet balance)
        performTransaction(transaction);

        // Save the transaction (user is saved due to cascading)
        transactionRepository.save(transaction);

        // Convert to DTO, including pre- and post-transaction balances
        return transactionMapper.convertToDto(transaction, usdBalanceBefore, btcBalanceBefore);
    }

    @Override
    public Page<TransactionDTO> getUserTransactionHistory(Long userId, Pageable pageable) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return transactionRepository.findByUsers(user, pageable)
                .map(transaction -> {
                    // Calculate balances before the transaction
                    double usdBalanceBefore = calculateUsdBalanceBefore(transaction);
                    double btcBalanceBefore = calculateBtcBalanceBefore(transaction);

                    return transactionMapper.convertToDto(transaction, usdBalanceBefore, btcBalanceBefore);
                });
    }

    /**
     * Handles the business logic for performing a transaction, updating the user's wallet balance.
     *
     * @param transaction the transaction to be performed.
     */
    private void performTransaction(Transaction transaction) {
        Users user = transaction.getUsers();
        Wallet wallet = user.getWallet();
        double totalAmount = transaction.getBtcAmount() * transaction.getBtcPriceHistory().getPrice();

        if (transaction.getTransactionType() == TransactionType.BUY) {
            if (wallet.getUsdBalance() >= totalAmount) {
                wallet.setUsdBalance(wallet.getUsdBalance() - totalAmount);
                wallet.setBtcBalance(wallet.getBtcBalance() + transaction.getBtcAmount());
            } else {
                throw new IllegalArgumentException("Insufficient USD balance for this transaction.");
            }
        } else if (transaction.getTransactionType() == TransactionType.SELL) {
            if (wallet.getBtcBalance() >= transaction.getBtcAmount()) {
                wallet.setBtcBalance(wallet.getBtcBalance() - transaction.getBtcAmount());
                wallet.setUsdBalance(wallet.getUsdBalance() + totalAmount);
            } else {
                throw new IllegalArgumentException("Insufficient BTC balance for this transaction.");
            }
        }
    }

    /**
     * Calculate the USD balance before the transaction.
     * @param transaction the transaction entity.
     * @return the USD balance before the transaction.
     */
    private double calculateUsdBalanceBefore(Transaction transaction) {
        Wallet wallet = transaction.getUsers().getWallet();
        double totalAmount = transaction.getBtcAmount() * transaction.getBtcPriceHistory().getPrice();

        if (transaction.getTransactionType() == TransactionType.BUY) {
            return wallet.getUsdBalance() + totalAmount;
        } else if (transaction.getTransactionType() == TransactionType.SELL) {
            return wallet.getUsdBalance() - totalAmount;
        }
        return wallet.getUsdBalance();
    }

    /**
     * Calculate the BTC balance before the transaction.
     * @param transaction the transaction entity.
     * @return the BTC balance before the transaction.
     */
    private double calculateBtcBalanceBefore(Transaction transaction) {
        Wallet wallet = transaction.getUsers().getWallet();

        if (transaction.getTransactionType() == TransactionType.BUY) {
            return wallet.getBtcBalance() - transaction.getBtcAmount();
        } else if (transaction.getTransactionType() == TransactionType.SELL) {
            return wallet.getBtcBalance() + transaction.getBtcAmount();
        }
        return wallet.getBtcBalance();
    }
}
