package com.example.assignment.controller;

import com.example.assignment.dto.CreateTransactionRequest;
import com.example.assignment.dto.TransactionDTO;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void testBuyBtc() throws Exception {
        // Arrange
        CreateTransactionRequest request = new CreateTransactionRequest(1L, 0.5);
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(1L);
        when(transactionService.createTransaction(eq(request), eq(TransactionType.BUY))).thenReturn(transactionDTO);

        // Act & Assert
        mockMvc.perform(post("/api/transactions/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 1, \"btcAmount\": 0.5}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));

        verify(transactionService, times(1)).createTransaction(eq(request), eq(TransactionType.BUY));
    }

    @Test
    void testSellBtc() throws Exception {
        // Arrange
        CreateTransactionRequest request = new CreateTransactionRequest(1L, 0.5);
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(1L);
        when(transactionService.createTransaction(eq(request), eq(TransactionType.SELL))).thenReturn(transactionDTO);

        // Act & Assert
        mockMvc.perform(post("/api/transactions/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 1, \"btcAmount\": 0.5}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));

        verify(transactionService, times(1)).createTransaction(eq(request), eq(TransactionType.SELL));
    }

    @Test
    void testGetUserTransactionHistory() throws Exception {
        // Arrange
        Long userId = 1L;
        TransactionDTO transactionDTO1 = new TransactionDTO();
        transactionDTO1.setId(1L);
        TransactionDTO transactionDTO2 = new TransactionDTO();
        transactionDTO2.setId(2L);

        List<TransactionDTO> transactionDTOList = Arrays.asList(transactionDTO1, transactionDTO2);
        Page<TransactionDTO> page = new PageImpl<>(transactionDTOList);
        Pageable pageable = PageRequest.of(0, 10);

        when(transactionService.getUserTransactionHistory(eq(userId), eq(pageable))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/transactions/history/{userId}", userId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[{\"id\":1},{\"id\":2}]}"));

        verify(transactionService, times(1)).getUserTransactionHistory(eq(userId), eq(pageable));
    }
}
