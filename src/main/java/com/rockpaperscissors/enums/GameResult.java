package com.rockpaperscissors.enums;

public enum GameResult {
    WIN("WIN"),
    LOSS("LOSS"),
    DRAW("DRAW");

    private String name;

    GameResult(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}