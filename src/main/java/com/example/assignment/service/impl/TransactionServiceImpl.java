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

        BTCPriceHistory currentPriceHistory = btcPriceHistoryRepository.findTopByOrderByTimestampDesc()
                .orElseThrow(() -> new IllegalArgumentException("No BTC price history found"));

        Wallet wallet = user.getWallet();
        Transaction transaction = new Transaction(user, currentPriceHistory, request.getBtcAmount(),
                transactionType);

        // Perform the transaction and update the wallet balance
        performTransaction(transaction, wallet);

        // Save the transaction (user is saved due to cascading)
        transactionRepository.save(transaction);

        // Convert to DTO
        return transactionMapper.convertToDto(transaction, wallet.getUsdBalance(), wallet.getBtcBalance());
    }

    @Override
    public Page<TransactionDTO> getUserTransactionHistory(Long userId, Pageable pageable) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return transactionRepository.findByUsers(user, pageable)
                .map(transaction -> {
                    Wallet wallet = transaction.getUsers().getWallet();
                    return transactionMapper.convertToDto(transaction, wallet.getUsdBalance(), wallet.getBtcBalance());
                });
    }

    /**
     * Handles the business logic for performing a transaction, updating the user's wallet balance.
     *
     * @param transaction the transaction to be performed.
     * @param wallet      the wallet to update.
     */
    private void performTransaction(Transaction transaction, Wallet wallet) {
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
}
