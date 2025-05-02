package com.nathanholmberg.chess.client.controller.game.managers;

import com.nathanholmberg.chess.client.view.game.BoardPanel;
import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.ChessGame;
import com.nathanholmberg.chess.engine.pieces.Piece;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.types.Position;

import java.util.List;

public class SelectionManager {
    private final BoardPanel boardPanel;
    private final ChessGame chessGame;
    private Color color;

    private Position selectedPosition = null;
    private Position markedPosition = null;
    private boolean pieceSelected = false;

    public SelectionManager(BoardPanel boardPanel, ChessGame chessGame, Color color) {
        this.boardPanel = boardPanel;
        this.chessGame = chessGame;
        this.color = color;
    }

    public boolean canSelectPosition(Position position) {
        if (position == null) return false;
        Piece piece = chessGame.board.getPieceAt(position);
        if (piece == null)
            return false;
        if (piece.getColor() == color)
            return true;
        if (selectedPosition == null)
            return true;

        // If an opponent’s piece is clicked while a piece is already selected,
        // allow selection only if the move between them is illegal.
        Move move = new Move(selectedPosition, position, 'Q');
        return !chessGame.isMoveLegal(move);
    }

    public void select(Position position) {
        if (position == null)
            throw new IllegalArgumentException("Error: Selected Position is null.");

        boardPanel.clearMarkedRed();
        selectedPosition = position;
        boardPanel.setHighlight(position, true);

        if (chessGame.getTurn() == color) {
            List<Position> legalMoves = chessGame.getLegalMoves(position);
            for (Position pos : legalMoves) {
                boardPanel.setHint(pos, true);
            }
        }

        boardPanel.grabPiece(position);
    }

    public void deselect() {
        if (selectedPosition != null) {
            boardPanel.setHighlight(selectedPosition, false);
        }
        boardPanel.clearHints();
        boardPanel.dropPiece();
        selectedPosition = null;
        pieceSelected = false;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Position getSelectedPosition() {
        return selectedPosition;
    }

    public void setPieceSelected(boolean selected) {
        this.pieceSelected = selected;
    }

    public boolean isPieceSelected() {
        return pieceSelected;
    }

    public void setMarkedPosition(Position position) {
        markedPosition = position;
    }

    public Position getMarkedPosition() {
        return markedPosition;
    }

    public void clearMarkedPosition() {
        markedPosition = null;
    }
}
