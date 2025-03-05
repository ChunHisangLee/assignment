package com.example.assignment.controller;

import static com.example.assignment.constants.PaginationConstants.DEFAULT_PAGE_NUMBER;
import static com.example.assignment.constants.PaginationConstants.DEFAULT_PAGE_SIZE;

import com.example.assignment.dto.CreateTransactionRequestDto;
import com.example.assignment.dto.TransactionDto;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/transactions")
public class TransactionController {

  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping("/buy")
  public ResponseEntity<TransactionDto> buyBtc(@RequestBody CreateTransactionRequestDto request) {
    log.info("Initiating BTC buy transaction for user ID: {}", request.getUserId());

    TransactionDto transactionDto =
        transactionService.createTransaction(request, TransactionType.BUY);

    log.info("BTC buy transaction completed for user ID: {}", request.getUserId());
    return ResponseEntity.ok(transactionDto);
  }

  @PostMapping("/sell")
  public ResponseEntity<TransactionDto> sellBtc(@RequestBody CreateTransactionRequestDto request) {
    log.info("Initiating BTC sell transaction for user ID: {}", request.getUserId());

    TransactionDto transactionDto =
        transactionService.createTransaction(request, TransactionType.SELL);

    log.info("BTC sell transaction completed for user ID: {}", request.getUserId());
    return ResponseEntity.ok(transactionDto);
  }

  @GetMapping("/history/{userId}")
  public ResponseEntity<Page<TransactionDto>> getUserTransactionHistory(
      @PathVariable Long userId,
      @RequestParam(defaultValue = "" + DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int size) {

    log.info(
        "Fetching transaction history for user ID: {} with page number: {} and size: {}",
        userId,
        page,
        size);

    Pageable pageable = PageRequest.of(page, size);
    Page<TransactionDto> transactions =
        transactionService.getUserTransactionHistory(userId, pageable);

    log.info("Fetched {} transactions for user ID: {}", transactions.getTotalElements(), userId);
    return ResponseEntity.ok(transactions);
  }
}
