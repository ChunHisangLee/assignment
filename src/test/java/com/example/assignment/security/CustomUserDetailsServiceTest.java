package com.example.assignment.security;

import com.example.assignment.entity.Users;
import com.example.assignment.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {

    private UsersRepository usersRepository;
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        usersRepository = mock(UsersRepository.class);
        customUserDetailsService = new CustomUserDetailsService(usersRepository);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        Users user = new Users();
        user.setEmail("jacklee@example.com");
        user.setPassword("password");
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        org.springframework.security.core.userdetails.UserDetails userDetails =
                customUserDetailsService.loadUserByUsername("jacklee@example.com");

        assertEquals("jacklee@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsername_UserDoesNotExist() {
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername("notfound@example.com"));
    }
}
