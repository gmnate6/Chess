package engine.utils;

import engine.game.Game;
import engine.types.Move;
import engine.types.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;

public class AI {
    public static Move randomMove(Game game) {
        // Random instance
        Random random = new Random();

        // Get Initial Positions for Current Player
        List<Position> initialPositionList = game.getBoard().getPiecePositionsByColor(game.getCurrentPlayer());

        while (!initialPositionList.isEmpty()) {
            // Select Random Initial Position
            int randomInitialIndex = random.nextInt(initialPositionList.size());
            Position initialPosition = initialPositionList.get(randomInitialIndex);

            // Get Legal Moves for Initial Position
            List<Position> finalPositionList = game.getLegalMoves(initialPosition);

            // If there are legal moves, pick one and return
            if (!finalPositionList.isEmpty()) {
                Position finalPosition = finalPositionList.get(random.nextInt(finalPositionList.size()));
                return new Move(initialPosition, finalPosition, 'q');
            }

            // Remove position from list if no legal moves are found
            initialPositionList.remove(randomInitialIndex);
        }

        // No moves available
        throw new RuntimeException("Error: No Moves Available");

    }

    private static String parseBestMove(String stockfishOutput) {
        String[] parts = stockfishOutput.split(" "); // Split by spaces

        if (parts.length >= 2 && parts[0].equals("bestmove")) {
            return parts[1]; // Return the move after "bestmove"
        }

        return null; // Return null if no valid move is found
    }

    public static Move stockFishMove(Game game) {
        String fen = game.toFEN();
        String stringMove;

        // Get Stock Fish Move
        try {
            Process stockfish = new ProcessBuilder("src/main/stockfish/stockfish-windows-x86-64-sse41-popcnt.exe").start();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stockfish.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(stockfish.getInputStream()));

            // Send UCI commands
            writer.write("ucinewgame\n"); // Reset Stockfish's internal state
            writer.flush();
            Thread.sleep(500);  // Wait for Stockfish to respond

            // Set position and ask for best move
            writer.write("position fen " + fen + "\n");
            writer.write("go depth 10\n");
            writer.flush();

            // Read Stockfish output
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("bestmove")) {
                    stringMove = parseBestMove(line);
                    assert stringMove != null;
                    return Move.fromLongAlgebraic(stringMove);
                }
            }

            // Close process
            writer.write("quit\n");
            writer.flush();
            stockfish.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // In Case
        System.out.println("StockFish Failed. Did Random Move.");
        return randomMove(game);
    }
}
