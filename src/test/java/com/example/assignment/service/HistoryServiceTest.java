package com.example.assignment.service;

import com.example.assignment.entity.History;
import com.example.assignment.entity.TradeDirection;
import com.example.assignment.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HistoryServiceTest {
    @Autowired
    private IHistoryService historyService;

    @Test
    public void createHistory() {
        History history = new History();
        history.setHistoryId("ABCD-EFGH-IJKLMNOPQRSTUVWXYZ-QWERTYU");
        history.setUserId("****************");
        history.setCoinId(2);
        history.setTransType(String.valueOf(TradeDirection.BUY));
        history.setTransPrice(BigDecimal.valueOf(220));
        history.setTransQuantity(BigDecimal.valueOf(1.25));
        history.setBeforeBalance(BigDecimal.valueOf(1000));
        history.setAfterBalance(BigDecimal.valueOf(880));
        history.setBeforeBalanceUSD(BigDecimal.valueOf(1290));
        history.setAfterBalanceUSD(BigDecimal.valueOf(780));
        Instant instant = Instant.now();
        history.setTransTime(Date.from(instant));
        historyService.createHistory(history);
        System.out.println("OK!!");
    }
    @Test
    public void deleteHistory() {
        User user = new User();
        user.setUserId("***************");
        historyService.deleteHistory(user.getUserId());
        System.out.println("OK!!");
    }
}
