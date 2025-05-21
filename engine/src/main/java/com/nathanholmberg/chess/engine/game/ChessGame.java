package com.nathanholmberg.chess.engine.game;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.enums.GameResult;
import com.nathanholmberg.chess.engine.exceptions.IllegalMoveException;
import com.nathanholmberg.chess.engine.pieces.King;
import com.nathanholmberg.chess.engine.pieces.Pawn;
import com.nathanholmberg.chess.engine.pieces.Piece;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.types.Position;
import com.nathanholmberg.chess.engine.utils.FEN;
import com.nathanholmberg.chess.engine.utils.MoveUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class ChessGame {
    public Board board;
    private Color turn;
    private int halfMoveClock;
    private int fullMoveNumber;
    private GameResult result;
    private final HashMap<String, Integer> boardHistory;
    private final MoveHistory moveHistory;

    public ChessGame() {
        board = new Board();
        turn = Color.WHITE;
        halfMoveClock = 0;
        fullMoveNumber = 1;
        result = GameResult.ON_GOING;
        boardHistory = new HashMap<>();
        moveHistory = new MoveHistory();
    }

    public ChessGame(Board board, Color turn, int halfMoveClock, int fullMoveNumber) {
        this();
        this.board = board;
        this.turn = turn;
        this.halfMoveClock = halfMoveClock;
        this.fullMoveNumber = fullMoveNumber;
    }

    public void loadGameStateAt(int moveIndex) {
        if (moveIndex < -1 || moveIndex >= moveHistory.getSize()) {
            throw new IllegalArgumentException("Invalid move index: " + moveIndex);
        }

        // Fresh Game
        ChessGame newChessGame = new ChessGame();
        Board newBoard = newChessGame.board;

        // Replay Moves
        for (int i = 0; i <= moveIndex; i++) {
            Move move = moveHistory.getMoves().get(i);
            Piece piece = newBoard.getPieceAt(move.initialPosition());

            if (piece == null || !piece.isMoveValid(move, newBoard)) {
                throw new IllegalStateException("Invalid move in move history at index " + i + ": " + move);
            }

            newChessGame.move(move);
        }

        // Apply new game
        board = newChessGame.board;
        turn = newChessGame.turn;
        halfMoveClock = newChessGame.halfMoveClock;
        fullMoveNumber = newChessGame.fullMoveNumber;
        moveHistory.setCurrentMoveIndex(moveIndex);
    }

    public void stepFullBack() {
        loadGameStateAt(-1);
    }

    public void stepBack() {
        loadGameStateAt(moveHistory.getCurrentMoveIndex() - 1);
    }

    public void stepForward() {
        loadGameStateAt(moveHistory.getCurrentMoveIndex() + 1);
    }

    public void stepFullForward() {
        loadGameStateAt(moveHistory.getSize()-1);
    }

    public ChessGame getDeepCopy() {
        return new ChessGame(
                this.board.getDeepCopy(),
                this.turn,
                this.halfMoveClock,
                this.fullMoveNumber
        );
    }

    // Getters
    public Color getTurn() { return turn; }
    public int getHalfMoveClock() { return halfMoveClock; }
    public int getFullMoveNumber() { return fullMoveNumber; }
    public boolean inPlay() { return result == GameResult.ON_GOING;}
    public MoveHistory getMoveHistory() { return moveHistory; }
    public GameResult getResult() { return result; }

    public void setResult(GameResult result) { this.result = result; }

    private void switchTurn() {
        this.turn = this.turn.inverse();
    }

    public List<Position> getLegalMoves(Position initialPosition) {
        List<Position> positions = new ArrayList<>();

        // Get the piece to move
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
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates whether a given move is legal in the current game state.
     * A move is considered legal if it satisfies the following conditions:
     * - The game is ongoing.
     * - The move corresponds to a non-null piece at the starting position.
     * - The game is up to date.
     * - The piece belongs to the current player.
     * - The move is valid, according to the piece's movement rules and the game state.
     * - The move does not leave the king in check.
     *
     * @param move The move to validate.
     * @return `true` if the move is legal; otherwise, `false`.
     */
    public boolean isMoveLegal(Move move) {
        Piece pieceToMove = board.getPieceAt(move.initialPosition());

        // Cannot move after the game
        if (getResult() != GameResult.ON_GOING) {
            return false;
        }

        // Cannot move null
        if (pieceToMove == null) {
            return false;
        }

        // Game must be current
        if (!moveHistory.isAtLastMove()) {
            return false;
        }

        // Cannot move the wrong color piece
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

    public boolean isCheckmate() {
        Position kingPosition = board.getKingPosition(turn);
        return isStalemate() && board.getKing(turn).isChecked(kingPosition, board);
    }

    public boolean isStalemate() {
        List<Position> pieces = board.getPiecePositionsByColor(turn);

        for (Position pos : pieces) {
            if (!getLegalMoves(pos).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void updateBoardHistory() {
        String fen = FEN.getFENBoardAndTurn(this);
        boardHistory.put(fen, boardHistory.getOrDefault(fen, 0) + 1);
    }

    private void updateMoveHistory(Move move) {
        moveHistory.addMove(move);
    }

    private void checkWinConditions() {
        // Checkmated
        if (isCheckmate()) {
            result = (turn == Color.WHITE ? GameResult.BLACK_WON_BY_CHECKMATE : GameResult.WHITE_WON_BY_CHECKMATE);
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
        if (!boardHistory.isEmpty() && boardHistory.get(FEN.getFENBoardAndTurn(this)) >= 3) {
            result = GameResult.THREEFOLD_REPETITION;
        }
    }

    public void winByResign(Color color) {
        this.result = (color == Color.WHITE ? GameResult.WHITE_WON_BY_RESIGN : GameResult.BLACK_WON_BY_RESIGN);
    }

    public void drawAgreement() {
        this.result = GameResult.DRAW_AGREEMENT;
    }

    public void winOnTime(Color winner) {
        if (winner == Color.WHITE) {
            this.result = GameResult.WHITE_WON_ON_TIME;
        } else if (winner == Color.BLACK) {
            this.result = GameResult.BLACK_WON_ON_TIME;
        } else {
            throw new IllegalArgumentException("Invalid winner color: " + winner);
        }
    }

    public void move(Move move) {
        // If Game is Over
        this.checkWinConditions();
        if (getResult() != GameResult.ON_GOING) {
            throw new IllegalMoveException("Illegal Move: Cannot make move after game is finished.");
        }

        // Game must be current
        if (!moveHistory.isAtLastMove()) {
            throw new IllegalStateException("Cannot move while board is not up to date. Call Game.stepToLastMove() first");
        }

        // Convert Stuff
        Position initialPosition = move.initialPosition();
        Piece pieceToMove = board.getPieceAt(initialPosition);

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

    @Override
    public String toString() {
        return board.toString() +
                "\nEn Passant: " + board.getEnPassantPosition() +
                "\nGame Result: " + getResult() +
                "\nCurrent Turn: " + getTurn();
    }
}
