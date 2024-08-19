package com.example.assignment.controller;

import com.example.assignment.dto.CreateTransactionRequest;
import com.example.assignment.dto.TransactionDTO;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.assignment.constants.PaginationConstants.DEFAULT_PAGE_NUMBER;
import static com.example.assignment.constants.PaginationConstants.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/buy")
    public ResponseEntity<TransactionDTO> buyBtc(@RequestBody CreateTransactionRequest request) {
        TransactionDTO transactionDTO = transactionService.createTransaction(request, TransactionType.BUY);
        return ResponseEntity.ok(transactionDTO);
    }

    @PostMapping("/sell")
    public ResponseEntity<TransactionDTO> sellBtc(@RequestBody CreateTransactionRequest request) {
        TransactionDTO transactionDTO = transactionService.createTransaction(request, TransactionType.SELL);
        return ResponseEntity.ok(transactionDTO);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<Page<TransactionDTO>> getUserTransactionHistory(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "" + DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionDTO> transactions = transactionService.getUserTransactionHistory(userId, pageable);
        return ResponseEntity.ok(transactions);
    }
}
