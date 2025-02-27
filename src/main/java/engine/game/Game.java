package engine.game;

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
 * Represents a chess game: handles game logic, win conditions, moves, player turns, and timers.
 */
public class Game {
    public Board board = new Board();
    private Color turn = Color.WHITE;
    private int halfMoveClock = 0;
    private int fullMoveNumber = 1;
    public Timer timer;
    private GameResult result = GameResult.ON_GOING;
    private final HashMap<String, Integer> boardHistory = new HashMap<>();
    private final MoveHistory moveHistory = new MoveHistory();

    /**
     * Constructs a new Game instance, optionally with an already initialized Timer object.
     * If a Timer is provided and enabled, it ensures the Timer is valid (non-null) and starts it
     * if it is not already running. If the Timer is disabled (or null), the game operates without
     * time tracking.
     *
     * @param timer The Timer object used to manage time for both players, or null if timers are disabled.
     */
    public Game(Timer timer) {
        // Disabled Timer
        if (timer == null) {
            timer = new Timer();
        }

        // Set Timer
        this.timer = timer;
        if (!this.timer.isStarted()) {
            this.timer.start();
        }
    }

    /**
     * Creates a Game with a custom board state, move counters, and a Timer.
     * Reuses the Timer setup logic from the single-argument constructor.
     *
     * @param board           The game board state.
     * @param turn            The current player's turn.
     * @param halfMoveClock   Half-move counter for the fifty-move rule.
     * @param fullMoveNumber  Full move number starting from 1.
     * @param timer           The Timer object to manage turns (must not be null).
     */
    public Game(Board board, Color turn, int halfMoveClock, int fullMoveNumber, Timer timer) {
        this(timer);
        this.board = board;
        this.turn = turn;
        this.halfMoveClock = halfMoveClock;
        this.fullMoveNumber = fullMoveNumber;

        // Update Timer.turn if needed
        if (this.turn != timer.getTurn()) {
            timer.setTurn(this.turn);
        }
    }

    // Deep Copy
    public Game getDeepCopy() {
        return new Game(this.board.getDeepCopy(), this.turn, this.halfMoveClock, this.fullMoveNumber, this.timer.getDeepCopy());
    }

    // Getters
    public Color getTurn() { return turn; }
    public int getHalfMoveClock() { return halfMoveClock; }
    public int getFullMoveNumber() { return fullMoveNumber; }
    public GameResult getResult() { return result; }
    public boolean inPlay() { return result == GameResult.ON_GOING;}
    public Timer getTimer() { return timer; }
    public MoveHistory getMoveHistory() { return moveHistory; }

    // Setter
    public void setTurn(Color turn) { this.turn = turn;}

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
        if (pieceToMove.getColor() != turn) {
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
        boardCopy.executeMove(move);

        // Check if the king is in check after the move
        King king = boardCopy.getKing(turn);
        Position kingPosition = boardCopy.getKingPosition(turn);
        return !king.isChecked(kingPosition, boardCopy);
    }

    /**
     * Checks if a move is legal for the current game state.
     *
     * @param move The move to validate.
     * @return `true` if the move is legal according to chess rules; otherwise, `false`.
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
     * Checks if the current player is in checkmate.
     *
     * @return `true` if the player is in checkmate; otherwise, `false`.
     */
    public boolean isCheckmate() {
        Position kingPosition = board.getKingPosition(turn);
        return isStalemate() && board.getKing(turn).isChecked(kingPosition, board);
    }

    /**
     * Checks if the current player is in stalemate (no legal moves).
     *
     * @return `true` if the player is in stalemate; otherwise, `false`.
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
     * Updates the game's board history for tracking repetitions.
     */
    public void updateBoardHistory() {
        String fen = FEN.getFENBoardAndTurn(this);
        boardHistory.put(fen, boardHistory.getOrDefault(fen, 0) + 1);
    }

    /**
     * Updates the game's move history.
     */
    public void updateMoveHistory(Move move) {
        moveHistory.addMove(move);
    }

    /**
     * Checks and updates the game's result based on win conditions.
     */
    public void checkWinConditions() {
        // Checkmated
        if (isCheckmate()) {
            result = (turn == Color.WHITE ? GameResult.BLACK_CHECKMATE : GameResult.WHITE_CHECKMATE);
            return;
        }

        // Stalemate
        if (isStalemate()) {
            result = GameResult.STALEMATE;
            return;
        }

        // 50 Move Rule
        if (this.halfMoveClock >= 100) {
            result = GameResult.FIFTY_MOVE_RULE;
            return;
        }

        // Threefold Repetition
        if (boardHistory.get(FEN.getFENBoardAndTurn(this)) >= 3) {
            result = GameResult.THREEFOLD_REPETITION;
            return;
        }

        // Timer
        if (!timer.isDisabled()){
            if (this.timer.isOutOfTime(Color.WHITE)) {
                result = GameResult.TIME_WHITE_LOSS;
            }
            if (this.timer.isOutOfTime(Color.BLACK)) {
                result = GameResult.TIME_BLACK_LOSS;
            }
        }
    }

    /**
     * Executes a move, updates the board and game state, and switches turns.
     *
     * @param move The move to execute.
     */
    public void move(Move move) {
        // Convert Stuff
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        Piece pieceToMove = board.getPieceAt(initialPosition);

        // If Game is Over
        if (getResult() != GameResult.ON_GOING) {
            throw new RuntimeException("Cannot make move after game is finished.");
        }

        // Make sure there is a pieceToMove
        if (pieceToMove == null) {
            throw new IllegalStateException("No piece at the initial position to move.");
        }

        // Wrong Color
        if (pieceToMove.getColor() != turn) {
            throw new IllegalArgumentException("'" + turn + "' Player cannot move this piece: '" + pieceToMove + "'.");
        }

        // Move must be valid
        if (!pieceToMove.isMoveValid(move, board)) {
            throw new IllegalArgumentException("Illegal move for '" + pieceToMove + "': " + move);
        }

        // Is Move Safe?
        if (!isMoveSafe(move)) {
            throw new IllegalArgumentException("Move puts king in check: " + move);
        }

        // Update Full
        if (this.turn == Color.BLACK) {
            this.fullMoveNumber++;
        }

        // Update Half
        if ((pieceToMove instanceof Pawn) || (this.board.getPieceAt(finalPosition) != null)) {
            this.halfMoveClock = 0;
        } else {
            this.halfMoveClock++;
        }

        // Make Move on Board
        board.executeMove(move);

        // Switch Players
        this.setTurn(this.getTurn().inverse());
        this.timer.switchTurn();
        if (this.turn != timer.getTurn()) {
            throw new IllegalStateException("Timer.turn did not equal Game.turn");
        }

        //  Update History
        this.updateBoardHistory();
        this.updateMoveHistory(move);

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
                "\nGame Result: " + getResult() +
                "\nCurrent Turn: " + getTurn() +
                "\nWhite Time: " + timer.getFormatedTimeLeft(Color.WHITE) +
                "\nBlack Time: " + timer.getFormatedTimeLeft(Color.BLACK);
    }
}