package com.example.assignment.mapper;

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
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HistoryMapperTest {
    @Autowired
    private HistoryMapper historyMapper;

    @Test
    public void insert() {
        History history = new History();
        history.setHistoryId("*****************");
        history.setUserId("****");
        history.setCoinId(5);
        history.setTransPrice(BigDecimal.valueOf(210));
        history.setTransType(String.valueOf(TradeDirection.BUY));
        history.setTransQuantity(BigDecimal.valueOf(2.5));
        history.setBeforeBalance(BigDecimal.valueOf(1.2));
        history.setAfterBalance(history.getBeforeBalance().add(BigDecimal.valueOf(2.5)));
        history.setBeforeBalanceUSD(BigDecimal.valueOf(1000));
        history.setAfterBalanceUSD(history.getBeforeBalance().add(BigDecimal.valueOf(-1 * 210 * 2.5)));
        Instant instant = Instant.now();
        history.setTransTime(Date.from(instant));
        Integer rows = historyMapper.insert(history);
        System.out.println(rows);
    }

    @Test
    public void findByUserId() {
        User user = new User();
        user.setUserId("****************");
        List<History> historyListList = historyMapper.findByUserId(user.getUserId());
        for (History history : historyListList) {
            System.out.println(history);
        }
    }
}
