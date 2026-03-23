package com.example.snakesladders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SnakesLaddersTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testPlayerStartsAtZero();
        testDiceGivesValueBetween1And6();
        testSnakeBringsPlayerDown();
        testLadderTakesPlayerUp();
        testPlayerDoesNotMoveBeyondBoard();
        testPlayerWinsOnExactLastCell();
        testGameEndsWhenOnePlayerLeft();
        testTurnOrderIsRoundRobin();
        testFullGameWithFixedDice();
        testBoardHasCorrectNumberOfSnakesAndLadders();
        testSnakesAndLaddersDontOverlap();
        testNoCycleInSnakesAndLadders();
        testGameBuilderCreatesValidGame();

        System.out.println("\n========== RESULTS ==========");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total:  " + (passed + failed));

        if (failed > 0) {
             System.exit(1);
        }
    }

    private static void testPlayerStartsAtZero() {
         Player p = new Player("Alice");
         check("Player starts at position 0", p.getPosition() == 0);
    }

    private static void testDiceGivesValueBetween1And6() {
        Dice dice = new Dice(6);
        boolean allInRange = true;
        for (int i = 0; i < 1000; i++) {
            int val = dice.roll();
            if (val < 1 || val > 6) {
                allInRange = false;
                break;
            }
        }
        check("Dice always gives value between 1 and 6", allInRange);
    }

    private static void testSnakeBringsPlayerDown() {
        List<Snake> snakes = Arrays.asList(new Snake(15, 5));
        List<Ladder> ladders = new ArrayList<>();
        Board board = new Board(10, snakes, ladders);

        Player p = new Player("Alice");
        List<Player> players = Arrays.asList(p, new Player("Bob"));

        Dice dice = new Dice(6);
        dice.setSeed(0);

        Game game = new Game(board, players, dice);

        p.setPosition(10);
        int diceVal = 5;
        int newPos = 10 + diceVal;
        int finalPos = board.getFinalPosition(newPos);

        check("Snake at 15 brings player down to 5", finalPos == 5);
    }

    private static void testLadderTakesPlayerUp() {
        List<Snake> snakes = new ArrayList<>();
        List<Ladder> ladders = Arrays.asList(new Ladder(8, 45));
        Board board = new Board(10, snakes, ladders);

        int finalPos = board.getFinalPosition(8);
        check("Ladder at 8 takes player up to 45", finalPos == 45);
    }

    private static void testPlayerDoesNotMoveBeyondBoard() {
        List<Snake> snakes = new ArrayList<>();
        List<Ladder> ladders = new ArrayList<>();
        Board board = new Board(10, snakes, ladders);

        Player alice = new Player("Alice");
        Player bob = new Player("Bob");
        alice.setPosition(98);
        List<Player> players = Arrays.asList(alice, bob);

        Dice dice = new Dice(6);
        dice.setSeed(42);
        Game game = new Game(board, players, dice);

        game.playTurn();

        String lastLog = game.getMoveLog().get(game.getMoveLog().size() - 1);
        boolean stayed = alice.getPosition() == 98 || alice.getPosition() == 100;
        check("Player at 98 either stays or wins (no overshooting)", stayed);
    }

    private static void testPlayerWinsOnExactLastCell() {
        List<Snake> snakes = new ArrayList<>();
        List<Ladder> ladders = new ArrayList<>();
        Board board = new Board(10, snakes, ladders);

        Player alice = new Player("Alice");
        Player bob = new Player("Bob");
        alice.setPosition(96);
        List<Player> players = Arrays.asList(alice, bob);

        Dice fixedDice = new Dice(6) {
            @Override
            public int roll() {
                return 4;
            }
        };

        Game game = new Game(board, players, fixedDice);
        game.playTurn();

        check("Player at 96 rolls 4, reaches 100 and wins", alice.getPosition() == 100 && alice.hasWon());
    }

    private static void testGameEndsWhenOnePlayerLeft() {
        List<Snake> snakes = new ArrayList<>();
        List<Ladder> ladders = new ArrayList<>();
        Board board = new Board(10, snakes, ladders);

        Player alice = new Player("Alice");
        Player bob = new Player("Bob");
        alice.setPosition(96);
        bob.setPosition(0);

        Dice fixedDice = new Dice(6) {
            @Override
            public int roll() {
                return 4;
            }
        };

        Game game = new Game(board, Arrays.asList(alice, bob), fixedDice);
        game.playTurn();

        check("After Alice wins, only Bob remains, game is over", game.isGameOver());
    }

    private static void testTurnOrderIsRoundRobin() {
        List<Snake> snakes = new ArrayList<>();
        List<Ladder> ladders = new ArrayList<>();
        Board board = new Board(10, snakes, ladders);

        Player alice = new Player("Alice");
        Player bob = new Player("Bob");
        Player charlie = new Player("Charlie");

        Dice fixedDice = new Dice(6) {
            @Override
            public int roll() {
                return 1;
            }
        };

        Game game = new Game(board, Arrays.asList(alice, bob, charlie), fixedDice);

        game.playTurn();
        game.playTurn();
        game.playTurn();

        List<String> log = game.getMoveLog();
        boolean correctOrder = log.get(0).startsWith("Alice")
                && log.get(1).startsWith("Bob")
                && log.get(2).startsWith("Charlie");

        check("Turns go Alice -> Bob -> Charlie", correctOrder);
    }

    private static void testFullGameWithFixedDice() {
        List<Snake> snakes = new ArrayList<>();
        List<Ladder> ladders = new ArrayList<>();
        Board board = new Board(10, snakes, ladders);

        Player alice = new Player("Alice");
        Player bob = new Player("Bob");

        Dice fixedDice = new Dice(6) {
            private int count = 0;
            @Override
            public int roll() {
                count++;
                return 5;
            }
        };

        Game game = new Game(board, Arrays.asList(alice, bob), fixedDice);
        game.playFullGame();

        check("Full game ends with at least one winner", game.getWinners().size() >= 1);
        check("Full game ends with at most 1 player remaining", game.getRemainingPlayers().size() <= 1);
    }

    private static void testBoardHasCorrectNumberOfSnakesAndLadders() {
        Board board = new Board(10, 10, 10, DifficultyLevel.EASY);
        check("Board has 10 snakes", board.getSnakes().size() == 10);
        check("Board has 10 ladders", board.getLadders().size() == 10);
    }

    private static void testSnakesAndLaddersDontOverlap() {
        Board board = new Board(10, 10, 10, DifficultyLevel.EASY);

        java.util.Set<Integer> allPositions = new java.util.HashSet<>();
        boolean noOverlap = true;

        for (Snake s : board.getSnakes()) {
            if (!allPositions.add(s.getHead())) noOverlap = false;
            if (!allPositions.add(s.getTail())) noOverlap = false;
        }
        for (Ladder l : board.getLadders()) {
            if (!allPositions.add(l.getStart())) noOverlap = false;
            if (!allPositions.add(l.getEnd())) noOverlap = false;
        }

        check("No snake/ladder positions overlap", noOverlap);
    }

    private static void testNoCycleInSnakesAndLadders() {
        Board board = new Board(10, 10, 10, DifficultyLevel.HARD);

        boolean noCycle = true;
        for (int i = 1; i <= board.getTotalCells(); i++) {
            int pos = board.getFinalPosition(i);
            int next = board.getFinalPosition(pos);
            if (next != pos) {
                noCycle = false;
                break;
            }
        }

        check("No cycles: landing after a snake/ladder doesn't trigger another", noCycle);
    }

    private static void testGameBuilderCreatesValidGame() {
        GameBuilder builder = new GameBuilder(10, 3, DifficultyLevel.EASY);
        Game game = builder.build();

        check("GameBuilder creates game with 3 players", game.getRemainingPlayers().size() == 3);
        check("GameBuilder board has 100 cells", game.getBoard().getTotalCells() == 100);
    }

    private static void check(String testName, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName);
            failed++;
        }
    }
}
