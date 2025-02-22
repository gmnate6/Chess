package tests;

import engine.Game;

import engine.board.Position;

import engine.utils.Move;
import engine.utils.Timer;

import java.util.Scanner;

public class CommandLineGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;

        // Create Game / Timer
        Game game;
        Timer timer = new Timer(600_000, 0); // 10 minute game

        // Import FEN?
        System.out.print("Import FEN? (y, n): ");
        input = scanner.nextLine();
        if (!input.equalsIgnoreCase("y")) {
            // Start Game
            game = new Game(timer);
        } else {
            // Load from FEN
            System.out.print("Enter FEN: ");
            input = scanner.nextLine();
            game = Game.fromFEN(input, timer);
        }
        System.out.println("\n");

        // Main Loop
        while (game.isGameInPlay()) {
            // Print Game
            System.out.println("\n");
            System.out.println(game);
            System.out.print(game.getCurrentPlayer().toString() + "'s Move: ");
            input = scanner.nextLine();

            // Break
            if (input.isEmpty()) { break; }

            // Split
            String[] positions = input.split(" ");

            // Initial Position
            Position initialPosition = Position.fromAlgebraic(positions[0]);

            // SOUT Moves
            if (positions.length == 1) {
                System.out.println(game.getLegalMoves(initialPosition));
                continue;
            }

            // Final Position
            Position finalPosition = Position.fromAlgebraic(positions[1]);

            // Create Move Obj
            Move move = new Move(initialPosition, finalPosition, 'q');

            // Check Move Legality
            if (!game.isMoveLegal(move)) {
                System.out.println("Illegal Move :(");
                System.out.println("\n");
                continue;
            }

            // Make Move
            game.move(move);
            System.out.println("FEN: " + game.toFEN());
            System.out.println("\n");
        }
    }
}
