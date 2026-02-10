package com.rockpaperscissors.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockpaperscissors.dto.request.LoginRequest;
import com.rockpaperscissors.dto.request.RegisterRequest;
import com.rockpaperscissors.dto.response.LoginResponse;
import com.rockpaperscissors.dto.response.RegisterResponse;
import com.rockpaperscissors.exception.InvalidCredentialsException;
import com.rockpaperscissors.exception.UserAlreadyExistsException;
import com.rockpaperscissors.service.AuthService;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_TOKEN = "jwt-token";
    private static final String API_BASE = "/api/auth";

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private RegisterResponse registerResponse;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .username(TEST_USERNAME)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        loginRequest = LoginRequest.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        registerResponse = RegisterResponse.builder()
                .token(TEST_TOKEN)
                .username(TEST_USERNAME)
                .email(TEST_EMAIL)
                .id(TEST_USER_ID)
                .build();

        loginResponse = LoginResponse.builder()
                .id(TEST_USER_ID)
                .username(TEST_USERNAME)
                .email(TEST_EMAIL)
                .token(TEST_TOKEN)
                .build();
    }

    // ==================== register() ====================

    @Test
    @DisplayName("Should register user successfully")
    void testRegisterSuccessfully() throws Exception {
        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(registerResponse);

        mockMvc.perform(post(API_BASE + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.token").value(TEST_TOKEN))
                .andExpect(jsonPath("$.data.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.data.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.data.id").value(TEST_USER_ID.toString()));
    }

    @Test
    @DisplayName("Should return 400 when registering with invalid request")
    void testRegisterWithInvalidRequest() throws Exception {
        RegisterRequest invalidRequest = RegisterRequest.builder()
                .username("")
                .email("")
                .password("")
                .build();

        mockMvc.perform(post(API_BASE + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 409 when registering with existing username")
    void testRegisterWithExistingUsername() throws Exception {
        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new UserAlreadyExistsException("username", "testuser"));

        mockMvc.perform(post(API_BASE + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return 409 when registering with existing email")
    void testRegisterWithExistingEmail() throws Exception {
        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new UserAlreadyExistsException("email", "test@example.com"));

        mockMvc.perform(post(API_BASE + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isConflict());
    }

    // ==================== login() ====================

    @Test
    @DisplayName("Should login user successfully")
    void testLoginSuccessfully() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(loginResponse);

        mockMvc.perform(post(API_BASE + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.id").value(TEST_USER_ID.toString()))
                .andExpect(jsonPath("$.data.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.data.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.data.token").value(TEST_TOKEN));
    }

    @Test
    @DisplayName("Should return 400 when logging in with invalid request")
    void testLoginWithInvalidRequest() throws Exception {
        LoginRequest invalidRequest = LoginRequest.builder()
                .email("")
                .password("")
                .build();

        mockMvc.perform(post(API_BASE + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 401 when logging in with invalid credentials")
    void testLoginWithInvalidCredentials() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post(API_BASE + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}