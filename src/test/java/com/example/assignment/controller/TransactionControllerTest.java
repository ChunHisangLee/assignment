package com.example.assignment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.assignment.dto.CreateTransactionRequestDto;
import com.example.assignment.dto.TransactionDto;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.service.TransactionService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

  private MockMvc mockMvc;

  @Mock private TransactionService transactionService;

  @InjectMocks private TransactionController transactionController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
  }

  @Test
  void testBuyBtc() throws Exception {
    // Arrange
    TransactionDto transactionDTO = new TransactionDto();
    transactionDTO.setId(1L);

    when(transactionService.createTransaction(
            any(CreateTransactionRequestDto.class), eq(TransactionType.BUY)))
        .thenReturn(transactionDTO);

    // Act & Assert
    mockMvc
        .perform(
            post("/api/transactions/buy")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\": 1, \"btcAmount\": 0.5}"))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"id\":1}"));

    verify(transactionService, times(1))
        .createTransaction(any(CreateTransactionRequestDto.class), eq(TransactionType.BUY));
  }

  @Test
  void testSellBtc() throws Exception {
    // Arrange
    TransactionDto transactionDTO = new TransactionDto();
    transactionDTO.setId(1L);

    when(transactionService.createTransaction(
            any(CreateTransactionRequestDto.class), eq(TransactionType.SELL)))
        .thenReturn(transactionDTO);

    // Act & Assert
    mockMvc
        .perform(
            post("/api/transactions/sell")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\": 1, \"btcAmount\": 0.5}"))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"id\":1}"));

    verify(transactionService, times(1))
        .createTransaction(any(CreateTransactionRequestDto.class), eq(TransactionType.SELL));
  }

  @Test
  void testGetUserTransactionHistory() throws Exception {
    // Arrange
    Long userId = 1L;
    TransactionDto transactionDto1 = new TransactionDto();
    transactionDto1.setId(1L);
    TransactionDto transactionDto2 = new TransactionDto();
    transactionDto2.setId(2L);

    List<TransactionDto> transactionDtoList = Arrays.asList(transactionDto1, transactionDto2);
    Pageable pageable = PageRequest.of(0, 20);
    Page<TransactionDto> page =
        new PageImpl<>(transactionDtoList, pageable, transactionDtoList.size());

    when(transactionService.getUserTransactionHistory(userId, pageable)).thenReturn(page);

    // Act & Assert
    mockMvc
        .perform(
            get("/api/transactions/history/{userId}", userId)
                .param("page", "0")
                .param("size", "20"))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"content\":[{\"id\":1},{\"id\":2}]}"));

    verify(transactionService, times(1)).getUserTransactionHistory(userId, pageable);
  }
}
