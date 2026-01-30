package com.rockpaperscissors.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatisticsResponse {

    private Long userId;
    private String username;
    private Integer totalMatches;
    private Integer totalWins;
    private Integer totalLosses;
    private Integer totalDraws;
}