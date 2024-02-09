package TicTacToe;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Class representing a player in Tic Tac Toe game
class Player {
    private String name;
    private char symbol;

    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }
}

// Class representing the Tic Tac Toe game
class TicTacToeGame {
    private char[][] board;
    private BlockingQueue<Player> playerQueue;
    private ExecutorService executorService;

    public TicTacToeGame() {
        this.board = new char[3][3];
        this.playerQueue = new ArrayBlockingQueue<>(2);
        this.executorService = Executors.newSingleThreadExecutor();
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
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
            Player currentPlayer = null;
            boolean gameWon = false;
            int moves = 0;

            while (!gameWon && moves < 9) {
                try {
                    currentPlayer = playerQueue.take();
                    makeMove(currentPlayer);
                    printBoard();
                    gameWon = checkWin(currentPlayer);
                    moves++;
                    playerQueue.put(currentPlayer);
                } catch (InterruptedException e) {
                    System.err.println("Error playing game: " + e.getMessage());
                }
            }

            if (!gameWon) {
                System.out.println("It's a draw!");
            } else {
                System.out.println(currentPlayer.getName() + " wins!");
            }
            shutdown();
        });
    }

    private void makeMove(Player player) {
        Scanner scanner = new Scanner(System.in);
        int row, col;

        System.out.println(player.getName() + "'s turn (" + player.getSymbol() + ")");
        do {
            System.out.print("Enter row (0-2): ");
            row = scanner.nextInt();
            System.out.print("Enter column (0-2): ");
            col = scanner.nextInt();
        } while (row < 0 || row > 2 || col < 0 || col > 2 || board[row][col] != '-');

        board[row][col] = player.getSymbol();
    }

    private void printBoard() {
        System.out.println("Board:");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private boolean checkWin(Player player) {
        char symbol = player.getSymbol();

        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) {
                return true; // Row win
            }
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) {
                return true; // Column win
            }
        }

        // Check diagonals
        if ((board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
                (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol)) {
            return true; // Diagonal win
        }

        return false;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}

// Main class for Tic Tac Toe game
public class TicTacToe {
    public static void main(String[] args) {
        TicTacToeGame game = new TicTacToeGame();

        // Create players
        Player player1 = new Player("Player 1", 'X');
        Player player2 = new Player("Player 2", 'O');

        // Add players to the game
        game.addPlayer(player1);
        game.addPlayer(player2);

        // Start the game
        game.playGame();
    }
}

