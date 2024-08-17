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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                .andExpect(jsonPath("$.transactionTime").exists());
    }

    @Test
    void testGetUserTransactionHistory() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Transaction> transactionPage = new PageImpl<>(Collections.singletonList(sampleTransaction), pageable, 1);

        when(transactionService.getUserTransactionHistory(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(transactionPage);

        ResultActions resultActions = mockMvc.perform(get("/api/transactions/history/1")
                .param("page", "0")
                .param("size", "20")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].users.id").value(1L))
                .andExpect(jsonPath("$.content[0].btcAmount").value(0.5))
                .andExpect(jsonPath("$.content[0].transactionType").value("BUY"))
                .andExpect(jsonPath("$.content[0].transactionTime").exists())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.number").value(0));
    }
}
