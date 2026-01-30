package com.rockpaperscissors.dto.response;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rockpaperscissors.enums.GameMove;
import com.rockpaperscissors.enums.GameResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GamePlayResponse {

    private Long id;
    private GameMove playerMove;
    private GameMove computerMove;
    private GameResult result;
    private String playedAt;
}