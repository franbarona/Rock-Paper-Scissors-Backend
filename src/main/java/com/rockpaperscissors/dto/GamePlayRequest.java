package com.rockpaperscissors.dto;

import com.rockpaperscissors.model.GameMove;

public class GamePlayRequest {
    private GameMove move;

    public GameMove getMove() {
        return move;
    }

    public void setMove(GameMove move) {
        this.move = move;
    }
}
