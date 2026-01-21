package com.rockpaperscissors.dto.request;

import com.rockpaperscissors.model.GameMove;

import jakarta.validation.constraints.NotBlank;

public class GamePlayRequest {
    
    @NotBlank(message = "Move cannot be blank")
    private GameMove move;

    //Getters and Setters
    public GameMove getMove() {
        return move;
    }

    public void setMove(GameMove move) {
        this.move = move;
    }
}
