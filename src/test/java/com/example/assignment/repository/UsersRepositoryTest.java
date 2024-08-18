package com.example.assignment.repository;

import com.example.assignment.entity.Users;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // Step 2: Disable DataSource replacement
class UsersRepositoryTest {

    // Step 3: Setup PostgreSQL container using Testcontainers
    @SuppressWarnings("resource")
    @Container
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
    @Autowired
    private UsersRepository usersRepository;

    private Users sampleUser;

    // Step 3: Configure Spring Boot to use the PostgreSQL container properties
    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        sampleUser = new Users();
        sampleUser.setName("Jack Lee");
        sampleUser.setEmail("jacklee@example.com");
        sampleUser.setPassword("encodedPassword");
    }

    @Test
    void testCreateUser() {
        Users savedUser = usersRepository.save(sampleUser);
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("jacklee@example.com");
    }

    @Test
    void testFindUserById() {
        Users savedUser = usersRepository.save(sampleUser);
        Optional<Users> foundUser = usersRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("jacklee@example.com");
    }

    @Test
    void testFindByEmail() {
        usersRepository.save(sampleUser);
        Optional<Users> foundUser = usersRepository.findByEmail("jacklee@example.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Jack Lee");
    }

    @Test
    void testUpdateUser() {
        Users savedUser = usersRepository.save(sampleUser);
        savedUser.setName("Jack Lee Updated");
        Users updatedUser = usersRepository.save(savedUser);

        assertThat(updatedUser.getName()).isEqualTo("Jack Lee Updated");
    }

    @Test
    void testDeleteUser() {
        Users savedUser = usersRepository.save(sampleUser);
        usersRepository.deleteById(savedUser.getId());

        Optional<Users> deletedUser = usersRepository.findById(savedUser.getId());
        assertThat(deletedUser).isEmpty();
    }
}
