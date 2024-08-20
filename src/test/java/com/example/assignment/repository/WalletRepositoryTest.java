package com.example.assignment.repository;

import com.example.assignment.entity.Users;
import com.example.assignment.entity.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WalletRepositoryTest {

    @Container
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UsersRepository usersRepository;

    private Users sampleUser;
    private Wallet sampleWallet;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        sampleUser = createSampleUser();
        usersRepository.save(sampleUser);

        sampleWallet = createSampleWallet(sampleUser);
    }

    private Users createSampleUser() {
        return Users.builder()
                .name("Jack Lee")
                .email("jacklee@example.com")
                .password("encodedPassword")
                .build();
    }

    private Wallet createSampleWallet(Users user) {
        return Wallet.builder()
                .usdBalance(1000.0)
                .btcBalance(0.5)
                .users(user)
                .build();
    }

    private Wallet saveSampleWallet() {
        return walletRepository.save(sampleWallet);
    }

    @Test
    void shouldCreateWalletSuccessfully() {
        Wallet savedWallet = saveSampleWallet();
        assertThat(savedWallet.getId()).isNotNull();
        assertThat(savedWallet.getUsdBalance()).isEqualTo(1000.0);
        assertThat(savedWallet.getBtcBalance()).isEqualTo(0.5);
        assertThat(savedWallet.getUsers().getId()).isEqualTo(sampleUser.getId());
    }

    @Test
    void shouldFindWalletByIdSuccessfully() {
        Wallet savedWallet = saveSampleWallet();
        Optional<Wallet> foundWallet = walletRepository.findById(savedWallet.getId());
        assertThat(foundWallet).isPresent();
        assertThat(foundWallet.get().getUsdBalance()).isEqualTo(1000.0);
        assertThat(foundWallet.get().getBtcBalance()).isEqualTo(0.5);
        assertThat(foundWallet.get().getUsers().getId()).isEqualTo(sampleUser.getId());
    }

    @Test
    void shouldUpdateWalletSuccessfully() {
        Wallet savedWallet = saveSampleWallet();
        savedWallet.setUsdBalance(2000.0);
        savedWallet.setBtcBalance(1.0);
        Wallet updatedWallet = walletRepository.save(savedWallet);

        assertThat(updatedWallet.getUsdBalance()).isEqualTo(2000.0);
        assertThat(updatedWallet.getBtcBalance()).isEqualTo(1.0);
        assertThat(updatedWallet.getUsers().getId()).isEqualTo(sampleUser.getId());
    }

    @Test
    void shouldDeleteWalletSuccessfully() {
        Wallet savedWallet = saveSampleWallet();
        walletRepository.deleteById(savedWallet.getId());

        Optional<Wallet> deletedWallet = walletRepository.findById(savedWallet.getId());
        assertThat(deletedWallet).isEmpty();
    }
}
