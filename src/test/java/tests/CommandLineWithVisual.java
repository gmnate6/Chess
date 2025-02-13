package tests;

import engine.Game;
import engine.board.Position;

import frontend.gui.ChessGUI;

import javax.swing.*;
import java.util.List;
import java.util.Scanner;

public class CommandLineWithVisual {
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

        // Create Board GUI
        ChessGUI gui = new ChessGUI();

        // Main Loop
        while (game.isGameInPlay()) {
            // Show Board
            gui.boardPanel.loadFromBoard(game.getBoard());

            // Print Game
            System.out.print(game.getCurrentPlayer().toString() + "'s Move: ");
            input = scanner.nextLine();

            // Clear GUI Square Overlays
            SwingUtilities.invokeLater(() -> {
                gui.boardPanel.clearPieceOverlays();
            });

            // Break
            if (input.isEmpty()) { break; }

            // Split
            String[] move = input.split(" ");

            // Get Moves
            if (move.length == 1) {
                // Sout
                Position piecePos = Position.fromAlgebraic(move[0]);
                List<Position> positions = game.getLegalMoves(piecePos);
                System.out.println(positions);

                // Update Board
                gui.boardPanel.getSquareButton(piecePos.file(), piecePos.rank()).setActive(true);
                for (Position pos : positions) {
                    SwingUtilities.invokeLater(() -> {
                        gui.boardPanel.getSquareButton(pos.file(), pos.rank()).setHint(true);
                    });
                }
                continue;
            }

            // Make Move
            game.move(move[0], move[1], 'q');
            System.out.println("\n");
        }
        gui.dispose();
    }
}
