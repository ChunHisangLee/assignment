package com.example.assignment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.assignment.constants.MessagesConstants;
import com.example.assignment.dto.UsersDto;
import com.example.assignment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  private MockMvc mockMvc;

  @Mock private UserService userService;

  @Mock private AuthenticationManager authenticationManager;

  @InjectMocks private UserController userController;

  private UsersDto sampleUserDTO;
  private Authentication authentication;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

    sampleUserDTO = UsersDto.builder().id(1L).name("Jack Lee").email("jacklee@example.com").build();

    authentication =
        new UsernamePasswordAuthenticationToken(sampleUserDTO.getEmail(), "encodedPassword");
  }

  @Test
  void testRegisterUser_Success() throws Exception {
    // For void methods, use doNothing() to simulate behavior.
    doNothing().when(userService).registerUser(any(UsersDto.class));

    mockMvc
        .perform(
            post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"name\": \"Jack Lee\", \"email\": \"jacklee@example.com\", \"password\": \"password\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value(MessagesConstants.STATUS_201))
        .andExpect(jsonPath("$.message").value(MessagesConstants.MESSAGE_201));
  }

  @Test
  void testUpdateUser_Success() throws Exception {
    // updateUser now returns a boolean.
    when(userService.updateUser(eq(1L), any(UsersDto.class))).thenReturn(true);

    String jsonRequest =
        "{\"name\": \"Jack Lee\", \"email\": \"jacklee@example.com\", \"password\": \"newpassword\"}";

    mockMvc
        .perform(put("/api/users/1").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(MessagesConstants.STATUS_200))
        .andExpect(jsonPath("$.message").value(MessagesConstants.MESSAGE_200));
  }

  @Test
  void testDeleteUser_Success() throws Exception {
    // deleteUser now returns a boolean.
    when(userService.deleteUser(1L)).thenReturn(true);

    mockMvc
        .perform(delete("/api/users/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(MessagesConstants.STATUS_200))
        .andExpect(jsonPath("$.message").value(MessagesConstants.MESSAGE_200));
  }

  @Test
  void testGetUserById_Success() throws Exception {
    // getUserById returns a UsersDto.
    when(userService.getUserById(1L)).thenReturn(sampleUserDTO);

    mockMvc
        .perform(get("/api/users/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Jack Lee"))
        .andExpect(jsonPath("$.email").value("jacklee@example.com"));
  }

  @Test
  void testLogin_Success() throws Exception {
    // For login, we expect a JwtAuthenticationResponse.
    when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
    when(userService.login(any(UsersDto.class))).thenReturn("mockedToken");

    String jsonRequest = "{\"email\": \"jacklee@example.com\", \"password\": \"password\"}";

    mockMvc
        .perform(
            post("/api/users/login").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("mockedToken"));
  }

  @Test
  void testLogin_InvalidCredentials() throws Exception {
    when(authenticationManager.authenticate(any(Authentication.class)))
        .thenThrow(new RuntimeException("Invalid credentials"));

    String jsonRequest = "{\"email\": \"jacklee@example.com\", \"password\": \"wrongpassword\"}";

    mockMvc
        .perform(
            post("/api/users/login").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void testLogout_Success() throws Exception {
    // Logout endpoint simply returns an OK status.
    mockMvc.perform(get("/api/users/logout")).andExpect(status().isOk());
  }
}
