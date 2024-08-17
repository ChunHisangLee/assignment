package com.example.assignment.controller;

import com.example.assignment.entity.Users;
import com.example.assignment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithMockUser
@Transactional
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    private Users sampleUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        sampleUser = new Users();
        sampleUser.setName("Jack Lee");
        sampleUser.setEmail("jacklee@example.com");
        sampleUser.setPassword(passwordEncoder.encode("password"));
    }

    @Test
    void testCreateUser() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Jack Lee\", \"email\": \"jacklee@example.com\", \"password\": \"password\"}")
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Jack Lee"))
                .andExpect(jsonPath("$.email").value("jacklee@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());

        // Verify that the user is actually saved in the database
        Optional<Users> savedUser = userRepository.findByEmail("jacklee@example.com");
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getName()).isEqualTo("Jack Lee");
    }

    @Test
    void testUpdateUser() throws Exception {
        // First, save a user
        Users savedUser = userRepository.save(sampleUser);

        // Update the user
        ResultActions resultActions = mockMvc.perform(put("/api/users/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Jack Lee Updated\", \"email\": \"jacklee@example.com\", \"password\": \"newpassword\"}")
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.name").value("Jack Lee Updated"))
                .andExpect(jsonPath("$.email").value("jacklee@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());

        // Verify the user is updated in the database
        Optional<Users> updatedUser = userRepository.findById(savedUser.getId());
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getName()).isEqualTo("Jack Lee Updated");
    }

    @Test
    void testDeleteUser() throws Exception {
        // First, save a user
        Users savedUser = userRepository.save(sampleUser);

        // Delete the user
        ResultActions resultActions = mockMvc.perform(delete("/api/users/" + savedUser.getId())
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        resultActions.andExpect(status().isOk());

        // Verify the user is deleted from the database
        Optional<Users> deletedUser = userRepository.findById(savedUser.getId());
        assertThat(deletedUser).isNotPresent();
    }

    @Test
    void testGetUserById() throws Exception {
        // First, save a user
        Users savedUser = userRepository.save(sampleUser);

        // Retrieve the user
        ResultActions resultActions = mockMvc.perform(get("/api/users/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.name").value("Jack Lee"))
                .andExpect(jsonPath("$.email").value("jacklee@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }
}
