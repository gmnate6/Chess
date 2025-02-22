package engine;

import engine.utils.*;

import engine.board.Board;
import engine.board.Position;
import engine.pieces.*;

import utils.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Represents a chess game: handles game logic, win conditions, moves, player turns, and timers.
 */
public class Game {
    private Board board = new Board();
    private Color currentPlayer = Color.WHITE;
    private int halfMoveClock = 0;
    private int fullMoveNumber = 1;
    private GameResult gameResult = GameResult.ON_GOING;
    private final HashMap<String, Integer> boardHistory = new HashMap<>();
    public Timer timer;

    /**
     * Constructs a new Game instance with an already initialized Timer object.
     * This constructor is different from other constructors as it accepts an
     * external `Timer` instance and starts the game by triggering the Timer.
     *
     * @param timer The Timer object used to manage time for both players.
     *              This includes settings like initial time and increment per move.
     */
    public Game(Timer timer) {
        if (timer == null) {
            throw new NullPointerException("Timer must not be null.");
        }

        // Set Timer
        this.timer = timer;
        if (!this.timer.isStarted()) {
            this.timer.start();
        }
    }

    /**
     * Creates a Game object from a FEN string.
     *
     * @param fen The FEN string describing the game state.
     * @return A Game instance reflecting the given FEN state.
     * @throws IllegalArgumentException If the FEN string is invalid.
     */
    public static Game fromFEN(String fen, Timer timer) {
        Game game = new Game(timer);
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

    /**
     * Converts the current game state into a FEN string.
     *
     * @return A FEN representation of the game's current state.
     */
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
    public Timer getTimer() { return timer; }

    // Setter
    private void setCurrentPlayer(Color currentPlayer) { this.currentPlayer = currentPlayer;}
    private void setHalfMoveClock(int halfMoveClock) { this.halfMoveClock = halfMoveClock; }
    private void setFullMoveNumber(int fullMoveNumber) { this.fullMoveNumber = fullMoveNumber; }

    /**
     * Gets legal moves for a piece at a given position.
     *
     * @param initialPosition The position of the piece to move.
     * @return A list of legal positions the piece can move to.
     */
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
                if (isMoveLegal(currentMove)) {
                    positions.add(finalPosition);
                }
            }
        }
        return positions;
    }

    /**
     * Checks if a move is safe for the current player's king.
     *
     * @param move The move to check.
     * @return `true` if the move is safe; otherwise, `false`.
     */
    public boolean isMoveSafe(Move move) {
        // Create a copy of the board
        Board boardCopy = board.getDeepCopy();

        // Apply Move
        boardCopy.move(move);

        // Check if the king is in check after the move
        King king = boardCopy.getKing(currentPlayer);
        Position kingPosition = board.getKingPosition(currentPlayer);
        return !king.isChecked(kingPosition, boardCopy);
    }

    /**
     * Checks if a move is legal for the current game state.
     *
     * @param move The move to validate.
     * @return `true` if the move is legal according to chess rules; otherwise, `false`.
     */
    public boolean isMoveLegal(Move move) {
        Piece pieceToMove = board.getPieceAt(move.getInitialPosition());

        // Cannot move after game
        if (getGameResult() != GameResult.ON_GOING) {
            return false;
        }

        // Cannot move null
        if (pieceToMove == null) {
            return false;
        }

        // Cannot move wrong color
        if (pieceToMove.getColor() != currentPlayer) {
            return false;
        }

        // Move must be valid
        if (!pieceToMove.isMoveValid(move, board)) {
            return false;
        }

        // Move must be safe
        if (!isMoveSafe(move)) {
            return false;
        }

        // Move must be legal
        return true;
    }

    /**
     * Checks if the current player is in checkmate.
     *
     * @return `true` if the player is in checkmate; otherwise, `false`.
     */
    public boolean isCheckmate() {
        Position kingPosition = board.getKingPosition(currentPlayer);
        return isStalemate() && board.getKing(currentPlayer).isChecked(kingPosition, board);
    }

    /**
     * Checks if the current player is in stalemate (no legal moves).
     *
     * @return `true` if the player is in stalemate; otherwise, `false`.
     */
    public boolean isStalemate() {
        List<Position> pieces = board.getPiecePositionsByColor(currentPlayer);

        for (Position pos : pieces) {
            if (!getLegalMoves(pos).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Updates the game's board history for tracking repetitions.
     */
    public void updateBoardHistory() {
        String fen = this.toFEN(); // Convert board to FEN string
        boardHistory.put(fen, boardHistory.getOrDefault(fen, 0) + 1);
    }

    /**
     * Checks and updates the game's result based on win conditions.
     */
    public void checkWinConditions() {
        // Checkmated
        if (isCheckmate()) {
            gameResult = (currentPlayer == Color.WHITE ? GameResult.BLACK_CHECKMATE : GameResult.WHITE_CHECKMATE);
            return;
        }

        // Stalemate
        if (isStalemate()) {
            gameResult = GameResult.STALEMATE;
            return;
        }

        // 50 Move Rule
        if (this.halfMoveClock >= 100) {
            gameResult = GameResult.FIFTY_MOVE_RULE;
        }

        // Threefold Repetition
        if (boardHistory.get(this.toFEN()) >= 3) {
            gameResult = GameResult.THREEFOLD_REPETITION;
        }

        // Timer
        if (this.timer.isOutOfTime(Color.WHITE)) {
            gameResult = GameResult.TIME_WHITE_LOSS;
        }
        if (this.timer.isOutOfTime(Color.BLACK)) {
            gameResult = GameResult.TIME_BLACK_LOSS;
        }
    }

    /**
     * Executes a move, updates the board and game state, and switches turns.
     *
     * @param move The move to execute.
     */
    public void move(Move move) {
        // Convert Stuff
        Position initialPosition = move.getInitialPosition();
        Position finalPosition = move.getFinalPosition();
        Piece pieceToMove = board.getPieceAt(initialPosition);

        // If Game is Over
        if (getGameResult() != GameResult.ON_GOING) {
            throw new RuntimeException("Cannot make move after game is finished.");
        }

        // Make sure there is a pieceToMove
        if (pieceToMove == null) {
            throw new IllegalStateException("No piece at the initial position to move.");
        }

        // Wrong Color
        if (pieceToMove.getColor() != currentPlayer) {
            throw new IllegalArgumentException("'" + currentPlayer + "' Player cannot move this piece: '" + pieceToMove + "'.");
        }

        // Move must be valid
        if (!pieceToMove.isMoveValid(move, board)) {
            throw new IllegalArgumentException("Illegal move for '" + pieceToMove + "': " + move);
        }

        // Is Move Safe?
        if (!isMoveSafe(move)) {
            throw new IllegalArgumentException("Move puts king in check: " + move);
        }

        // Make Move on Board
        board.move(move);

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

        // Switch Players
        this.setCurrentPlayer(this.getCurrentPlayer() == Color.WHITE ? Color.BLACK : Color.WHITE);
        this.timer.switchTurn();
        if (this.currentPlayer != timer.getCurrentTurn()) {
            throw new IllegalStateException("Timer.turn did not equal Game.currentPlayer");
        }

        //  Update Board History
        this.updateBoardHistory();

        // Check for Win Condition
        this.checkWinConditions();
    }

    /**
     * Returns a string representation of the game's current state.
     */
    @Override
    public String toString() {
        return board.toString() +
                "\nEn Passant: " + board.getEnPassantPosition() +
                "\nGame Result: " + getGameResult() +
                "\nCurrent Player: " + getCurrentPlayer() +
                "\nWhite Time: " + timer.getFormatedTimeLeft(Color.WHITE) +
                "\nBlack Time: " + timer.getFormatedTimeLeft(Color.BLACK);
    }
}