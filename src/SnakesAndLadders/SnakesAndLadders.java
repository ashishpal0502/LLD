package SnakesAndLadders;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Class representing a player in the game
class Player {
    private String name;
    private int position;

    public Player(String name) {
        this.name = name;
        this.position = 0; // Starting position
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

// Class representing the Snakes and Ladders game
class SnakesAndLaddersGame {
    private final int boardSize;
    private Map<Integer, Integer> snakes;
    private Map<Integer, Integer> ladders;
    private BlockingQueue<Player> playerQueue;
    private ExecutorService executorService;

    public SnakesAndLaddersGame(int boardSize, int numPlayers) {
        this.boardSize = boardSize;
        this.snakes = new HashMap<>();
        this.ladders = new HashMap<>();
        this.playerQueue = new ArrayBlockingQueue<>(numPlayers);
        this.executorService = Executors.newSingleThreadExecutor();
        initializeBoard();
    }

    private void initializeBoard() {
        // Define snakes and ladders
        snakes.put(16, 6);
        snakes.put(47, 26);
        snakes.put(49, 11);
        snakes.put(56, 53);
        snakes.put(62, 19);
        snakes.put(64, 60);
        snakes.put(87, 24);
        snakes.put(93, 73);
        snakes.put(95, 75);
        snakes.put(98, 78);

        ladders.put(1, 38);
        ladders.put(4, 14);
        ladders.put(9, 31);
        ladders.put(21, 42);
        ladders.put(28, 84);
        ladders.put(36, 44);
        ladders.put(51, 67);
        ladders.put(71, 91);
        ladders.put(80, 100);
    }

    public void addPlayer(Player player) {
        try {
            playerQueue.put(player);
        } catch (InterruptedException e) {
            System.err.println("Error adding player: " + e.getMessage());
        }
    }

    public void playGame() {
        executorService.execute(() -> {
            while (true) {
                try {
                    Player player = playerQueue.take();
                    int diceRoll = rollDice();
                    int newPosition = player.getPosition() + diceRoll;
                    if (newPosition > boardSize) {
                        newPosition = boardSize - (newPosition - boardSize);
                    }
                    System.out.println(player.getName() + " rolled " + diceRoll + " and moved to position " + newPosition);
                    if (snakes.containsKey(newPosition)) {
                        newPosition = snakes.get(newPosition);
                        System.out.println("Oops! " + player.getName() + " got bitten by a snake and moved down to position " + newPosition);
                    }
                    if (ladders.containsKey(newPosition)) {
                        newPosition = ladders.get(newPosition);
                        System.out.println("Yay! " + player.getName() + " climbed a ladder and moved up to position " + newPosition);
                    }
                    player.setPosition(newPosition);
                    if (newPosition == boardSize) {
                        System.out.println(player.getName() + " wins!");
                        shutdown();
                        break;
                    }
                    playerQueue.put(player);
                } catch (InterruptedException e) {
                    System.err.println("Error playing game: " + e.getMessage());
                }
            }
        });
    }

    private int rollDice() {
        return new Random().nextInt(6) + 1; // Roll a 6-sided dice
    }

    public void shutdown() {
        executorService.shutdown();
    }
}

// Main class for simulating Snakes and Ladders game
public class SnakesAndLadders {
    public static void main(String[] args) {
        SnakesAndLaddersGame game = new SnakesAndLaddersGame(100, 2); // Board size: 100, Number of players: 2

        // Add players
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        game.addPlayer(player1);
        game.addPlayer(player2);

        // Start the game
        game.playGame();
    }
}
