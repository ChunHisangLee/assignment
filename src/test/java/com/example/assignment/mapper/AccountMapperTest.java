package com.example.assignment.mapper;

import com.example.assignment.entity.Account;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountMapperTest {
    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void insert() {
        Account account = new Account();
        String userId = UUID.randomUUID().toString().toUpperCase();
        account.setUserId(userId);
        account.setCoinId(1);
        account.setAccountStatus("Normal");
        Integer rows = accountMapper.insert(account);
        System.out.println(rows);
    }

    @Test
    public void setUSDNetValue() {
        Instant instant = Instant.now();
        Account account = new Account();
        account.setUserId("******");
        account.setCoinId(1);
        account.setNetValue(BigDecimal.valueOf(1000));
        account.setUpdateTime(Date.from(instant));
        Integer rows = accountMapper.setUSDNetValue(account);
        System.out.println(rows);
    }
}
