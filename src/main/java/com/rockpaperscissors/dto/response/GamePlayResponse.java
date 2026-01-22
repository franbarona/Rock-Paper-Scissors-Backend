package com.rockpaperscissors.dto.response;

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
}