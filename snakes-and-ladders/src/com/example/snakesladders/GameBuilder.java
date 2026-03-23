package com.example.snakesladders;

import java.util.ArrayList;
import java.util.List;

public class GameBuilder {
    private int boardSize;
    private int numberOfPlayers;
    private DifficultyLevel difficulty;

    public GameBuilder(int boardSize, int numberOfPlayers, DifficultyLevel difficulty) {
        this.boardSize = boardSize;
        this.numberOfPlayers = numberOfPlayers;
        this.difficulty = difficulty;
    }

    public Game build() {
        Board board = new Board(boardSize, boardSize, boardSize, difficulty);
        Dice dice = new Dice(6);

        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            players.add(new Player("Player" + i));
        }

        return new Game(board, players, dice);
    }
}
