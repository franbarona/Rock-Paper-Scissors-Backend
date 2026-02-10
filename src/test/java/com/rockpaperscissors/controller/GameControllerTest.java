package com.rockpaperscissors.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockpaperscissors.dto.request.GamePlayRequest;
import com.rockpaperscissors.dto.response.GamePlayResponse;
import com.rockpaperscissors.enums.GameMove;
import com.rockpaperscissors.enums.GameResult;
import com.rockpaperscissors.service.GameService;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("GameController Tests")
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private GameService gameService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TEST_USER_ID_STRING = "550e8400-e29b-41d4-a716-446655440000";
    private static final UUID TEST_USER_ID = UUID.fromString(TEST_USER_ID_STRING);
    private static final String API_BASE = "/api/game";

    private GamePlayRequest gamePlayRequest;
    private GamePlayResponse gamePlayResponse;

    @BeforeEach
    void setUp() {
        gamePlayRequest = GamePlayRequest.builder()
                .move(GameMove.ROCK)
                .build();

        gamePlayResponse = GamePlayResponse.builder()
                .playerMove(GameMove.ROCK)
                .computerMove(GameMove.SCISSORS)
                .result(GameResult.WIN)
                .build();
    }

    // ==================== playMove() ====================

    @Test
    @DisplayName("Should play move successfully")
    @WithMockUser(username = TEST_USER_ID_STRING)
    void testPlayMoveSuccessfully() throws Exception {
        when(gameService.playMove(any(GameMove.class), eq(TEST_USER_ID)))
                .thenReturn(gamePlayResponse);

        mockMvc.perform(post(API_BASE + "/play")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gamePlayRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Game played successfully"))
                .andExpect(jsonPath("$.data.playerMove").value("ROCK"))
                .andExpect(jsonPath("$.data.computerMove").value("SCISSORS"))
                .andExpect(jsonPath("$.data.result").value("WIN"));
    }

    @Test
    @DisplayName("Should return unauthorized when playing move without authentication")
    void testPlayMoveUnauthorized() throws Exception {
        mockMvc.perform(post(API_BASE + "/play")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gamePlayRequest)))
                .andExpect(status().isUnauthorized());
    }

    // ==================== getHistory() ====================

    @Test
    @DisplayName("Should get game history successfully")
    @WithMockUser(username = TEST_USER_ID_STRING)
    void testGetHistorySuccessfully() throws Exception {
        GamePlayResponse response1 = GamePlayResponse.builder()
                .playerMove(GameMove.ROCK)
                .computerMove(GameMove.SCISSORS)
                .result(GameResult.WIN)
                .build();

        GamePlayResponse response2 = GamePlayResponse.builder()
                .playerMove(GameMove.PAPER)
                .computerMove(GameMove.PAPER)
                .result(GameResult.DRAW)
                .build();

        List<GamePlayResponse> history = List.of(response1, response2);
        when(gameService.getHistory(TEST_USER_ID)).thenReturn(history);

        mockMvc.perform(get(API_BASE + "/history")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Game history retrieved successfully"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].playerMove").value("ROCK"))
                .andExpect(jsonPath("$.data[0].result").value("WIN"))
                .andExpect(jsonPath("$.data[1].playerMove").value("PAPER"))
                .andExpect(jsonPath("$.data[1].result").value("DRAW"));
    }

    @Test
    @DisplayName("Should get empty history when user has no games")
    @WithMockUser(username = TEST_USER_ID_STRING)
    void testGetEmptyHistory() throws Exception {
        when(gameService.getHistory(TEST_USER_ID)).thenReturn(List.of());

        mockMvc.perform(get(API_BASE + "/history")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    @DisplayName("Should return unauthorized when getting history without authentication")
    void testGetHistoryUnauthorized() throws Exception {
        mockMvc.perform(get(API_BASE + "/history")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}