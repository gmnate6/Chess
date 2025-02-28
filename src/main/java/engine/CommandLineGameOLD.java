package engine;

import engine.game.Game;
import engine.game.Timer;
import engine.types.Move;
import engine.types.Position;
import engine.utils.FEN;
import engine.utils.PGN;

import java.util.Scanner;

/**
 * A command-line tool for playing a chess-like game.
 * Handles game initialization, input parsing, move validation, and game flow.
 */
public class CommandLineGameOLD {
    private static final int DEFAULT_GAME_DURATION_MS = 600_000; // 10 minutes
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Runs the main game flow.
     * It initializes the game state, displays the game, and processes player input in a loop.
     */
    private void run() {
        Timer timer = new Timer(DEFAULT_GAME_DURATION_MS, 0);
        Game game = initializeGame(timer); // Initialize the game

        System.out.println("\n");

        // Main game loop
        while (game.inPlay()) {
            displayGame(game); // Display the game state

            String input = getPlayerInput(game);
            if (isExitCommand(input)) break;

            processPlayerInput(game, input);
        }
        System.out.println("\n");
        System.out.println("Game Over!");
        System.out.println("Result: " + game.getResult());
        System.out.println(PGN.getPGN(game));
    }

    /**
     * Initializes the game by either starting a new game or loading from a FEN string.
     *
     * @param timer The game's timer to manage the duration.
     * @return The initialized `Game` object.
     */
    private Game initializeGame(Timer timer) {
        System.out.print("Import FEN? (y, n): ");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("y")) {
            System.out.print("Enter FEN: ");
            input = scanner.nextLine().trim();
            return FEN.getGame(input, timer);
        }
        return new Game(timer); // Default game creation
    }

    /**
     * Displays the current state of the game on the command line.
     *
     * @param game The current `Game` object to display.
     */
    private void displayGame(Game game) {
        System.out.println("\n");
        System.out.println(game);
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
    private void processPlayerInput(Game game, String input) {
        String[] positions = input.split(" ");

        // Display legal moves if only one position is provided
        if (positions.length == 1) {
            try {
                Position position = Position.fromAlgebraic(positions[0]);
                System.out.println("Legal moves from '" + position + "' -> " + game.getLegalMoves(position));
            } catch (Exception e) {
                System.out.println("Error: A legal position looks like this: 'e2'");
            }
            return;
        }

        // Len must be 2
        if (positions.length != 2) {
            System.out.println("Error: A legal move looks like this: 'e2 e4'");
            return;
        }

        // Process the move
        handlePlayerMove(game, positions);
    }

    /**
     * Handles the player's move by validating it and applying it to the game if legal.
     *
     * @param game      The current `Game` object.
     * @param positions An array containing the initial and final positions of the move.
     */
    private void handlePlayerMove(Game game, String[] positions) {
        Move move;

        // Try to make vars
        try {
            Position initialPosition = Position.fromAlgebraic(positions[0]);
            Position finalPosition = Position.fromAlgebraic(positions[1]);
            move = new Move(initialPosition, finalPosition, '\0');
        } catch (Exception e) {
            System.out.println("Error: A legal move looks like this: 'e2 e4'");
            return;
        }

        // Try to make move
        if (!game.isMoveLegal(move)) {
            System.out.println("Error: Move '" + move + "' is not legal.");
            System.out.println("\n");
        } else {
            game.move(move);
            System.out.println("FEN: " + FEN.getFEN(game));
            System.out.println("\n");
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
        System.out.println("2. To make a move, enter the initial and final positions separated by a space (e.g., 'e2 e4').");
        System.out.println("3. To quit the game at any time, simply press Enter without typing anything.");
        System.out.println();

        // Initialize and run the game
        CommandLineGameOLD gameRunner = new CommandLineGameOLD();
        gameRunner.run(); // Start the game loop
    }
}
