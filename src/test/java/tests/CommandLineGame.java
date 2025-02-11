package tests;

import engine.Game;
import engine.board.Position;

import java.util.Scanner;

public class CommandLineGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;

        // Create Game
        Game game;
        System.out.print("Import FEN? (y, n): ");
        input = scanner.nextLine();
        if (!input.equalsIgnoreCase("y")) {
            game = new Game(600_000, 0); // 10 minute game
        } else {
            // Load from FEN
            System.out.print("Enter FEN: ");
            input = scanner.nextLine();
            game = Game.fromFEN(input);
        }
        System.out.println("\n");

        // Main Loop
        while (game.isGameInPlay()) {
            // Print Game
            System.out.println(game);
            System.out.print(game.getCurrentPlayer().toString() + "'s Move: ");
            input = scanner.nextLine();

            // Break
            if (input.isEmpty()) { break; }

            // Split
            String[] move = input.split(" ");

            // SOUT Moves
            if (move.length == 1) {
                Position piecePos = Position.fromAlgebraic(move[0]);
                System.out.println(game.getLegalMoves(piecePos));
                continue;
            }

            // Make Move
            game.move(move[0], move[1], 'q');
            System.out.println(game.toFEN());
            System.out.println("\n");
        }
    }
}
