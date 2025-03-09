package com.example.assignment.controller;

import static com.example.assignment.constants.PaginationConstants.DEFAULT_PAGE_NUMBER;
import static com.example.assignment.constants.PaginationConstants.DEFAULT_PAGE_SIZE;

import com.example.assignment.constants.MessagesConstants;
import com.example.assignment.dto.CreateTransactionRequestDto;
import com.example.assignment.dto.TransactionDto;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.response.ErrorResponseDto;
import com.example.assignment.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Transactions", description = "Endpoints for managing transactions")
@RestController
@Slf4j
@RequestMapping("/api/transactions")
@AllArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;

  @Operation(summary = "Buy BTC", description = "REST API to buy BTC")
  @ApiResponses({
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_200,
        description = MessagesConstants.MESSAGE_200),
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_500,
        description = MessagesConstants.MESSAGE_500,
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @PostMapping("/buy")
  public ResponseEntity<TransactionDto> buyBtc(@RequestBody CreateTransactionRequestDto request) {
    log.info("Initiating BTC buy transaction for user ID: {}", request.getUserId());

    TransactionDto transactionDto =
        transactionService.createTransaction(request, TransactionType.BUY);

    log.info("BTC buy transaction completed for user ID: {}", request.getUserId());
    return ResponseEntity.ok(transactionDto);
  }

  @Operation(summary = "Sell BTC", description = "REST API to sell BTC")
  @ApiResponses({
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_200,
        description = MessagesConstants.MESSAGE_200),
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_500,
        description = MessagesConstants.MESSAGE_500,
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @PostMapping("/sell")
  public ResponseEntity<TransactionDto> sellBtc(@RequestBody CreateTransactionRequestDto request) {
    log.info("Initiating BTC sell transaction for user ID: {}", request.getUserId());

    TransactionDto transactionDto =
        transactionService.createTransaction(request, TransactionType.SELL);

    log.info("BTC sell transaction completed for user ID: {}", request.getUserId());
    return ResponseEntity.ok(transactionDto);
  }

  @Operation(
      summary = "Get transaction history",
      description = "REST API to get transaction history")
  @ApiResponses({
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_200,
        description = MessagesConstants.MESSAGE_200),
    @ApiResponse(
        responseCode = MessagesConstants.STATUS_500,
        description = MessagesConstants.MESSAGE_500,
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
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
