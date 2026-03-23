
## Design Approach

Each class does one thing.

**Snake & Ladder** — Simple data holders. A snake has a head (higher number) and a tail (lower number). A ladder has a start (lower) and an end (higher). That's it.

**Board** — Owns all the snakes and ladders. Has two constructors: one that randomly generates them (taking size, count, and difficulty), and one that takes pre-built lists (useful for testing with known positions). Internally it keeps two HashMaps — `snakeMap` (head → tail) and `ladderMap` (start → end) — so `getFinalPosition()` is O(1). The random generation makes sure no two snakes/ladders share a cell, and cell 1 and the last cell are never used.

**Difficulty** — In EASY mode, ladders tend to span from the bottom half to the top half (big jumps up), and snakes are spread anywhere. In HARD mode, snakes cluster in the top half of the board (harder to finish), and ladders give shorter jumps.

**Player** — Tracks name, current position (starts at 0, meaning off the board), and whether they've won.

**Dice** — Wraps `Random` with a configurable number of faces. The `setSeed()` method exists so tests can produce predictable rolls.

**Game** — The core engine. Uses a `Queue<Player>` for turn order — poll the front, let them play, push them back to the end. Each `playTurn()`:
1. Rolls the dice
2. If new position exceeds the board, player stays put
3. If new position equals the last cell, player wins and is removed from the queue
4. Otherwise, checks for snake/ladder at that cell and moves accordingly
5. Logs every move

The game ends when only 1 player is left in the queue.

**GameBuilder** — Convenience class that wires up a Board + Dice + Players from just three inputs: board size, player count, and difficulty.

## How to Run

```bash
cd snakes-and-ladders
javac -d out src/com/example/snakesladders/*.java
java -cp out com.example.snakesladders.SnakesLaddersTest
```
