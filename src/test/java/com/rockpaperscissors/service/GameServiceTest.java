package com.rockpaperscissors.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rockpaperscissors.dto.response.GamePlayResponse;
import com.rockpaperscissors.entity.GameMatch;
import com.rockpaperscissors.entity.User;
import com.rockpaperscissors.enums.GameMove;
import com.rockpaperscissors.enums.GameResult;
import com.rockpaperscissors.exception.UserNotFoundException;
import com.rockpaperscissors.repository.GameMatchRepository;
import com.rockpaperscissors.repository.UserRepository;
import com.rockpaperscissors.utils.RandomMoveSelector;

@ExtendWith(MockitoExtension.class)
@DisplayName("GameService Tests")
class GameServiceTest {

    @Mock
    private GameMatchRepository gameMatchRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private GameService gameService;

    private User testUser;
    private static final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail(TEST_EMAIL);
    }

    // ==================== playMove() ====================

    @Test
    @DisplayName("Should play move successfully and save game match")
    void testPlayMoveSuccessfully() {
        GameMove playerMove = GameMove.ROCK;
        GameMove computerMove = GameMove.SCISSORS;
        GameResult result = GameResult.WIN;
        GameMatch savedMatch = createGameMatch(1L, playerMove, computerMove, result);

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(gameMatchRepository.save(any(GameMatch.class))).thenReturn(savedMatch);

        try (MockedStatic<RandomMoveSelector> mockedSelector = mockStatic(RandomMoveSelector.class)) {
            mockedSelector.when(RandomMoveSelector::selectRandomMove).thenReturn(computerMove);
            GamePlayResponse response = gameService.playMove(playerMove, TEST_EMAIL);

            assertNotNull(response);
            assertEquals(playerMove, response.getPlayerMove());
            assertEquals(computerMove, response.getComputerMove());
            assertEquals(result, response.getResult());
            verify(statisticsService).updateUserStatistics(testUser, result);
        }
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not exist in playMove")
    void testPlayMoveUserNotFound() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> gameService.playMove(GameMove.ROCK, TEST_EMAIL));
        verify(gameMatchRepository, never()).save(any(GameMatch.class));
    }

    // ==================== getHistory() ====================

    @Test
    @DisplayName("Should retrieve game history successfully")
    void testGetHistorySuccessfully() {
        List<GameMatch> matches = List.of(
                createGameMatch(1L, GameMove.ROCK, GameMove.SCISSORS, GameResult.WIN),
                createGameMatch(2L, GameMove.PAPER, GameMove.ROCK, GameResult.WIN));

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(gameMatchRepository.findByUserIdOrderByPlayedAtDesc(testUser.getId())).thenReturn(matches);

        List<GamePlayResponse> history = gameService.getHistory(TEST_EMAIL);

        assertNotNull(history);
        assertEquals(2, history.size());
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not exist in getHistory")
    void testGetHistoryUserNotFound() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> gameService.getHistory(TEST_EMAIL));
        verify(gameMatchRepository, never()).findByUserIdOrderByPlayedAtDesc(any());
    }

    @Test
    @DisplayName("Should return empty list when user has no history")
    void testGetHistoryEmpty() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(gameMatchRepository.findByUserIdOrderByPlayedAtDesc(testUser.getId())).thenReturn(List.of());

        List<GamePlayResponse> history = gameService.getHistory(TEST_EMAIL);

        assertEquals(0, history.size());
    }

    // ==================== HELPER METHOD ====================

    private GameMatch createGameMatch(Long id, GameMove playerMove, GameMove computerMove, GameResult result) {
        GameMatch match = new GameMatch();
        match.setId(id);
        match.setPlayerMove(playerMove);
        match.setComputerMove(computerMove);
        match.setResult(result);
        match.setUser(testUser);
        return match;
    }
}
