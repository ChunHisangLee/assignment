package com.example.assignment.mapper;

import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.TradeDirection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class HistoryMapperTest {

    @Autowired
    private HistoryMapper historyMapper;

    private static final int TEST_COIN_ID = 5;
    private static final String TEST_USER_ID = "****************";

    @Test
    public void insert() {
        Transaction history = new Transaction();
        history.setHistoryId(TEST_USER_ID);
        history.setUserId("****");
        history.setCoinId(TEST_COIN_ID);
        history.setTransPrice(BigDecimal.valueOf(210));
        history.setTransType(String.valueOf(TradeDirection.BUY));
        history.setTransQuantity(BigDecimal.valueOf(2.5));
        history.setBeforeBalance(BigDecimal.valueOf(1.2));
        history.setAfterBalance(history.getBeforeBalance().add(BigDecimal.valueOf(2.5)));
        history.setBeforeBalanceUSD(BigDecimal.valueOf(1000));
        history.setAfterBalanceUSD(history.getBeforeBalanceUSD().subtract(BigDecimal.valueOf(210).multiply(BigDecimal.valueOf(2.5))));
        Instant instant = Instant.now();
        history.setTransTime(Date.from(instant));

        Integer rows = historyMapper.insert(history);

        assertNotNull(rows);
        assertTrue(rows > 0);
    }

    @Test
    public void findByUserId() {
        List<Transaction> historyList = historyMapper.findByUserId(TEST_USER_ID);

        assertNotNull(historyList);
        assertFalse(historyList.isEmpty());
    }
}
