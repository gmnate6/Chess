package frontend.controller.game.managers;

import engine.game.Game;
import engine.pieces.Piece;
import engine.types.Move;
import engine.types.Position;
import frontend.view.game.BoardPanel;

import java.util.List;

import utils.Color;

public class SelectionManager {
    private final BoardPanel boardPanel;
    private final Game game;
    private Color color;

    private Position selectedPosition = null;
    private Position markedPosition = null;
    private boolean pieceSelected = false;

    public SelectionManager(BoardPanel boardPanel, Game game, Color color) {
        this.boardPanel = boardPanel;
        this.game = game;
        this.color = color;
    }

    public boolean canSelectPosition(Position position) {
        if (position == null) return false;
        Piece piece = game.board.getPieceAt(position);
        if (piece == null)
            return false;
        if (piece.getColor() == color)
            return true;
        if (selectedPosition == null)
            return true;

        // If an opponent’s piece is clicked while a piece is already selected,
        // allow selection only if the move between them is illegal.
        Move move = new Move(selectedPosition, position, '\0');
        return !game.isMoveLegal(move);
    }

    public void select(Position position) {
        if (position == null)
            throw new IllegalArgumentException("Error: Selected Position is null.");

        boardPanel.clearMarkedRed();
        selectedPosition = position;
        boardPanel.setHighlight(position, true);

        if (game.getTurn() == color) {
            List<Position> legalMoves = game.getLegalMoves(position);
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
