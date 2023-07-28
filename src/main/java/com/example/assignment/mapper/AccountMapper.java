package com.example.assignment.mapper;

import com.example.assignment.entity.Account;

import java.time.Instant;


public interface AccountMapper {
    /**
     * Insert account data
     *
     * @param account account data
     * @return the row to be inserted
     */
    Integer insert(Account account);

    /**
     * Set the initial net value of USD
     *
     * @param account  setting time
     * @return the row to be updated
     */

    Integer setUSDNetValue(Account account);

    /**
     * Query the account data
     *
     * @param userId user ID
     * @param coinId coin ID
     * @return Account
     */
    Account findByKey(Integer userId, Integer coinId);
    /**
     * Update the Trading balance
     *
     * @param account  setting time
     * @return the row to be updated
     */

    Integer updateTradingBalance(Account account);
}
