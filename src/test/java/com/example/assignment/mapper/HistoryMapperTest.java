package com.example.assignment.mapper;

import com.example.assignment.entity.Trade;
import com.example.assignment.entity.TradeDirection;
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
public class HistoryMapperTest {
    @Autowired
    private HistoryMapper historyMapper;

    @Test
    public void insert() {
        Trade trade = new Trade();
        trade.setUserId(3);
        trade.setCoinId(5);
        trade.setTransPrice(BigDecimal.valueOf(2.1));
        trade.setTransType(String.valueOf(TradeDirection.BUY));
        trade.setTransQuantity(BigDecimal.valueOf(2.5));
        trade.setBeforeBalance(BigDecimal.valueOf(1000));
        trade.setAfterBalance(trade.getBeforeBalance().add(BigDecimal.valueOf(2.1 * 2.5)));
        Instant instant = Instant.now();
        trade.setTransTime(Date.from(instant));
        Integer rows = historyMapper.insert(trade);
        System.out.println(rows);
    }
}
