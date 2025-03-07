package engine;

import engine.game.Game;
import engine.game.Timer;
import engine.types.Move;
import engine.types.Position;
import engine.utils.FEN;
import engine.utils.MoveUtils;
import engine.utils.PGN;

import java.util.Scanner;

/**
 * A command-line tool for playing a chess-like game.
 * Handles game initialization, input parsing, move validation, and game flow.
 */
public class CommandLineGame {
    private static final int DEFAULT_GAME_DURATION_MS = 600_000; // 10 minutes
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Runs the main game flow.
     * It initializes the game state, displays the game, and processes player input in a loop.
     */
    private void run() {
        Timer timer = new Timer(DEFAULT_GAME_DURATION_MS, 0);
        Game game = initializeGame(timer); // Initialize the game

        // Main game loop
        while (game.inPlay()) {
            displayGame(game); // Display the game state

            String input = getPlayerInput(game);
            if (isExitCommand(input)) {
                System.out.println("\n");
                System.out.println("FEN: " + FEN.getFEN(game));
                System.out.println("PGN: " + PGN.getPGN(game));
                System.out.println("Thanks for playing!");
                System.exit(0);
            }

            processPlayerInput(input, game);
        }
        System.out.println("\n");
        System.out.println("Game Over!");
        System.out.println("Result: " + game.getResult());
        System.out.println("PGN: " + PGN.getPGN(game));
    }

    /**
     * Initializes the game by either starting a new game or loading from a FEN or PGN.
     *
     * @param timer The game's timer to manage the duration.
     * @return The initialized `Game` object.
     */
    private Game initializeGame(Timer timer) {
        System.out.print("Import Game? (y, n): ");
        String input = scanner.nextLine();

        // Import Game
        if (input.equalsIgnoreCase("y")) {
            System.out.print("\tImport FEN   (1)\n\tImport PGN   (2)\n\tWhich one? ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("1")) {
                // Import FEN
                System.out.print("Enter FEN: ");
                input = scanner.nextLine().trim();
                try {
                    return FEN.getGame(input, timer);
                } catch (Exception e) {
                    System.out.println("Not a valid FEN string.");
                }
            } else if (input.equalsIgnoreCase("2")) {
                // Import PGN
                System.out.print("Enter PGN: ");
                input = scanner.nextLine().trim();
                try {
                    return PGN.getGame(input);
                } catch (Exception e) {
                    System.out.println("Not a valid PGN string.");
                }
            }
        }

        // New Game
        return new Game(timer); // Default game creation
    }

    /**
     * Displays the current state of the game on the command line.
     *
     * @param game The current `Game` object to display.
     */
    private void displayGame(Game game) {
        System.out.println("\n");
        System.out.println(game.board);
        System.out.println(game.getTimer());
    }

    /**
     * Retrieves the current player's input from the command line.
     *
     * @param game The current `Game` object to provide player context.
     * @return The raw string input from the player.
     */
    private String getPlayerInput(Game game) {
        System.out.print(game.getTurn() + "'s Move: ");
        return scanner.nextLine();
    }

    /**
     * Checks whether the player entered the exit command (an empty input).
     *
     * @param input The player's raw input string.
     * @return `true` if the input is empty, indicating an exit command.
     */
    private boolean isExitCommand(String input) {
        return input.isEmpty();
    }

    /**
     * Processes the player's input by parsing it and either displaying legal moves
     * or executing a move if valid.
     *
     * @param game  The current `Game` object.
     * @param input The player's raw input string.
     */
    private void processPlayerInput(String input, Game game) {
        // Display legal moves if only one position is provided
        if (input.length() == 2) {
            try {
                Position position = Position.fromAlgebraic(input);
                System.out.println("Legal moves from '" + position + "' -> " + game.getLegalMoves(position));
            } catch (Exception e) {
                System.out.println("Error: A legal position looks like this: 'e2'");
            }
            return;
        }

        // Len must be 2
        if (input.length() < 4 || input.length() > 5) {
            System.out.println("Error: A legal move looks like this: 'e2e4'");
            return;
        }

        // Process the move
        handlePlayerMove(game, input);
    }

    /**
     * Handles the player's move by validating it and applying it to the game if legal.
     *
     * @param game         The current `Game` object.
     * @param moveNotation A long algebraic notation of the move.
     */
    private void handlePlayerMove(Game game, String moveNotation) {
        Move move;

        // Try to make vars
        try {
            move = MoveUtils.fromLongAlgebraic(moveNotation, game);
        } catch (Exception e) {
            System.err.println("Error: A legal move looks like this: 'e2e4'");
            System.err.println("Error Message: " + e.getMessage());
            return;
        }

        // Try to make move
        if (!game.isMoveLegal(move)) {
            System.err.println("Error: Move '" + moveNotation + "' is not legal.");
            if (game.isMoveSafe(move)) {
                System.err.println(moveNotation + " leaves king in check.");
            } else if (game.board.getPieceAt(move.initialPosition()) == null) {
                System.err.println("No piece at the initial position to move.");
            }
            System.out.println("\n");
        } else {
            game.move(move);
        }
    }

    /**
     * Entry point for the command-line game.
     * Responsible for creating the CommandLineGame instance and running the game.
     */
    public static void main(String[] args) {
        // Display instructions for the player
        System.out.println("Welcome to Command Line Chess Game!");
        System.out.println("Instructions:");
        System.out.println("1. To display all legal moves from a position, enter the position (e.g., 'e2').");
        System.out.println("2. To make a move, enter the long algebraic notation of that move (e.g., 'e2e4').");
        System.out.println("3. To quit the game at any time, simply press Enter without typing anything.");
        System.out.println();

        // Initialize and run the game
        CommandLineGame gameRunner = new CommandLineGame();
        gameRunner.run(); // Start the game loop
    }
}
