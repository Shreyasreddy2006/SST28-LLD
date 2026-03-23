package com.example.snakesladders;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Game {
    private Board board;
    private Queue<Player> players;
    private List<Player> winners;
    private Dice dice;
    private List<String> moveLog;

    public Game(Board board, List<Player> playerList, Dice dice) {
        this.board = board;
        this.players = new LinkedList<>(playerList);
        this.winners = new ArrayList<>();
        this.dice = dice;
        this.moveLog = new ArrayList<>();
    }

    public void playTurn() {
        if (isGameOver()) {
            return;
        }

        Player current = players.poll();
        int diceValue = dice.roll();
        int oldPosition = current.getPosition();
        int newPosition = oldPosition + diceValue;

        if (newPosition > board.getTotalCells()) {
            moveLog.add(current.getName() + " rolled " + diceValue + " at position " + oldPosition + " -> stays (would exceed board)");
            players.add(current);
            return;
        }

        if (newPosition == board.getTotalCells()) {
            current.setPosition(newPosition);
            current.markAsWinner();
            winners.add(current);
            moveLog.add(current.getName() + " rolled " + diceValue + " at position " + oldPosition + " -> reached " + newPosition + " and WON!");
            return;
        }

        int finalPosition = board.getFinalPosition(newPosition);

        if (board.hasSnakeAt(newPosition)) {
            moveLog.add(current.getName() + " rolled " + diceValue + " at position " + oldPosition + " -> " + newPosition + " bitten by snake, goes to " + finalPosition);
        } else if (board.hasLadderAt(newPosition)) {
            moveLog.add(current.getName() + " rolled " + diceValue + " at position " + oldPosition + " -> " + newPosition + " climbed ladder to " + finalPosition);
        } else {
            moveLog.add(current.getName() + " rolled " + diceValue + " at position " + oldPosition + " -> " + finalPosition);
        }

        current.setPosition(finalPosition);
        players.add(current);
    }

    public boolean isGameOver() {
        return players.size() <= 1;
    }

    public void playFullGame() {
        while (!isGameOver()) {
            playTurn();
        }
    }

    public List<Player> getWinners() {
        return winners;
    }

    public List<String> getMoveLog() {
        return moveLog;
    }

    public Queue<Player> getRemainingPlayers() {
        return players;
    }

    public Board getBoard() {
        return board;
    }
}
