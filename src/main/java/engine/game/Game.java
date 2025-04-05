package engine.game;

import engine.exceptions.IllegalMoveException;
import engine.types.Move;
import engine.utils.*;
import engine.types.Position;
import engine.pieces.*;
import utils.Color;
import utils.GameResult;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Represents a chess game, encapsulating the game logic, rules, player turns,
 * and timer management.
 *
 * <p>Key Responsibilities:</p>
 * <ul>
 *   <li>Manages the board state, player turns, and move histories.</li>
 *   <li>Tracks game progression, including half-move and full-move counters.</li>
 *   <li>Handles win conditions, including checks for draws, checkmates, and the fifty-move rule.</li>
 *   <li>Supports optional timer integration for timed games.</li>
 *   <li>Provides utility methods for generating legal moves, validating moves, and determining game state.</li>
 * </ul>
 *
 * <p>This class facilitates core gameplay mechanics while maintaining flexibility
 * for custom game states, such as initializing with specific board setups or timer configurations.</p>
 */
public class Game {
    public Board board = new Board();
    private Color turn = Color.WHITE;
    private int halfMoveClock = 0;
    private int fullMoveNumber = 1;
    private Timer timer;
    private GameResult result = GameResult.ON_GOING;
    private final HashMap<String, Integer> boardHistory = new HashMap<>();
    private final MoveHistory moveHistory = new MoveHistory();

    /**
     * Constructs a new Game instance, optionally with an initialized Timer.
     * If a Timer is provided, it is validated and started if not already running.
     * If no Timer is provided, the game is played without time tracking.
     *
     * @param timer The Timer object for managing player time or null to disable timers.
     */
    public Game(Timer timer) {
        // Disabled Timer
        if (timer == null) {
            this.timer = null;
            return;
        }

        // Set and Start Timer
        this.timer = timer;
        this.timer.start();
    }

    /**
     * Constructs a Game instance with a custom board, turn, move counters, and timer.
     * This allows initializing games in custom or intermediate states.
     *
     * <p>If a Timer is provided, its setup logic is validated similarly
     * to the single-argument constructor, ensuring it is usable and synchronized.</p>
     *
     * @param board           The initial game board state.
     * @param turn            The current player's turn (Color.WHITE or Color.BLACK).
     * @param halfMoveClock   The half-move counter for the fifty-move rule.
     * @param fullMoveNumber  The turn count, incremented after each move by Black.
     * @param timer           The Timer object for time management (nullable).
     */
    public Game(Board board, Color turn, int halfMoveClock, int fullMoveNumber, Timer timer) {
        this(timer);
        this.board = board;
        this.turn = turn;
        this.halfMoveClock = halfMoveClock;
        this.fullMoveNumber = fullMoveNumber;

        // Update Timer.turn if needed
        if (timer != null && this.turn != this.timer.getTurn()) {
            this.timer.setTurn(this.turn);
        }
    }

    /**
     * Creates a deep copy of the current game state.
     * The method ensures all mutable fields, such as the board and timer, are deeply copied
     * to prevent unintended modifications to the original game state when the copy is modified.
     *
     * @return A new `Game` object with the same state as the original, but with independent copies of the board and timer.
     */
    public Game getDeepCopy() {
        return new Game(
                this.board.getDeepCopy(),
                this.turn, this.halfMoveClock,
                this.fullMoveNumber,
                (this.timer == null ? null : this.timer.getDeepCopy())
        );
    }

    // Getters
    public Color getTurn() { return turn; }
    public int getHalfMoveClock() { return halfMoveClock; }
    public int getFullMoveNumber() { return fullMoveNumber; }
    public GameResult getResult() { return result; }
    public boolean inPlay() { return result == GameResult.ON_GOING;}
    public Timer getTimer() { return timer; }
    public MoveHistory getMoveHistory() { return moveHistory; }

    /**
     * Removes the timer from the game.
     *
     * <p>This method sets the game's timer to `null`, effectively disabling
     * time tracking for the rest of the game. Use this when time management
     * is no longer required or to transition the game to a non-timed format.</p>
     */
    public void removeTimer() {
        this.timer = null;
    }

    /**
     * Switches the current player's turn in the game.
     * Updates the `turn` field to the opposite player and, if a timer is present, switches the timer's turn as well.
     * Ensures that the timer's turn remains consistent with the game's turn after the switch.
     *
     * @throws IllegalStateException If the game's turn and the timer's turn are inconsistent after the switch.
     */
    private void switchTurn() {
        // Switch Game Turn
        this.turn = this.turn.inverse();

        // Switch Timer Turn

        if (this.timer == null) { return; }
        this.timer.switchTurn();
        if (this.turn != timer.getTurn()) {
            throw new IllegalStateException("Timer.turn did not equal Game.turn");
        }
    }

