package com.example.assignment.security;

import com.example.assignment.entity.Users;
import com.example.assignment.repository.UsersRepository;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

  private final UsersRepository usersRepository;

  public CustomUserDetailsService(UsersRepository usersRepository) {
    this.usersRepository = usersRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    log.info("Attempting to load user by email: {}", email);

    Users user =
        usersRepository
            .findByEmail(email)
            .orElseThrow(
                () -> {
                  log.error("User not found with email: {}", email);
                  return new UsernameNotFoundException("User not found with email: " + email);
                });

    log.info("User found with email: {}", email);

    return new User(user.getEmail(), user.getPassword(), Collections.emptyList());
  }
}
