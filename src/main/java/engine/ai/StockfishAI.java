package engine.ai;

import engine.game.Game;
import engine.types.Move;
import engine.utils.FEN;
import engine.utils.MoveUtils;

import java.io.*;

public class StockfishAI implements ChessAI{
    private static String getStockfishPath() {
        // Set path based on the user's operating system
        String os = System.getProperty("os.name").toLowerCase();
        String stockfishPath = "stockfish/";

        if (os.contains("win")) {
            stockfishPath += "windows/stockfish.exe";
        } else if (os.contains("nix") || os.contains("nux")) {
            stockfishPath += "linux/stockfish";
        } else if (os.contains("mac")) {
            stockfishPath += "mac/stockfish";
        }
        return stockfishPath;
    }

    private static String parseBestMove(String stockfishOutput) {
        // Split by spaces
        String[] parts = stockfishOutput.split(" ");
        if (parts.length >= 2 && parts[0].equals("bestmove")) {
            // Return the move after "bestmove"
            return parts[1];
        }
        // Return null if no valid move is found
        return null;
    }

    public static boolean doesStockfishExist() {
        String stockfishPath = getStockfishPath();
        File stockfishFile = new File(stockfishPath);
        return stockfishFile.exists();
    }

    public Move getMove(Game game) {
        String fen = FEN.getFEN(game);
        String stringMove;
        String stockfishPath = getStockfishPath();

        // Stockfish is missing
        if (!doesStockfishExist()) {
            System.out.println("Stockfish Missing. Did Random Move.");
            RandomAI randomAI = new RandomAI();
            return randomAI.getMove(game);
        }

        // Get Stock Fish Move
        try {
            Process stockfish = new ProcessBuilder(stockfishPath).start();
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
                    return MoveUtils.fromLongAlgebraic(stringMove, game);
                }
            }

            // Close process
            writer.write("quit\n");
            writer.flush();
            stockfish.destroy();
        } catch (Exception e) {
            System.out.println("Stockfish Failed.");
        }

        // Stockfish failed
        System.out.println("Did random move.");
        RandomAI randomAI = new RandomAI();
        return randomAI.getMove(game);
    }

    public String toString() {
        return "StockfishAI";
    }
}
