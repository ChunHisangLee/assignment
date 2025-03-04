package com.example.assignment.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.assignment.dto.UsersDto;
import com.example.assignment.entity.Users;
import com.example.assignment.exception.CustomErrorException;
import com.example.assignment.exception.ResourceNotFoundException;
import com.example.assignment.mapper.UsersMapper;
import com.example.assignment.repository.UsersRepository;
import com.example.assignment.security.JwtTokenProvider;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock private UsersRepository usersRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private UsersMapper usersMapper;

  @Mock private AuthenticationManager authenticationManager;

  @Mock private JwtTokenProvider jwtTokenProvider;

  @InjectMocks private UserServiceImpl userService;

  private Users user;
  private UsersDto usersDto;

  @BeforeEach
  void setUp() {
    user =
        Users.builder()
            .id(1L)
            .email("jacklee@example.com")
            .name("Jack Lee")
            .password("rawPassword")
            .build();

    usersDto =
        UsersDto.builder()
            .id(1L)
            .email("jacklee@example.com")
            .name("Jack Lee")
            .password("rawPassword")
            .build();
  }

  @Test
  void registerUser_Success() {
    when(usersMapper.toEntity(any(UsersDto.class))).thenReturn(user);
    when(usersRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    when(usersRepository.save(any(Users.class))).thenReturn(user);

    userService.registerUser(usersDto);

    verify(usersMapper, times(1)).toEntity(any(UsersDto.class));
    verify(usersRepository, times(1)).findByEmail(anyString());
    verify(passwordEncoder, times(1)).encode(anyString());
    verify(usersRepository, times(1)).save(any(Users.class));
  }

  @Test
  void registerUser_EmailAlreadyRegistered() {
    when(usersMapper.toEntity(any(UsersDto.class))).thenReturn(user);
    when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

    CustomErrorException exception =
        assertThrows(CustomErrorException.class, () -> userService.registerUser(usersDto));
    assertTrue(exception.getMessage().contains("Customer already registered with given Email"));
    verify(usersRepository, times(1)).findByEmail(anyString());
    verify(usersRepository, never()).save(any(Users.class));
  }

  @Test
  void updateUser_Success() {
    when(usersMapper.toEntity(any(UsersDto.class))).thenReturn(user);
    when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(usersRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    when(usersRepository.save(any(Users.class))).thenReturn(user);

    boolean result = userService.updateUser(1L, usersDto);

    assertTrue(result);
    verify(usersRepository, times(1)).findById(anyLong());
    verify(usersRepository, times(1)).save(any(Users.class));
  }

  @Test
  void updateUser_UserNotFound() {
    when(usersMapper.toEntity(any(UsersDto.class))).thenReturn(user);
    when(usersRepository.findById(anyLong())).thenReturn(Optional.empty());

    ResourceNotFoundException exception =
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, usersDto));
    assertTrue(exception.getMessage().toLowerCase().contains("not found"));
    verify(usersRepository, times(1)).findById(anyLong());
    verify(usersRepository, never()).save(any(Users.class));
  }

  @Test
  void deleteUser_Success() {
    when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));
    doNothing().when(usersRepository).delete(any(Users.class));

    boolean result = userService.deleteUser(1L);

    assertTrue(result);
    verify(usersRepository, times(1)).findById(anyLong());
    verify(usersRepository, times(1)).delete(any(Users.class));
  }

  @Test
  void deleteUser_UserNotFound() {
    when(usersRepository.findById(anyLong())).thenReturn(Optional.empty());

    ResourceNotFoundException exception =
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
    assertTrue(exception.getMessage().toLowerCase().contains("not found"));
    verify(usersRepository, times(1)).findById(anyLong());
    verify(usersRepository, never()).delete(any(Users.class));
  }

  @Test
  void login_Success() {
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("mockedToken");

    String token = userService.login(usersDto);

    assertNotNull(token);
    assertEquals("mockedToken", token);
    verify(authenticationManager, times(1))
        .authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(jwtTokenProvider, times(1)).generateToken(any(Authentication.class));
  }

  @Test
  void login_InvalidCredentials() {
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new AuthenticationException("Invalid credentials") {});

    String token = userService.login(usersDto);

    assertNull(token);
    verify(authenticationManager, times(1))
        .authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(jwtTokenProvider, never()).generateToken(any(Authentication.class));
  }


  @Test
  void getUserById_Success() {
    when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(usersMapper.toDto(any(Users.class))).thenReturn(usersDto);

    UsersDto foundUser = userService.getUserById(1L);

    assertNotNull(foundUser);
    assertEquals(user.getId(), foundUser.getId());
    verify(usersRepository, times(1)).findById(anyLong());
  }

  @Test
  void getUserById_UserNotFound() {
    when(usersRepository.findById(anyLong())).thenReturn(Optional.empty());

    ResourceNotFoundException exception =
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));

    assertTrue(exception.getMessage().toLowerCase().contains("not found"));
    verify(usersRepository, times(1)).findById(anyLong());
  }

  @Test
  void verifyPassword_Success() {
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

    boolean isMatch = userService.verifyPassword("rawPassword", "encodedPassword");

    assertTrue(isMatch);
    verify(passwordEncoder, times(1)).matches(anyString(), anyString());
  }
}
