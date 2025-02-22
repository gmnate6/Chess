package engine;

import engine.utils.*;

import engine.board.Board;
import engine.board.Position;
import engine.pieces.*;

import utils.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Game {
    private Board board = new Board();
    private Color currentPlayer = Color.WHITE;
    private int halfMoveClock = 0;
    private int fullMoveNumber = 1;
    private GameResult gameResult = GameResult.ON_GOING;
    private final HashMap<String, Integer> boardHistory = new HashMap<>();
    public Timer timer;

    // Constructor
    public Game(long initialTime, long increment) {
        timer = new Timer(initialTime, increment);
    }
    public static Game fromFEN(String fen) {
        Game game = new Game(600_000, 0); // TODO: Fix this
        FEN fenObj;

        try {
            fenObj = FEN.fromFEN(fen);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid FEN format: " + fen + "\n", e);
        }

        // Update Game
        game.board = fenObj.getBoard();
        game.currentPlayer = fenObj.getCurrentPlayer();
        game.board.setCastlingRights(fenObj.getCastlingRights());
        game.board.setEnPassantPosition(fenObj.getEnPassantPosition());
        game.halfMoveClock = fenObj.getHalfMoveClock();
        game.fullMoveNumber = fenObj.getFullMoveNumber();

        // Return
        return game;
    }

    // To FEN
    public String toFEN() {
        FEN fen = new FEN(board, currentPlayer, halfMoveClock, fullMoveNumber);
        return fen.toFEN();
    }

    // Getters
    public Color getCurrentPlayer() { return currentPlayer; }
    public int getHalfMoveClock() { return halfMoveClock; }
    public int getFullMoveNumber() { return fullMoveNumber; }
    public Board getBoard() { return board; }
    public GameResult getGameResult() { return gameResult; }
    public boolean isGameInPlay() { return gameResult == GameResult.ON_GOING;}

    // Get Legal Moves
    public List<Position> getLegalMoves(Position initialPosition) {
        List<Position> positions = new ArrayList<>();

        // Get piece to move
        Piece pieceToMove = board.getPieceAt(initialPosition);

        // If null
        if (pieceToMove == null) {
            return positions;
        }

        // If Wrong Color
        if (pieceToMove.getColor() != currentPlayer) {
            return positions;
        }

        // Loop Through Board
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Position finalPosition = new Position(file, rank);
                Move currentMove = new Move(initialPosition, finalPosition, 'q');
                try {
                    if (currentMove.isMoveSafe(board, getCurrentPlayer())) {
                        positions.add(finalPosition);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return positions;
    }

    // True if checkmate
    public boolean isCheckmate() {
        Position kingPosition = board.getKingPosition(currentPlayer);
        return isStalemate() && board.getKingPiece(currentPlayer).isChecked(kingPosition, board);
    }

    // True if stalemate
    public boolean isStalemate() {
        List<Position> pieces = board.getPiecePositionsByColor(currentPlayer);

        for (Position pos : pieces) {
            if (!getLegalMoves(pos).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Setter
    public void setCurrentPlayer(Color currentPlayer) { this.currentPlayer = currentPlayer;}
    public void setHalfMoveClock(int halfMoveClock) { this.halfMoveClock = halfMoveClock; }
    public void setFullMoveNumber(int fullMoveNumber) { this.fullMoveNumber = fullMoveNumber; }

    // To String
    @Override
    public String toString() {
        return board.toString() +
                "\nEn Passant: " + board.getEnPassantPosition() +
                "\nGame Result: " + getGameResult() +
                "\nCurrent Player: " + getCurrentPlayer() +
                "\nWhite Time: " + this.timer.getFormatedTimeLeft(Color.WHITE) +
                "\nBlack Time: " + this.timer.getFormatedTimeLeft(Color.BLACK);
    }

    // Update Board History
    public void updateBoardHistory() {
        String fen = this.toFEN(); // Convert board to FEN string
        boardHistory.put(fen, boardHistory.getOrDefault(fen, 0) + 1);
    }

    // Check Win Conditions
    public void checkWinConditions() {
        /// Checkmated
        if (isCheckmate()) {
            gameResult = (currentPlayer == Color.WHITE ? GameResult.BLACK_CHECKMATE : GameResult.WHITE_CHECKMATE);
            return;
        }

        /// Stalemate
        if (isStalemate()) {
            gameResult = GameResult.STALEMATE;
            return;
        }

        /// 50 Move Rule
        if (this.halfMoveClock >= 100) {
            gameResult = GameResult.FIFTY_MOVE_RULE;
        }

        /// Threefold Repetition
        if (boardHistory.get(this.toFEN()) >= 3) {
            gameResult = GameResult.THREEFOLD_REPETITION;
        }

        /// Timer
        if (this.timer.isOutOfTime(Color.WHITE)) {
            gameResult = GameResult.TIME_WHITE_LOSS;
        }
        if (this.timer.isOutOfTime(Color.BLACK)) {
            gameResult = GameResult.TIME_BLACK_LOSS;
        }
    }

    /// Move
    public void move(String initialPositionAlgebraNotation, String finalPositionAlgebraNotation, char promotionPiece) {
        /// If Game is Over
        if (getGameResult() != GameResult.ON_GOING) {
            throw new RuntimeException("Cannot make move after game is finished.");
        }

        /// Convert Stuff
        Position initialPosition = Position.fromAlgebraic(initialPositionAlgebraNotation);
        Position finalPosition = Position.fromAlgebraic(finalPositionAlgebraNotation);
        Move move = new Move(initialPosition, finalPosition, promotionPiece);

        // Make sure there is a pieceToMove
        Piece pieceToMove = board.getPieceAt(initialPosition);
        if (pieceToMove == null) {
            throw new IllegalStateException("No piece at the initial position to move.");
        }

        /// Wrong Color
        if (pieceToMove.getColor() != currentPlayer) {
            throw new IllegalArgumentException("'" + currentPlayer + "' Player cannot move this piece: '" + pieceToMove + "'.");
        }

        /// Is Move Safe?
        if (!move.isMoveSafe(board, currentPlayer)) {
            throw new IllegalArgumentException("Move puts king in check: " + move);
        }

        /// Make Move on Board
        board.move(move);

        /// Update Game
        // Update Full
        if (this.currentPlayer == Color.BLACK) {
            this.fullMoveNumber++;
        }
        // Update Half
        if ((pieceToMove instanceof Pawn) || (this.board.getPieceAt(finalPosition) != null)) {
            this.halfMoveClock = 0;
        } else {
            this.halfMoveClock++;
        }
        this.setCurrentPlayer(this.getCurrentPlayer() == Color.WHITE ? Color.BLACK : Color.WHITE);

        ///  Update Board History
        this.updateBoardHistory();

        /// Update Timer
        this.timer.switchTurn();

        /// Check for Win Condition
        this.checkWinConditions();
    }
}
