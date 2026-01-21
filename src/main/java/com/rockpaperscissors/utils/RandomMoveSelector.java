package com.rockpaperscissors.utils;

import java.util.Random;

import com.rockpaperscissors.enums.GameMove;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RandomMoveSelector {
    private static final Random random = new Random();

    public static GameMove selectRandomMove() {
        GameMove[] moves = GameMove.values();
        return moves[random.nextInt(moves.length)];
    }
}
