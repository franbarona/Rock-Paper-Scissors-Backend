package com.rockpaperscissors.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rockpaperscissors.dto.request.LoginRequest;
import com.rockpaperscissors.dto.request.RegisterRequest;
import com.rockpaperscissors.dto.response.AuthResponse;
import com.rockpaperscissors.dto.response.LoginResponse;
import com.rockpaperscissors.entity.User;
import com.rockpaperscissors.exception.InvalidCredentialsException;
import com.rockpaperscissors.exception.UserAlreadyExistsException;
import com.rockpaperscissors.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_TOKEN = "jwt-token";

    @BeforeEach
    void setUp() {
    }

    // ==================== register() ====================

    @Test
    @DisplayName("Should register user successfully")
    void testRegisterSuccessfully() {
        RegisterRequest request = new RegisterRequest(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        User savedUser = createUser(TEST_USER_ID, TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        when(userRepository.existsByUsername(TEST_USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(anyLong(), anyString(), anyString())).thenReturn(TEST_TOKEN);

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals(TEST_TOKEN, response.getToken());
        assertEquals(TEST_EMAIL, response.getEmail());
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(anyLong(), eq(TEST_EMAIL), eq(TEST_USERNAME));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when username exists")
    void testRegisterUsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        when(userRepository.existsByUsername(TEST_USERNAME)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when email exists")
    void testRegisterEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        when(userRepository.existsByUsername(TEST_USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    // ==================== login() ====================

    @Test
    @DisplayName("Should login user successfully")
    void testLoginSuccessfully() {
        LoginRequest request = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);
        User user = createUser(1L, TEST_USERNAME, TEST_EMAIL, "encoded-password");

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(TEST_PASSWORD, user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(TEST_USER_ID, TEST_EMAIL, TEST_USERNAME)).thenReturn(TEST_TOKEN);

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals(TEST_TOKEN, response.getToken());
        assertEquals(TEST_USERNAME, response.getUsername());
        assertEquals(TEST_EMAIL, response.getEmail());
        assertEquals(1L, response.getId());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when user not found")
    void testLoginUserNotFound() {
        LoginRequest request = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
        verify(jwtService, never()).generateToken(anyLong(), anyString(), anyString());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when password is incorrect")
    void testLoginInvalidPassword() {
        LoginRequest request = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);
        User user = createUser(1L, TEST_USERNAME, TEST_EMAIL, "encoded-password");

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(TEST_PASSWORD, user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
        verify(jwtService, never()).generateToken(anyLong(), anyString(), anyString());
    }

    // ==================== HELPER METHOD ====================

    private User createUser(Long id, String username, String email, String password) {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .build();
    }
}