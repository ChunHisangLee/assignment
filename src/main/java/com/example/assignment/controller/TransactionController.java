package com.example.assignment.controller;

import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<Transaction>> getUserTransactionHistory(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.getUserTransactionHistory(userId);
        return ResponseEntity.ok(transactions);
    }
}
