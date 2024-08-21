package com.example.assignment.service.impl;

import com.example.assignment.dto.CreateTransactionRequest;
import com.example.assignment.dto.TransactionDTO;
import com.example.assignment.entity.*;
import com.example.assignment.mapper.TransactionMapper;
import com.example.assignment.repository.TransactionRepository;
import com.example.assignment.repository.UsersRepository;
import com.example.assignment.service.PriceService;
import com.example.assignment.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final UsersRepository usersRepository;
    private final TransactionMapper transactionMapper;
    private final PriceService priceService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UsersRepository usersRepository,
                                  TransactionMapper transactionMapper, PriceService priceService) {
        this.transactionRepository = transactionRepository;
        this.usersRepository = usersRepository;
        this.transactionMapper = transactionMapper;
        this.priceService = priceService;
    }

    @Override
    @Transactional
    public TransactionDTO createTransaction(CreateTransactionRequest request, TransactionType transactionType) {
        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found", request.getUserId());
                    return new IllegalArgumentException("User not found");
                });

        int currentPrice = priceService.getPrice();
        logger.info("Fetched current BTC price: {}", currentPrice);

        BTCPriceHistory currentPriceHistory = BTCPriceHistory.builder()
                .price(currentPrice)
                .build();

        Wallet wallet = user.getWallet();
        Transaction transaction = Transaction.builder()
                .users(user)
                .btcPriceHistory(currentPriceHistory)
                .btcAmount(request.getBtcAmount())
                .transactionType(transactionType)
                .build();

        logger.info("Performing {} transaction for user with ID {}", transactionType, user.getId());

        performTransaction(transaction, wallet);

        transactionRepository.save(transaction);
        logger.info("Transaction saved for user with ID {}", user.getId());

        return transactionMapper.toDto(transaction, wallet.getUsdBalance(), wallet.getBtcBalance());
    }

    @Override
    public Page<TransactionDTO> getUserTransactionHistory(Long userId, Pageable pageable) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return transactionRepository.findByUsers(user, pageable)
                .map(transaction -> {
                    Wallet wallet = transaction.getUsers().getWallet();
                    return transactionMapper.toDto(transaction, wallet.getUsdBalance(), wallet.getBtcBalance());
                });
    }

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
