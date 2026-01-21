package com.rockpaperscissors.enums;

public enum GameMove {
    ROCK("ROCK"),
    PAPER("PAPER"),
    SCISSORS("SCISSORS");

    private String name;

    GameMove(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
