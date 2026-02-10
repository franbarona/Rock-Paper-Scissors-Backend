package com.rockpaperscissors.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rockpaperscissors.dto.response.UserStatisticsResponse;
import com.rockpaperscissors.entity.User;
import com.rockpaperscissors.entity.UserStatistics;
import com.rockpaperscissors.enums.GameResult;
import com.rockpaperscissors.exception.StatisticsNotFoundException;
import com.rockpaperscissors.exception.UserNotFoundException;
import com.rockpaperscissors.repository.UserRepository;
import com.rockpaperscissors.repository.UserStatisticsRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("StatisticsService Tests")
class StatisticsServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserStatisticsRepository userStatisticsRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    private static final UUID TEST_USER_ID = UUID.randomUUID();
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_USERNAME = "testuser";

    private static final UUID TEST_USER_ID_2 = UUID.randomUUID();
    private static final String TEST_EMAIL_2 = "test2@example.com";
    private static final String TEST_USERNAME_2 = "testuser2";

    private User user;
    private User user2;
    private UserStatistics userStatistics;
    private UserStatistics user2Statistics;
    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(TEST_USER_ID)
                .email(TEST_EMAIL)
                .username(TEST_USERNAME)
                .build();

        userStatistics = UserStatistics.builder()
                .id(TEST_USER_ID)
                .user(user)
                .totalMatches(10)
                .totalWins(6)
                .totalLosses(2)
                .totalDraws(2)
                .build();

        user2 = User.builder()
                .id(TEST_USER_ID_2)
                .email(TEST_EMAIL_2)
                .username(TEST_USERNAME_2)
                .build();

        user2Statistics = UserStatistics.builder()
                .id(TEST_USER_ID_2)
                .user(user2)
                .totalMatches(0)
                .totalWins(0)
                .totalLosses(0)
                .totalDraws(0)
                .build();
    }

    // ==================== getUserStatistics() ====================

    @Test
    @DisplayName("Should get user statistics successfully")
    void testGetUserStatisticsSuccessfully() {
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(userStatisticsRepository.findByUserId(TEST_USER_ID))
                .thenReturn(Optional.of(userStatistics));

        UserStatisticsResponse response = statisticsService.getUserStatistics(TEST_USER_ID);

        assertEquals(TEST_USER_ID, response.getUserId());
        assertEquals(TEST_USERNAME, response.getUsername());
        assertEquals(10, response.getTotalMatches());
        assertEquals(6, response.getTotalWins());
        assertEquals(2, response.getTotalLosses());
        assertEquals(2, response.getTotalDraws());
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not exist")
    void testGetUserStatisticsUserNotFound() {
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> statisticsService.getUserStatistics(TEST_USER_ID));
    }

    @Test
    @DisplayName("Should throw StatisticsNotFoundException when statistics not found for user")
    void testGetUserStatisticsNotFound() {
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(userStatisticsRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.empty());

        assertThrows(StatisticsNotFoundException.class,
                () -> statisticsService.getUserStatistics(TEST_USER_ID));
    }

    // ==================== updateUserStatistics() ====================

    @Test
    @DisplayName("Should initialize statistics when result is null")
    void testUpdateUserStatisticsInitialization() {
        when(userStatisticsRepository.findByUserId(TEST_USER_ID_2)).thenReturn(Optional.empty());
        when(userStatisticsRepository.save(any(UserStatistics.class))).thenReturn(user2Statistics);

        statisticsService.updateUserStatistics(user2, null);

        verify(userStatisticsRepository).save(any(UserStatistics.class));
        assertEquals(0, user2Statistics.getTotalMatches());
        assertEquals(0, user2Statistics.getTotalWins());
        assertEquals(0, user2Statistics.getTotalLosses());
        assertEquals(0, user2Statistics.getTotalDraws());
    }

    @Test
    @DisplayName("Should update statistics with WIN result")
    void testUpdateUserStatisticsWithWin() {
        when(userStatisticsRepository.findByUserId(TEST_USER_ID))
                .thenReturn(Optional.of(userStatistics));
        when(userStatisticsRepository.save(any(UserStatistics.class))).thenReturn(userStatistics);

        statisticsService.updateUserStatistics(user, GameResult.WIN);

        verify(userStatisticsRepository).save(any(UserStatistics.class));
        assertEquals(11, userStatistics.getTotalMatches());
        assertEquals(7, userStatistics.getTotalWins());
    }

    @Test
    @DisplayName("Should create new statistics when they don't exist and result is WIN")
    void testUpdateUserStatisticsCreateNewWithWin() {
        UserStatistics newStats = UserStatistics.builder()
                .user(user)
                .totalMatches(0)
                .totalWins(0)
                .totalLosses(0)
                .totalDraws(0)
                .build();

        when(userStatisticsRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.empty());
        when(userStatisticsRepository.save(any(UserStatistics.class))).thenReturn(newStats);

        statisticsService.updateUserStatistics(user, GameResult.WIN);

        verify(userStatisticsRepository).save(any(UserStatistics.class));
    }
}