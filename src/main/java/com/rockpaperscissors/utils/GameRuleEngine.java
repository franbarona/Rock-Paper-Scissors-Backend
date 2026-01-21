package com.rockpaperscissors.utils;

import com.rockpaperscissors.enums.GameMove;
import com.rockpaperscissors.enums.GameResult;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class GameRuleEngine {

    public static GameResult determineWinner(GameMove player, GameMove computer) {
        if (player == computer)
            return GameResult.DRAW;

        return switch (player) {
            case ROCK -> computer == GameMove.SCISSORS ? GameResult.WIN : GameResult.LOSS;
            case PAPER -> computer == GameMove.ROCK ? GameResult.WIN : GameResult.LOSS;
            case SCISSORS -> computer == GameMove.PAPER ? GameResult.WIN : GameResult.LOSS;
        };
    }

}
