package com.example.assignment.repository;

import com.example.assignment.entity.Transaction;
import com.example.assignment.entity.Users;
import com.example.assignment.entity.BTCPriceHistory;
import com.example.assignment.entity.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransactionRepositoryTest {

    @Container
    @SuppressWarnings("resource")
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BTCPriceHistoryRepository btcPriceHistoryRepository;

    private Users user;
    private BTCPriceHistory btcPriceHistory;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setName("Jack Lee");
        user.setEmail("jacklee@example.com");
        user.setPassword("encodedPassword");
        user = usersRepository.save(user);

        btcPriceHistory = new BTCPriceHistory();
        btcPriceHistory.setPrice(45000.0);
        btcPriceHistory.setTimestamp(LocalDateTime.now());
        btcPriceHistory = btcPriceHistoryRepository.save(btcPriceHistory);
    }

    @Test
    void testFindByUsers() {
        // Save some transactions for the user
        Transaction transaction1 = new Transaction();
        transaction1.setUsers(user);
        transaction1.setBtcPriceHistory(btcPriceHistory);
        transaction1.setBtcAmount(0.01);
        transaction1.setTransactionTime(LocalDateTime.now());
        transaction1.setTransactionType(TransactionType.BUY);
        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setUsers(user);
        transaction2.setBtcPriceHistory(btcPriceHistory);
        transaction2.setBtcAmount(0.02);
        transaction2.setTransactionTime(LocalDateTime.now());
        transaction2.setTransactionType(TransactionType.SELL);
        transactionRepository.save(transaction2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> page = transactionRepository.findByUsers(user, pageable);

        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).contains(transaction1, transaction2);
    }

    @Test
    void testFindByUsers_NoTransactions() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> page = transactionRepository.findByUsers(user, pageable);

        assertThat(page.getTotalElements()).isEqualTo(0);
    }
}