    /**
     * Retrieves all legal moves for the piece located at the specified position.
     * Legal moves are calculated based on the current game state, including turn,
     * board state, and rules like check and checkmate.
     *
     * @param initialPosition The position of the piece to calculate legal moves for.
     * @return A list of positions the piece can legally move to.
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
        if (pieceToMove.getColor() != turn) {
            return positions;
        }

        // Loop Through Board
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Position finalPosition = new Position(file, rank);
                Move currentMove = new Move(initialPosition, finalPosition, '\0');
                if (MoveUtils.causesPromotion(currentMove, this)) {
                    currentMove = new Move(initialPosition, finalPosition, 'q');
                }
                if (isMoveLegal(currentMove)) {
                    positions.add(finalPosition);
                }
            }
        }
        return positions;
    }

    /**
     * Validates whether a given move is safe for the current player's king.
     * A move is considered safe if it does not leave the king in check after execution.
     *
     * @param move The move to evaluate.
     * @return `true` if the move is safe for the king; otherwise, `false`.
     */
    public boolean isMoveSafe(Move move) {
        try {
            // Create a copy of the board
            Board boardCopy = board.getDeepCopy();

            // Apply Move
            boardCopy.executeMove(move);

            // Check if the king is in check after the move
            King king = boardCopy.getKing(turn);
            Position kingPosition = boardCopy.getKingPosition(turn);
            return !king.isChecked(kingPosition, boardCopy);
        } catch (IllegalMoveException e) {
            return false;
        }
    }

    /**
     * Validates whether a given move is legal in the current game state.
     * A move is considered legal if it satisfies the following conditions:
     * <ul>
     *   <li>The game is ongoing.</li>
     *   <li>The move corresponds to a non-null piece at the starting position.</li>
     *   <li>The piece belongs to the current player.</li>
     *   <li>The move is valid according to the piece's movement rules and the game state.</li>
     *   <li>The move does not leave the king in check.</li>
     * </ul>
     *
     * @param move The move to validate.
     * @return `true` if the move is legal; otherwise, `false`.
     */
    public boolean isMoveLegal(Move move) {
        Piece pieceToMove = board.getPieceAt(move.initialPosition());

        // Cannot move after game
        if (getResult() != GameResult.ON_GOING) {
            return false;
        }

        // Cannot move null
        if (pieceToMove == null) {
            return false;
        }

        // Cannot move wrong color
        if (pieceToMove.getColor() != turn) {
            return false;
        }

        // Move must be valid
        if (!pieceToMove.isMoveValid(move, board)) {
            return false;
        }

        // Move must be safe
        return isMoveSafe(move);
    }

    /**
     * Determines whether the current player is in checkmate.
     * A player is in checkmate if:
     * <ul>
     *   <li>The player's king is in check.</li>
     *   <li>The player has no legal moves available.</li>
     * </ul>
     *
     * @return `true` if the current player is in checkmate; otherwise, `false`.
     */
    public boolean isCheckmate() {
        Position kingPosition = board.getKingPosition(turn);
        return isStalemate() && board.getKing(turn).isChecked(kingPosition, board);
    }

