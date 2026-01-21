package com.rockpaperscissors.dto.response;

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
    private String playerMove;
    private String computerMove;
    private String result;
}