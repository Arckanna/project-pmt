package com.pmt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmt.entities.User;
import com.pmt.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testPing() throws Exception {
        mockMvc.perform(get("/api/users/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("secret");

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testLoginSuccess() throws Exception {
        User user = new User();
        user.setEmail("login@example.com");
        user.setPassword("password");

        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("email", "login@example.com", "password", "password"))))
                .andExpect(status().isOk())
                .andExpect(content().string("Connexion réussie"));
    }

    @Test
    public void testLoginFailure() throws Exception {
        Mockito.when(userRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("email", "fail@example.com", "password", "wrong"))))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Identifiants incorrects"));
    }
}
