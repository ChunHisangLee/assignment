package com.example.assignment.schedule;

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
    private ScheduledTasks scheduledTasks;

    @BeforeEach
    void setUp() {
        priceService = mock(PriceService.class);
        scheduledTasks = new ScheduledTasks(priceService);
    }

    @Test
    void testPriceIncreasesUntilMaxThenDecreases() {
        ReflectionTestUtils.setField(scheduledTasks, "currentPrice", 100);
        ReflectionTestUtils.setField(scheduledTasks, "isIncreasing", true);

        for (int i = 0; i < 36; i++) {
            scheduledTasks.updateCurrentPrice();
        }

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(priceService, times(36)).setPrice(captor.capture());

        assertEquals(460, captor.getAllValues().get(35));
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

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(priceService, times(36)).setPrice(captor.capture());

        assertEquals(100, captor.getAllValues().get(35));
        assertEquals(100, scheduledTasks.getCurrentPrice());
        assertTrue(scheduledTasks.isIncreasing());
    }

    @Test
    void testScheduledAnnotationIsPresent() throws NoSuchMethodException {
        Scheduled scheduled = ScheduledTasks.class.getMethod("updateCurrentPrice").getAnnotation(Scheduled.class);
        assertEquals(5000, scheduled.fixedRate());
    }
}