    /**
     * Determines whether the current player is in stalemate.
     * A player is in stalemate if:
     * <ul>
     *   <li>The player is not in check.</li>
     *   <li>The player has no legal moves available.</li>
     * </ul>
     *
     * @return `true` if the current player is in stalemate; otherwise, `false`.
     */
    public boolean isStalemate() {
        List<Position> pieces = board.getPiecePositionsByColor(turn);

        for (Position pos : pieces) {
            if (!getLegalMoves(pos).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Updates the game's board history by recording the current board state and turn
     * in Forsyth-Edwards Notation (FEN) format. This information is used to track
     * repetitions for enforcing the threefold repetition rule.
     */
    private void updateBoardHistory() {
        String fen = FEN.getFENBoardAndTurn(this);
        boardHistory.put(fen, boardHistory.getOrDefault(fen, 0) + 1);
    }

    /**
     * Records the specified move in the game's move history.
     *
     * @param move The move to add to the move history.
     */
    private void updateMoveHistory(Move move) {
        moveHistory.addMove(move);
    }

    /**
     * Evaluates and updates the game's result based on various win or draw conditions.
     * The conditions include:
     * <ul>
     *   <li>Checkmate: A player has been checkmated.</li>
     *   <li>Stalemate: The current player has no legal moves and is not in check.</li>
     *   <li>Fifty-Move Rule: Neither player has moved a pawn or captured a piece for 50 moves.</li>
     *   <li>Threefold Repetition: The board state has been repeated three or more times.</li>
     *   <li>Timeout: One of the players has run out of time (if a timer is being used).</li>
     * </ul>
     */
    private void checkWinConditions() {
        // Checkmated
        if (isCheckmate()) {
            result = (turn == Color.WHITE ? GameResult.BLACK_CHECKMATE : GameResult.WHITE_CHECKMATE);
            stopTimer();
            return;
        }

        // Stalemate
        if (isStalemate()) {
            result = GameResult.STALEMATE;
            stopTimer();
            return;
        }

        // 50 Move Rule
        if (this.halfMoveClock >= 100) {
            result = GameResult.FIFTY_MOVE_RULE;
            stopTimer();
            return;
        }

        // Threefold Repetition
        if (boardHistory.get(FEN.getFENBoardAndTurn(this)) >= 3) {
            result = GameResult.THREEFOLD_REPETITION;
            stopTimer();
            return;
        }

        // Timer
        if (this.timer != null){
            if (this.timer.isOutOfTime(Color.WHITE)) {
                result = GameResult.BLACK_WON_ON_TIME;
                stopTimer();
                return;
            }
            if (this.timer.isOutOfTime(Color.BLACK)) {
                result = GameResult.WHITE_WON_ON_TIME;
                stopTimer();
                return;
            }
        }
    }

    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    /**
     * Updates the game result to reflect a resignation by the specified player.
     *
     * <p>The method sets the game result based on the color of the player
     * who resigns. If the White player resigns, the result is set to
     * `RESIGN_WHITE`, indicating that Black wins. Similarly, if the
     * Black player resigns, the result is set to `RESIGN_BLACK`, indicating that
     * White wins.</p>
     *
     * @param color The color of the player resigning (Color.WHITE or Color.BLACK).
     */
    public void resign(Color color) {
        this.result = color == Color.WHITE ? GameResult.RESIGN_WHITE : GameResult.RESIGN_BLACK;
        stopTimer();
    }

    /**
     * Updates the game result to indicate a draw by mutual agreement.
     *
     * <p>This method sets the game result to `DRAW_AGREEMENT`, signifying that
     * both players have agreed to end the game in a draw. This is one of the
     * standard methods to conclude a chess game without a decisive winner.</p>
     */
    public void drawAgreement() {
        this.result = GameResult.DRAW_AGREEMENT;
        stopTimer();
    }

    /**
     * Executes a move, updates the game state, and switches turns.
     * This method handles all necessary updates related to the move, including:
     * <ul>
     *   <li>Validating the legality of the move, throwing exceptions for invalid moves.</li>
     *   <li>Updating the full-move counter if Black moves.</li>
     *   <li>Resetting the half-move clock if a pawn moves or a capture occurs.</li>
     *   <li>Making the move on the board and updating the history.</li>
     *   <li>Checking for win conditions after the move.</li>
     * </ul>
     *
     * @param move The move to execute.
     * @throws IllegalMoveException If the move is illegal.
     */
    public void move(Move move) {
        // Convert Stuff
        Position initialPosition = move.initialPosition();
        Piece pieceToMove = board.getPieceAt(initialPosition);

        // If Game is Over
        if (getResult() != GameResult.ON_GOING) {
            throw new IllegalMoveException("Illegal Move: Cannot make move after game is finished.");
        }

        // Make sure there is a pieceToMove
        if (pieceToMove == null) {
            throw new IllegalMoveException("Illegal Move: No piece at the initial position to move.");
        }

        // Wrong Color
        if (pieceToMove.getColor() != turn) {
            throw new IllegalMoveException("Illegal Move: '" + pieceToMove + "' cannot move this turn.");
        }

        // Move must be valid
        if (!pieceToMove.isMoveValid(move, board)) {
            throw new IllegalMoveException("Illegal Move: " + move);
        }

        // Is Move Safe?
        if (!isMoveSafe(move)) {
            throw new IllegalMoveException("Illegal Move: " + move + " leaves king in check.");
        }

        // Update Full
        if (this.turn == Color.BLACK) {
            this.fullMoveNumber++;
        }

        // Update Half
        if ((pieceToMove instanceof Pawn) || (MoveUtils.isCapture(move, this))) {
            this.halfMoveClock = 0;
        } else {
            this.halfMoveClock++;
        }

        // Make Move on Board
        board.executeMove(move);

        // Switch Players
        this.switchTurn();

        //  Update History
        this.updateBoardHistory();
        this.updateMoveHistory(move);

        // Check for Win Condition
        this.checkWinConditions();
    }

    /**
     * Returns a string representation of the game's current state, including:
     * <ul>
     *   <li>The board as a string.</li>
     *   <li>The en passant position (if applicable).</li>
     *   <li>The current game result (winning, draw, etc.).</li>
     *   <li>The current player's turn.</li>
     *   <li>Formatted remaining time for each player (if a timer is used).</li>
     * </ul>
     *
     * @return A string representation of the game's current state.
     */
    @Override
    public String toString() {
        return board.toString() +
                "\nEn Passant: " + board.getEnPassantPosition() +
                "\nGame Result: " + getResult() +
                "\nCurrent Turn: " + getTurn() +
                (this.timer == null ?
                        "" : "\nWhite Time: " + timer.getFormatedTimeLeft(Color.WHITE) + "\nBlack Time: " + timer.getFormatedTimeLeft(Color.BLACK));
    }
}
