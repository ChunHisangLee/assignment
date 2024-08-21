package com.example.assignment.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(WebSecurityConfig.class)
class WebSecurityConfigTest {

    @Autowired
    private WebSecurityConfig webSecurityConfig;

    @Autowired
    private AuthenticationManager authenticationManager; // Automatically configured by Spring Security

    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = webSecurityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder);
    }

    @Test
    void testAuthenticationProviderBean() {
        DaoAuthenticationProvider authProvider = (DaoAuthenticationProvider) webSecurityConfig.authenticationProvider();
        assertNotNull(authProvider);
        assertInstanceOf(DaoAuthenticationProvider.class, authProvider);
    }

    @Test
    void testAuthenticationManagerBean() {
        assertNotNull(authenticationManager);
    }
}
