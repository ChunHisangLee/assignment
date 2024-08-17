package com.example.assignment.controller;

import com.example.assignment.entity.Users;
import com.example.assignment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@WithMockUser
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Users sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new Users();
        sampleUser.setId(1L);
        sampleUser.setName("Jack Lee");
        sampleUser.setEmail("jacklee@example.com");
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        sampleUser.setPassword(passwordEncoder.encode("password"));
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.findByEmail(eq("jacklee@example.com"))).thenReturn(Optional.empty());
        when(userService.createUser(any(Users.class))).thenReturn(sampleUser);

        ResultActions resultActions = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Jack Lee\", \"email\": \"jacklee@example.com\", \"password\": \"password\"}")
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jack Lee"))
                .andExpect(jsonPath("$.email").value("jacklee@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testCreateUser_EmailAlreadyRegistered() throws Exception {
        when(userService.findByEmail(eq("jacklee@example.com"))).thenReturn(Optional.of(sampleUser));

        ResultActions resultActions = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Jack Lee\", \"email\": \"jacklee@example.com\", \"password\": \"password\"}")
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already registered. Try to log in or register with another email."));
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userService.findByEmail(eq("jacklee@example.com"))).thenReturn(Optional.empty());
        when(userService.updateUser(anyLong(), any(Users.class))).thenReturn(Optional.of(sampleUser));

        ResultActions resultActions = mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Jack Lee Updated\", \"email\": \"jacklee@example.com\", \"password\": \"newpassword\"}")
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jack Lee"))
                .andExpect(jsonPath("$.email").value("jacklee@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testUpdateUser_EmailAlreadyRegisteredByAnotherUser() throws Exception {
        Users anotherUser = new Users();
        anotherUser.setId(2L);
        anotherUser.setEmail("jacklee@example.com");

        when(userService.findByEmail(eq("jacklee@example.com"))).thenReturn(Optional.of(anotherUser));

        ResultActions resultActions = mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Jack Lee Updated\", \"email\": \"jacklee@example.com\", \"password\": \"newpassword\"}")
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        resultActions.andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already registered by another user."));
    }

    @Test
    void testDeleteUser() throws Exception {
        when(userService.deleteUser(anyLong())).thenReturn(true);

        ResultActions resultActions = mockMvc.perform(delete("/api/users/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        resultActions.andExpect(status().isOk());
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(sampleUser));

        ResultActions resultActions = mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jack Lee"))
                .andExpect(jsonPath("$.email").value("jacklee@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }
}
