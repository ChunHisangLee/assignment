package com.example.assignment.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.assignment.entity.BTCPriceHistory;
import com.example.assignment.repository.BTCPriceHistoryRepository;
import com.example.assignment.service.PriceService;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ScheduledTasksTest {

  @Mock private PriceService priceService;

  @Mock private BTCPriceHistoryRepository btcPriceHistoryRepository;

  @InjectMocks private ScheduledTasks scheduledTasks;

  @Captor private ArgumentCaptor<BTCPriceHistory> priceHistoryCaptor;

  @BeforeEach
  void setUp() {
    // Set the initial currentPrice to BigDecimal 100 and isIncreasing to true.
    ReflectionTestUtils.setField(scheduledTasks, "currentPrice", BigDecimal.valueOf(100));
    ReflectionTestUtils.setField(scheduledTasks, "isIncreasing", true);

    // Reset mocks to avoid unwanted interactions
    reset(priceService, btcPriceHistoryRepository);
  }

  @Test
  void shouldIncreasePriceUpToMaxAndThenDecrease() {
    // Simulate 36 invocations which should increase the price from 100 to 460.
    for (int i = 0; i < 36; i++) {
      scheduledTasks.updateCurrentPrice();
    }

    // Verify that the repository's save method is called 36 times
    verify(btcPriceHistoryRepository, times(36)).save(priceHistoryCaptor.capture());
    // Verify that the priceService.setPrice method was also called 36 times with any BigDecimal
    verify(priceService, times(36)).setPrice(any(BigDecimal.class));

    // The last recorded price should be 460
    BTCPriceHistory lastInvocation = priceHistoryCaptor.getAllValues().get(35);
    assertThat(lastInvocation.getPrice()).isEqualTo(BigDecimal.valueOf(460));

    // Next update should decrease the price by 10 (from 460 to 450)
    scheduledTasks.updateCurrentPrice();
    verify(btcPriceHistoryRepository, times(37)).save(priceHistoryCaptor.capture());

    BTCPriceHistory decreaseInvocation = priceHistoryCaptor.getValue();
    assertThat(decreaseInvocation.getPrice()).isEqualTo(BigDecimal.valueOf(450));
  }

  @Test
  void shouldDecreasePriceDownToMinAndThenIncrease() {
    // Set the initial conditions to start decreasing: currentPrice at 460 and isIncreasing = false.
    ReflectionTestUtils.setField(scheduledTasks, "currentPrice", BigDecimal.valueOf(460));
    ReflectionTestUtils.setField(scheduledTasks, "isIncreasing", false);

    // Simulate 36 invocations which should decrease the price from 460 to 100.
    for (int i = 0; i < 36; i++) {
      scheduledTasks.updateCurrentPrice();
    }

    // Verify that the repository's save method is called 36 times
    verify(btcPriceHistoryRepository, times(36)).save(priceHistoryCaptor.capture());
    // Verify that the priceService.setPrice method was called 36 times with any BigDecimal
    verify(priceService, times(36)).setPrice(any(BigDecimal.class));

    // The last recorded price should be 100
    BTCPriceHistory lastInvocation = priceHistoryCaptor.getAllValues().get(35);
    assertThat(lastInvocation.getPrice()).isEqualTo(BigDecimal.valueOf(100));

    // Next update should increase the price by 10 (from 100 to 110)
    scheduledTasks.updateCurrentPrice();
    verify(btcPriceHistoryRepository, times(37)).save(priceHistoryCaptor.capture());

    BTCPriceHistory increaseInvocation = priceHistoryCaptor.getValue();
    assertThat(increaseInvocation.getPrice()).isEqualTo(BigDecimal.valueOf(110));
  }
}
