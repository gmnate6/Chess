package com.nathanholmberg.chess.engine.utils;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.exceptions.IllegalNotationException;
import com.nathanholmberg.chess.engine.game.ChessGame;
import com.nathanholmberg.chess.engine.types.Move;

import java.util.List;

public class PGN {
    public static String getPGN(ChessGame chessGame) {
        StringBuilder pgn = new StringBuilder();

        // Dummy Game
        ChessGame dummyChessGame = new ChessGame();

        // Loop Through Moves History
        List<Move> moves = chessGame.getMoveHistory().getMoves();
        for (Move move : moves) {
            // Add Move Count
            if (dummyChessGame.getTurn() == Color.WHITE) {
                pgn.append(dummyChessGame.getFullMoveNumber()).append(". ");
            }

            // Move should be legal
            assert (dummyChessGame.isMoveLegal(move));

            // Add Algebraic Notation
            pgn.append(MoveUtils.toAlgebraic(move, dummyChessGame));
            pgn.append(" ");

            // Make Move
            dummyChessGame.move(move);
        }

        // Add Result
        if (!chessGame.getResult().isOnGoing()){
            pgn.append(chessGame.getResult().getScore());
        }

        // Return PGN
        return pgn.toString().trim();
    }

    public static ChessGame getGame(String pgn) {
        ChessGame chessGame = new ChessGame();

        // Early Throw
        if (pgn == null || pgn.isEmpty()) {
            throw new IllegalNotationException("Illegal PGN Notation: '" + pgn + "'");
        }

        // Split PGN into moves, excluding metadata or headers
        String gameResult = "";
        String[] movesArray = pgn.replaceAll("\\n", " ").split("\\s+");
        for (String moveNotation : movesArray) {
            if (moveNotation.isEmpty()) { continue; }
            // Skip turn numbers and results
            if (moveNotation.matches("^\\d.*")) {
                gameResult = moveNotation.trim();
                continue;
            }

            try {
                Move move = MoveUtils.fromAlgebraic(moveNotation, chessGame);
                chessGame.move(move);
            } catch (Exception e) {
                throw new IllegalNotationException(
                        "Illegal PGN Notation: '" + pgn + "'" +
                        "\nException occurred on move: " + chessGame.getFullMoveNumber() + ". " + moveNotation +
                        "\n" + e.getMessage());
            }
        }

        // Check Game Result
        boolean notationSaysGameOver = (
                gameResult.equals("1-0") ||
                gameResult.equals("0-1") ||
                gameResult.equals("1/2-1/2")
        );

        // If notation says it's over, it's over
        if (notationSaysGameOver && chessGame.inPlay()) {
            if (gameResult.equals("1-0")) {
                chessGame.winByResign(Color.WHITE);
            } else if (gameResult.equals("0-1")) {
                chessGame.winByResign(Color.BLACK);
            } else {
                chessGame.drawAgreement();
            }
        }

        // If game says it's over, but notation does not
        if (!notationSaysGameOver && !chessGame.inPlay()) {
            throw new IllegalNotationException("Illegal PGN Notation: '" + pgn + "'. Game result does not match notation.");
        }

        // Return
        return chessGame;
    }
}
