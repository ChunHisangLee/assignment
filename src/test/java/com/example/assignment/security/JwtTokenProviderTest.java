package com.example.assignment.security;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    private final String jwtSecret = "testSecretKeyForJwtWhichIsAtLeast256BitsLong";  // Should be at least 256 bits long
    private final int jwtExpirationMs = 3600000;  // 1 hour

    @BeforeEach
    void setUp() {
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        jwtTokenProvider = new JwtTokenProvider(userDetailsService, jwtSecret, jwtExpirationMs);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
    }

    @Test
    void shouldGenerateToken() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testuser");

        // Act
        String token = jwtTokenProvider.generateToken(authentication);

        // Assert
        assertThat(token).isNotNull();
        assertThat(jwtTokenProvider.getUsernameFromJwt(token)).isEqualTo("testuser");
    }

    @Test
    void shouldGetUsernameFromJwt() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtTokenProvider.generateToken(authentication);

        // Act
        String username = jwtTokenProvider.getUsernameFromJwt(token);

        // Assert
        assertThat(username).isEqualTo("testuser");
    }

    @Test
    void shouldValidateToken() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtTokenProvider.generateToken(authentication);

        // Act
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Assert
        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalseWhenTokenIsInvalid() {
        // Arrange
        String invalidToken = "invalid.token";

        // Act
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        // Assert
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldGetAuthenticationFromToken() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);

        String token = jwtTokenProvider.generateToken(authentication);

        // Act
        Authentication auth = jwtTokenProvider.getAuthentication(token);

        // Assert
        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("testuser");
        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
    }
}
