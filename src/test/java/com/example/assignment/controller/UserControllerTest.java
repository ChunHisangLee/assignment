package com.example.assignment.controller;

import com.example.assignment.entity.Users;
import com.example.assignment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    private Users sampleUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        sampleUser = new Users();
        sampleUser.setId(1L);
        sampleUser.setName("Jack Lee");
        sampleUser.setEmail("jacklee@example.com");
        sampleUser.setPassword("encodedPassword");
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userService.findByEmail(eq(sampleUser.getEmail()))).thenReturn(Optional.empty());

        String jsonRequest = "{\"name\": \"Jack Lee\", \"email\": \"jacklee@example.com\", \"password\": \"password\"}";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jack Lee"))
                .andExpect(jsonPath("$.email").value("jacklee@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testRegisterUser_EmailAlreadyRegistered() throws Exception {
        when(userService.findByEmail(eq(sampleUser.getEmail()))).thenReturn(Optional.of(sampleUser));

        String jsonRequest = "{\"name\": \"Jack Lee\", \"email\": \"jacklee@example.com\", \"password\": \"password\"}";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already registered. Try to log in or register with another email."));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        when(userService.updateUser(eq(1L), any(Users.class))).thenReturn(Optional.of(sampleUser));
        when(userService.findByEmail(eq(sampleUser.getEmail()))).thenReturn(Optional.empty());

        String jsonRequest = "{\"name\": \"Jack Lee\", \"email\": \"jacklee@example.com\", \"password\": \"newpassword\"}";

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jack Lee"))
                .andExpect(jsonPath("$.email").value("jacklee@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testDeleteUser_Success() throws Exception {

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserById_Success() throws Exception {
        when(userService.getUserById(eq(1L))).thenReturn(Optional.of(sampleUser));

        mockMvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jack Lee"))
                .andExpect(jsonPath("$.email").value("jacklee@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testLogin_Success() throws Exception {
        when(userService.findByEmail(eq(sampleUser.getEmail()))).thenReturn(Optional.of(sampleUser));
        when(userService.verifyPassword(any(), any())).thenReturn(true);

        String jsonRequest = "{\"email\": \"jacklee@example.com\", \"password\": \"password\"}";

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jack Lee"))
                .andExpect(jsonPath("$.email").value("jacklee@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        when(userService.findByEmail(eq(sampleUser.getEmail()))).thenReturn(Optional.of(sampleUser));
        when(userService.verifyPassword(any(), any())).thenReturn(false);

        String jsonRequest = "{\"email\": \"jacklee@example.com\", \"password\": \"wrongpassword\"}";

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password."));
    }
}
