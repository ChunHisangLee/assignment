package com.example.assignment.controller;

import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.TransactionType;
import com.example.assignment.entity.Users;
import com.example.assignment.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@WithMockUser 
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private Transaction sampleTransaction;

    @BeforeEach
    void setUp() {
        Users sampleUser = new Users();
        sampleUser.setId(1L);
        sampleUser.setName("Jack Lee");
        sampleUser.setEmail("jacklee@example.com");
        sampleUser.setPassword("password");

        sampleTransaction = new Transaction();
        sampleTransaction.setId(1L);
        sampleTransaction.setUsers(sampleUser);
        sampleTransaction.setBtcAmount(0.5);
        sampleTransaction.setPriceAtTransaction(20000.0);
        sampleTransaction.setTransactionTime(LocalDateTime.now());
        sampleTransaction.setTransactionType(TransactionType.BUY);
    }

    @Test
    void testBuyBtc() throws Exception {
        when(transactionService.createTransaction(ArgumentMatchers.anyLong(), ArgumentMatchers.anyDouble(), ArgumentMatchers.eq(TransactionType.BUY)))
                .thenReturn(sampleTransaction);

        ResultActions resultActions = mockMvc.perform(post("/api/transactions/buy")
                .param("userId", "1")
                .param("btcAmount", "0.5")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.users.id").value(1L))
                .andExpect(jsonPath("$.btcAmount").value(0.5))
                .andExpect(jsonPath("$.transactionType").value("BUY"))
                .andExpect(jsonPath("$.priceAtTransaction").value(20000.0))
                .andExpect(jsonPath("$.transactionTime").exists());
    }

    @Test
    void testSellBtc() throws Exception {
        sampleTransaction.setTransactionType(TransactionType.SELL);

        when(transactionService.createTransaction(ArgumentMatchers.anyLong(), ArgumentMatchers.anyDouble(), ArgumentMatchers.eq(TransactionType.SELL)))
                .thenReturn(sampleTransaction);

        ResultActions resultActions = mockMvc.perform(post("/api/transactions/sell")
                .param("userId", "1")
                .param("btcAmount", "0.5")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.users.id").value(1L))
                .andExpect(jsonPath("$.btcAmount").value(0.5))
                .andExpect(jsonPath("$.transactionType").value("SELL"))
                .andExpect(jsonPath("$.priceAtTransaction").value(20000.0))
                .andExpect(jsonPath("$.transactionTime").exists());
    }

    @Test
    void testGetUserTransactionHistory() throws Exception {
        when(transactionService.getUserTransactionHistory(ArgumentMatchers.anyLong()))
                .thenReturn(Collections.singletonList(sampleTransaction));

        ResultActions resultActions = mockMvc.perform(get("/api/transactions/history/1")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].users.id").value(1L))
                .andExpect(jsonPath("$[0].btcAmount").value(0.5))
                .andExpect(jsonPath("$[0].transactionType").value("BUY"))
                .andExpect(jsonPath("$[0].priceAtTransaction").value(20000.0))
                .andExpect(jsonPath("$[0].transactionTime").exists());
    }
}
