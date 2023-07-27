package com.example.assignment.mapper;

import com.example.assignment.entity.Account;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountMapperTest {
    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void insert() {
        Account account = new Account();
        account.setUserId(1);
        account.setCoinId(1);
        account.setAccountStatus("正常");
        Integer rows = accountMapper.insert(account);
        System.out.println(rows);
    }

    @Test
    public void setUSDNetValue() {
        Integer rows = accountMapper.setUSDNetValue(1, 1, 1000);
        System.out.println(rows);
    }
}
