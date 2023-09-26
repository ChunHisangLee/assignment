package com.example.assignment.mapper;

import com.example.assignment.entity.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AccountMapperTest {

    @Autowired
    private AccountMapper accountMapper;

    private static final int TEST_COIN_ID = 1;
    private static final String TEST_USER_ID = "***************";

    @Test
    public void insert() {
        Account account = new Account();
        String userId = UUID.randomUUID().toString().toUpperCase();
        account.setUserId(userId);
        account.setCoinId(TEST_COIN_ID);
        account.setAccountStatus("Normal");

        Integer rows = accountMapper.insert(account);

        assertNotNull(rows);
        assertTrue(rows > 0);
    }

    @Test
    public void setUSDNetValue() {
        Instant instant = Instant.now();
        Account account = new Account();
        account.setUserId(TEST_USER_ID);
        account.setCoinId(TEST_COIN_ID);
        account.setNetValue(BigDecimal.valueOf(1000));
        account.setUpdateTime(Date.from(instant));

        Integer rows = accountMapper.setUSDNetValue(account);

        assertNotNull(rows);
        assertTrue(rows > 0);
    }

    @Test
    public void findByKey() {
        Account result = accountMapper.findByKey(TEST_USER_ID, TEST_COIN_ID);
        assertNotNull(result);
    }

    @Test
    public void findByUserId() {
        List<Account> accountList = accountMapper.findByUserId(TEST_USER_ID);
        assertNotNull(accountList);
        assertFalse(accountList.isEmpty());
    }
}
