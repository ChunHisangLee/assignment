package com.example.assignment.security;

import com.example.assignment.constants.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {
  private final CustomUserDetailsService customUserDetailsService;
  private final JwtTokenProvider jwtTokenProvider;

  @Value("${security.authentication.enabled:true}")
  private boolean authenticationEnabled;

  @Autowired
  public WebSecurityConfig(
      CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
    this.customUserDetailsService = customUserDetailsService;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    log.info("Configuring security filter chain");

    if (authenticationEnabled) {
      log.info("Authentication is ON");
      http.csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(
              authorize ->
                  authorize
                      .requestMatchers(SecurityConstants.getPublicUrls())
                      .permitAll()
                      .anyRequest()
                      .authenticated())
          .sessionManagement(
              session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .addFilterBefore(
              new JwtAuthenticationFilter(jwtTokenProvider),
              UsernamePasswordAuthenticationFilter.class);
    } else {
      log.info("Authentication is OFF");
      http.csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
    }

    log.info("Security filter chain configured successfully");
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    log.info("Creating AuthenticationManager bean");
    AuthenticationManager authenticationManager =
        http.getSharedObject(AuthenticationManagerBuilder.class).build();
    log.info("AuthenticationManager bean created successfully");
    return authenticationManager;
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    log.info("Creating AuthenticationProvider bean");
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setPasswordEncoder(passwordEncoder());
    authProvider.setUserDetailsService(customUserDetailsService);
    log.info("AuthenticationProvider bean created successfully");
    return authProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    log.info("Creating PasswordEncoder bean");
    return new BCryptPasswordEncoder();
  }
}
