package com.example.assignment.controller;

import com.example.assignment.dto.CreateTransactionRequest;
import com.example.assignment.dto.TransactionDTO;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.service.TransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private AutoCloseable closeable; // Declare AutoCloseable for mocks

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this); // Initialize mocks and keep reference
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close(); // Ensure mocks are closed
    }

    @Test
    void testBuyBtc() {
        // Arrange
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setUserId(1L);
        request.setBtcAmount(0.1);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(1L);
        transactionDTO.setBtcAmount(0.1);

        when(transactionService.createTransaction(any(CreateTransactionRequest.class), any(TransactionType.class)))
                .thenReturn(transactionDTO);

        // Act
        ResponseEntity<TransactionDTO> response = transactionController.buyBtc(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionDTO, response.getBody());
    }

    @Test
    void testSellBtc() {
        // Arrange
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setUserId(1L);
        request.setBtcAmount(0.1);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(1L);
        transactionDTO.setBtcAmount(0.1);

        when(transactionService.createTransaction(any(CreateTransactionRequest.class), any(TransactionType.class)))
                .thenReturn(transactionDTO);

        // Act
        ResponseEntity<TransactionDTO> response = transactionController.sellBtc(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionDTO, response.getBody());
    }

    @Test
    void testGetUserTransactionHistory() {
        // Arrange
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 20); // Use Pageable correctly

        TransactionDTO transaction1 = new TransactionDTO();
        transaction1.setId(1L);
        transaction1.setBtcAmount(0.1);

        TransactionDTO transaction2 = new TransactionDTO();
        transaction2.setId(2L);
        transaction2.setBtcAmount(0.2);

        List<TransactionDTO> transactionList = Arrays.asList(transaction1, transaction2);
        Page<TransactionDTO> page = new PageImpl<>(transactionList);

        when(transactionService.getUserTransactionHistory(anyLong(), any(Pageable.class))).thenReturn(page);

        // Act
        ResponseEntity<Page<TransactionDTO>> response = transactionController.getUserTransactionHistory(userId, 0, 20);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }
}
