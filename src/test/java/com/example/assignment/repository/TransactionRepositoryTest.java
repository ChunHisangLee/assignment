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
        user = Users.builder()
                .name("Jack Lee")
                .email("jacklee@example.com")
                .password("encodedPassword")
                .build();
        user = usersRepository.save(user);

        btcPriceHistory = BTCPriceHistory.builder()
                .price(450.0)
                .timestamp(LocalDateTime.now())
                .build();
        btcPriceHistory = btcPriceHistoryRepository.save(btcPriceHistory);
    }

    @Test
    void testFindByUsers_WithTransactions() {
        // Create and save transactions for the user
        Transaction transaction1 = Transaction.builder()
                .users(user)
                .btcPriceHistory(btcPriceHistory)
                .btcAmount(0.01)
                .transactionTime(LocalDateTime.now())
                .transactionType(TransactionType.BUY)
                .build();
        transactionRepository.save(transaction1);

        Transaction transaction2 = Transaction.builder()
                .users(user)
                .btcPriceHistory(btcPriceHistory)
                .btcAmount(0.02)
                .transactionTime(LocalDateTime.now())
                .transactionType(TransactionType.SELL)
                .build();
        transactionRepository.save(transaction2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> page = transactionRepository.findByUsers(user, pageable);

        // Assertions
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).contains(transaction1, transaction2);
        assertThat(page.getContent()).allMatch(t -> t.getUsers().equals(user));
    }

    @Test
    void testFindByUsers_NoTransactions() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> page = transactionRepository.findByUsers(user, pageable);

        // Assertions
        assertThat(page.getTotalElements()).isZero();
        assertThat(page.getContent()).isEmpty();
    }
}
