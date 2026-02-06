package com.rockpaperscissors.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.rockpaperscissors.dto.request.UpdateUserRequest;
import com.rockpaperscissors.dto.response.UserResponse;
import com.rockpaperscissors.entity.User;
import com.rockpaperscissors.exception.UserAlreadyExistsException;
import com.rockpaperscissors.exception.UserNotFoundException;
import com.rockpaperscissors.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String NEW_EMAIL = "newemail@example.com";
    private static final String NEW_USERNAME = "newusername";

    @BeforeEach
    void setUp() {
    }

    // ==================== getCurrentUser() ====================

    @Test
    @DisplayName("Should retrieve current user successfully")
    void testGetCurrentUserSuccessfully() {
        User user = createUser(TEST_USER_ID, TEST_USERNAME, TEST_EMAIL);

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));

        UserResponse response = userService.getCurrentUser(TEST_EMAIL);

        assertNotNull(response);
        assertEquals(TEST_USER_ID, response.getId());
        assertEquals(TEST_USERNAME, response.getUsername());
        assertEquals(TEST_EMAIL, response.getEmail());
        verify(userRepository).findByEmail(TEST_EMAIL);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user not found")
    void testGetCurrentUserNotFound() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getCurrentUser(TEST_EMAIL));
        verify(userRepository).findByEmail(TEST_EMAIL);
    }

    // ==================== updateCurrentUser() ====================

    @Test
    @DisplayName("Should update current user successfully")
    void testUpdateCurrentUserSuccessfully() {
        User user = createUser(TEST_USER_ID, TEST_USERNAME, TEST_EMAIL);
        UpdateUserRequest request = new UpdateUserRequest(NEW_USERNAME, NEW_EMAIL);
        User updatedUser = createUser(TEST_USER_ID, NEW_USERNAME, NEW_EMAIL);

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(NEW_EMAIL)).thenReturn(false);
        when(userRepository.existsByUsername(NEW_USERNAME)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserResponse response = userService.updateCurrentUser(TEST_EMAIL, request);

        assertNotNull(response);
        assertEquals(NEW_USERNAME, response.getUsername());
        assertEquals(NEW_EMAIL, response.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user not found during update")
    void testUpdateCurrentUserNotFound() {
        UpdateUserRequest request = new UpdateUserRequest(NEW_USERNAME, NEW_EMAIL);

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.updateCurrentUser(TEST_EMAIL, request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when email already exists")
    void testUpdateCurrentUserEmailAlreadyExists() {
        User user = createUser(TEST_USER_ID, TEST_USERNAME, TEST_EMAIL);
        UpdateUserRequest request = new UpdateUserRequest(NEW_USERNAME, NEW_EMAIL);

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(NEW_EMAIL)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> userService.updateCurrentUser(TEST_EMAIL, request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when username already exists")
    void testUpdateCurrentUserUsernameAlreadyExists() {
        User user = createUser(TEST_USER_ID, TEST_USERNAME, TEST_EMAIL);
        UpdateUserRequest request = new UpdateUserRequest(NEW_USERNAME, NEW_EMAIL);

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(NEW_EMAIL)).thenReturn(false);
        when(userRepository.existsByUsername(NEW_USERNAME)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> userService.updateCurrentUser(TEST_EMAIL, request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should skip duplicate validation when email and username don't change")
    void testUpdateCurrentUserSameValues() {
        User user = createUser(TEST_USER_ID, TEST_USERNAME, TEST_EMAIL);
        UpdateUserRequest request = new UpdateUserRequest(TEST_USERNAME, TEST_EMAIL);

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.updateCurrentUser(TEST_EMAIL, request);

        assertNotNull(response);
        assertEquals(TEST_USERNAME, response.getUsername());
        assertEquals(TEST_EMAIL, response.getEmail());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).existsByUsername(anyString());
        verify(userRepository).save(any(User.class));
    }

    // ==================== HELPER METHOD ====================

    private User createUser(Long id, String username, String email) {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .build();
    }
}