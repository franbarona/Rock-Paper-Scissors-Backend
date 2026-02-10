package com.rockpaperscissors.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.rockpaperscissors.dto.response.UserStatisticsResponse;
import com.rockpaperscissors.service.StatisticsService;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("StatisticsController Tests")
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StatisticsService statisticsService;

    private static final String TEST_USER_ID_STRING = "550e8400-e29b-41d4-a716-446655440000";
    private static final UUID TEST_USER_ID = UUID.fromString(TEST_USER_ID_STRING);
    private static final String API_BASE = "/api/statistics";

    private UserStatisticsResponse userStatisticsResponse;

    @BeforeEach
    void setUp() {
        userStatisticsResponse = UserStatisticsResponse.builder()
                .totalMatches(10)
                .totalWins(6)
                .totalLosses(2)
                .totalDraws(2)
                .build();
    }

    // ==================== getMyStatistics() ====================

    @Test
    @DisplayName("Should get user statistics successfully")
    @WithMockUser(username = TEST_USER_ID_STRING)
    void testGetMyStatisticsSuccessfully() throws Exception {
        when(statisticsService.getUserStatistics(TEST_USER_ID))
                .thenReturn(userStatisticsResponse);

        mockMvc.perform(get(API_BASE + "/my-stats")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Statistics retrieved successfully"))
                .andExpect(jsonPath("$.data.totalMatches").value(10))
                .andExpect(jsonPath("$.data.totalWins").value(6))
                .andExpect(jsonPath("$.data.totalLosses").value(2))
                .andExpect(jsonPath("$.data.totalDraws").value(2));
    }

    @Test
    @DisplayName("Should return unauthorized when getting statistics without authentication")
    void testGetMyStatisticsUnauthorized() throws Exception {
        mockMvc.perform(get(API_BASE + "/my-stats")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}