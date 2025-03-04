package com.example.assignment.service.impl;

import com.example.assignment.dto.UsersDto;
import com.example.assignment.entity.Users;
import com.example.assignment.exception.CustomErrorException;
import com.example.assignment.exception.ResourceNotFoundException;
import com.example.assignment.mapper.UsersMapper;
import com.example.assignment.repository.UsersRepository;
import com.example.assignment.security.JwtTokenProvider;
import com.example.assignment.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final UsersRepository usersRepository;
  private final PasswordEncoder passwordEncoder;
  private final UsersMapper usersMapper;

  public UserServiceImpl(
      AuthenticationManager authenticationManager,
      JwtTokenProvider jwtTokenProvider,
      UsersRepository usersRepository,
      PasswordEncoder passwordEncoder,
      UsersMapper usersMapper) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.usersRepository = usersRepository;
    this.passwordEncoder = passwordEncoder;
    this.usersMapper = usersMapper;
  }

  @Override
  public void registerUser(UsersDto usersDto) {
    Users users = usersMapper.toEntity(usersDto);
    log.info("Attempting to register user with email: {}", users.getEmail());

    if (usersRepository.findByEmail(users.getEmail()).isPresent()) {
      log.error("Customer already registered with given Email: {}", users.getEmail());
      throw new CustomErrorException(
          "Customer already registered with given Email: " + users.getEmail());
    }

    // Encode the user's password
    users.setPassword(passwordEncoder.encode(users.getPassword()));
    log.debug("Password encoded for user with email: {}", users.getEmail());

    // Save the user and cascade save the wallet
    Users savedUser = usersRepository.save(users);
    log.info(
        "User with email: {} registered successfully with ID: {}",
        savedUser.getEmail(),
        savedUser.getId());
  }

  @Override
  public boolean updateUser(Long id, UsersDto usersDto) {
    Users users = usersMapper.toEntity(usersDto);
    Users existingUser =
        usersRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  log.error("User with ID: {} not found for update.", id);
                  return new ResourceNotFoundException("User", "User ID", id.toString());
                });

    if (usersRepository
        .findByEmail(users.getEmail())
        .filter(user -> !user.getId().equals(id))
        .isPresent()) {
      log.error("Email {} is already registered by another user.", users.getEmail());
      throw new CustomErrorException("Email is already registered by another user.");
    }

    existingUser.setName(users.getName());
    existingUser.setEmail(users.getEmail());
    log.debug("User details updated for user with ID: {}", id);

    if (users.getPassword() != null && !users.getPassword().isEmpty()) {
      existingUser.setPassword(passwordEncoder.encode(users.getPassword()));
      log.debug("Password updated for user with ID: {}", id);
    }

    usersRepository.save(existingUser);
    log.info("User with ID: {} updated successfully.", id);
    return true;
  }

  @Override
  public boolean deleteUser(Long id) {
    log.info("Attempting to delete user with ID: {}", id);
    Users user =
        usersRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  log.error("User with ID: {} not found for deletion.", id);
                  return new ResourceNotFoundException("User", "User ID", id.toString());
                });
    usersRepository.delete(user);
    log.info("User with ID: {} deleted successfully.", id);
    return true;
  }

  @Override
  public String login(UsersDto usersDto) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(usersDto.getEmail(), usersDto.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      return jwtTokenProvider.generateToken(authentication);
    } catch (AuthenticationException e) {
      log.error("Invalid credentials for email: {}", usersDto.getEmail());
      return null;
    }
  }

  @Override
  public UsersDto getUserById(Long id) {
    log.info("Fetching user by ID: {}", id);

    Users users =
        usersRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  log.error("User with ID: {} not found.", id);
                  return new ResourceNotFoundException("User", "User ID", id.toString());
                });

    return usersMapper.toDto(users);
  }

  @Override
  public boolean verifyPassword(String rawPassword, String encodedPassword) {
    log.debug("Verifying password for authentication.");
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }
}
