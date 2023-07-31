package com.example.assignment.mapper;

import com.example.assignment.entity.Account;
import com.example.assignment.entity.Coin;
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

    @Test
    public void findByKey() {
        User user = new User();
        user.setUserId("***************");
        Coin coin = new Coin();
        coin.setCoinId(1);
        System.out.println(accountMapper.findByKey(user.getUserId(), coin.getCoinId()));
    }

    @Test
    public void findByUserId() {
        User user = new User();
        user.setUserId("***************");
        List<Account> accountList = accountMapper.findByUserId(user.getUserId());
        for (Account account : accountList) {
            System.out.println(account);
        }
    }
}
