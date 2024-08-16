package com.example.assignment.service.impl;

import com.example.assignment.entity.Transaction;
import com.example.assignment.mapper.HistoryMapper;
import com.example.assignment.service.exception.InsertException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class HistoryServiceImplTest {

    @InjectMocks
    private HistoryServiceImpl historyService;

    @Mock
    private HistoryMapper historyMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateHistory() {
        Transaction history = new Transaction();
        when(historyMapper.insert(history)).thenReturn(1);

        historyService.createHistory(history);

        verify(historyMapper, times(1)).insert(history);
    }

    @Test
    public void testCreateHistoryInsertException() {
        Transaction history = new Transaction();

        when(historyMapper.insert(history)).thenReturn(0);

        assertThrows(InsertException.class, () -> historyService.createHistory(history));
    }

    @Test
    public void testGetHistories() {
        Transaction history = new Transaction();
        history.setUserId("testUser");

        when(historyMapper.findByUserId("testUser")).thenReturn(Collections.singletonList(history));

        List<Transaction> histories = historyService.getHistories("testUser");

        assertEquals(1, histories.size());
        assertEquals(history, histories.get(0));
    }

    @Test
    public void testDeleteHistory() {
        when(historyMapper.deleteHistory("testUser")).thenReturn(1);

        Integer result = historyService.deleteHistory("testUser");

        assertEquals(Integer.valueOf(1), result);
    }
}
