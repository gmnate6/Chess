package frontend.controller;

import engine.ai.StockfishAI;
import engine.game.Game;
import engine.game.Timer;
import engine.pieces.Piece;
import engine.types.Move;

import engine.types.Position;
import engine.utils.FEN;
import engine.utils.MoveUtils;
import frontend.view.game.BoardPanel;
import frontend.model.GameModel;

import utils.Color;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController {
    // private final GamePanel gamePanel;
    private final BoardPanel boardPanel;
    private final GameModel gameModel;
    private Game game;
    private Color color;

    // State Vars
    private boolean isPieceSelected = false; // Updated only during the up click event
    private Position selectedPosition = null;
    private Move preMove = null;

    public GameController(BoardPanel gamePanel, GameModel gameModel) {
        // this.gamePanel = gamePanel;
        this.boardPanel = gamePanel;
        this.gameModel = gameModel;
    }

    public void startGame(Color color, Timer timer) {
        this.game = new Game(timer);
        this.color = color;
        setPerspective(color);
    }

    public void setPerspective(Color color) {
        this.color = color;
        boardPanel.setPerspective(color);
        boardPanel.loadPieces(game);

        // Add SquareButton Listeners
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Position pressedPosition = boardPanel.pointToPosition(e.getPoint());
                if (SwingUtilities.isLeftMouseButton(e)) {
                    onSquareButtonLeftDown(pressedPosition);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    onSquareButtonRightDown(pressedPosition);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Position releasePosition = boardPanel.pointToPosition(e.getPoint());
                if (SwingUtilities.isLeftMouseButton(e)) {
                    onSquareButtonLeftUp(releasePosition);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    onSquareButtonRightUp(releasePosition);
                }
            }
        });
    }

    public void onSquareButtonLeftDown(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Error: Position is null.");
        }
        Piece piece = game.board.getPieceAt(position);

        // Select
        if (piece != null && piece.getColor() == color) {
            // Deselect if already selecting and selecting different piece
            if (selectedPosition != null && !selectedPosition.equals(position)) {
                deselect();
            }
            select(position);
            return;
        }

        // Process Move
        if (selectedPosition == null) { return; }
        Move move = new Move(selectedPosition, position, '\0');
        processPlayerMove(move);
        deselect();
    }

    public void onSquareButtonLeftUp(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Error: Position is null.");
        }
        if (selectedPosition == null) { return; }

        // Drop Piece
        boardPanel.dropPiece();

        // Dragged Move
        if (!selectedPosition.equals(position)) {
            Move move = new Move(selectedPosition, position, '\0');
            processPlayerMove(move);
            deselect();
            return;
        }

        // Update isPieceSelected
        if (!isPieceSelected) {
            isPieceSelected = true;
            return;
        }

        // Deselect if selected already selected piece
        deselect();
    }

    private void select(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Error: Selected Position is null.");
        }

        // Update Selected Position
        selectedPosition = position;

        // Add Highlight
        boardPanel.setHighlight(position, true);

        // Add Hints
        for (Position pos : game.getLegalMoves(position)) {
            boardPanel.setHint(pos, true);
        }

        // Grab Piece
        boardPanel.grabPiece(position);
    }

    private void deselect() {
        if (selectedPosition != null) {
            boardPanel.setHighlight(selectedPosition, false);
        }
        boardPanel.clearHints();
        boardPanel.dropPiece();
        selectedPosition = null;
        isPieceSelected = false;
    }

    public void onSquareButtonRightDown(Position position) {
        // Ignore for now
    }

    public void onSquareButtonRightUp(Position position) {
        // Ignore for now
    }

    public void processPlayerMove(Move move) {
        // Remove Premove
        if (preMove != null) {
            boardPanel.setHighlight(preMove.initialPosition(), false);
            boardPanel.setHighlight(preMove.finalPosition(), false);
            preMove = null;
        }

        // Not your move / set preMove
        if (game.getTurn() != color) {
            preMove = move;
            return;
        }

        // If move is promotion
        if (MoveUtils.causesPromotion(move, game)) {
            move = new Move(move.initialPosition(), move.finalPosition(), 'Q'); // TODO: Ask player for promotion piece
        }

        // Move must be legal
        if (!game.isMoveLegal(move)) { return; }

        // Execute Move
        executeMove(move);

        /// Temp
        processServerMove(null);
    }
    public void processServerMove(Move move) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                executeMove(StockfishAI.getMove(game));
                return null;
            }
        }.execute();
    }

    public void executeMove(Move move) {
        System.out.println("Move: " + MoveUtils.toAlgebraic(move, game));
        game.move(move);
        boardPanel.loadPieces(game);
    }
}
