package com.example.assignment.schedule;

import com.example.assignment.entity.BTCPriceHistory;
import com.example.assignment.repository.BTCPriceHistoryRepository;
import com.example.assignment.service.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScheduledTasksTest {

    private PriceService priceService;
    private BTCPriceHistoryRepository btcPriceHistoryRepository;
    private ScheduledTasks scheduledTasks;

    @BeforeEach
    void setUp() {
        priceService = mock(PriceService.class);
        btcPriceHistoryRepository = mock(BTCPriceHistoryRepository.class);
        scheduledTasks = new ScheduledTasks(priceService, btcPriceHistoryRepository);
    }

    @Test
    void testPriceIncreasesUntilMaxThenDecreases() {
        ReflectionTestUtils.setField(scheduledTasks, "currentPrice", 100);
        ReflectionTestUtils.setField(scheduledTasks, "isIncreasing", true);

        for (int i = 0; i < 36; i++) {
            scheduledTasks.updateCurrentPrice();
        }

        ArgumentCaptor<Integer> priceCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(priceService, times(36)).setPrice(priceCaptor.capture());
        verify(btcPriceHistoryRepository, times(36)).save(any(BTCPriceHistory.class));

        assertEquals(460, priceCaptor.getAllValues().get(35));
        assertEquals(460, scheduledTasks.getCurrentPrice());
        assertFalse(scheduledTasks.isIncreasing());
    }

    @Test
    void testPriceDecreasesUntilMinThenIncreases() {
        ReflectionTestUtils.setField(scheduledTasks, "currentPrice", 460);
        ReflectionTestUtils.setField(scheduledTasks, "isIncreasing", false);

        for (int i = 0; i < 36; i++) {
            scheduledTasks.updateCurrentPrice();
        }

        ArgumentCaptor<Integer> priceCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(priceService, times(36)).setPrice(priceCaptor.capture());
        verify(btcPriceHistoryRepository, times(36)).save(any(BTCPriceHistory.class));

        assertEquals(100, priceCaptor.getAllValues().get(35));
        assertEquals(100, scheduledTasks.getCurrentPrice());
        assertTrue(scheduledTasks.isIncreasing());
    }

    @Test
    void testScheduledAnnotationIsPresent() throws NoSuchMethodException {
        Scheduled scheduled = ScheduledTasks.class.getMethod("updateCurrentPrice").getAnnotation(Scheduled.class);
        assertEquals(5000, scheduled.fixedRate());
    }
}
