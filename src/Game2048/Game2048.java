package Game2048;

import java.util.Random;

// Enum representing the direction of movement
enum Direction {
    UP, DOWN, LEFT, RIGHT
}

// Class representing the 2048 game
public class Game2048 {
    private final int SIZE = 4; // Size of the board
    private int[][] board;
    private int score;

    public Game2048() {
        board = new int[SIZE][SIZE];
        score = 0;
        initializeBoard();
    }

    private void initializeBoard() {
        // Initialize the board with two random tiles
        generateRandomTile();
        generateRandomTile();
    }

    private void generateRandomTile() {
        Random random = new Random();
        int value = random.nextInt(2) == 0 ? 2 : 4; // Randomly choose 2 or 4
        int row, col;

        do {
            row = random.nextInt(SIZE);
            col = random.nextInt(SIZE);
        } while (board[row][col] != 0); // Keep choosing until an empty cell is found

        board[row][col] = value;
    }

    public void move(Direction direction) {
        // Implement the move logic based on the direction
        // ...
        // For simplicity, let's assume all moves are valid and just generate a random tile after each move
        generateRandomTile();
    }

    public boolean isGameOver() {
        // Check if the game is over (no more valid moves)
        // ...
        return false; // For simplicity, always return false
    }

    public void printBoard() {
        // Print the current state of the board
        for (int[] row : board) {
            for (int cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }

    // Other methods for checking if a move is possible, calculating score, etc.

    public static void main(String[] args) {
        Game2048 game = new Game2048();
        game.printBoard();

        // Sample game loop
        while (!game.isGameOver()) {
            // Assume user inputs direction (UP, DOWN, LEFT, RIGHT)
            Direction direction = Direction.UP; // Example direction
            game.move(direction);
            game.printBoard();
        }
    }
}

