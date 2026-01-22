package com.rockpaperscissors.dto.request;

import com.rockpaperscissors.enums.GameMove;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GamePlayRequest {

    @NotBlank(message = "Move cannot be blank")
    private GameMove move;
}
