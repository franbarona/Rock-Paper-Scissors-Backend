package com.rockpaperscissors.enums;

import lombok.Getter;

@Getter
public enum GameResult {
    WIN("WIN"),
    LOSS("LOSS"),
    DRAW("DRAW");

    private final String name;

    GameResult(String name) {
        this.name = name;
    }
}