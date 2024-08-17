package com.example.assignment.controller;

import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/buy")
    public ResponseEntity<Transaction> buyBtc(@RequestParam Long userId, @RequestParam double btcAmount) {
        Transaction transaction = transactionService.createTransaction(userId, btcAmount, TransactionType.BUY);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/sell")
    public ResponseEntity<Transaction> sellBtc(@RequestParam Long userId, @RequestParam double btcAmount) {
        Transaction transaction = transactionService.createTransaction(userId, btcAmount, TransactionType.SELL);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<Page<Transaction>> getUserTransactionHistory(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionService.getUserTransactionHistory(userId, pageable);
        return ResponseEntity.ok(transactions);
    }
}
