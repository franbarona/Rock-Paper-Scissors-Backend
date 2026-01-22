package com.rockpaperscissors.enums;

import lombok.Getter;

@Getter
public enum GameMove {
    ROCK("ROCK"),
    PAPER("PAPER"),
    SCISSORS("SCISSORS");

    private final String name;

    GameMove(String name) {
        this.name = name;
    }
}
