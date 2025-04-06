package frontend.controller;

import engine.ai.StockfishAI;
import engine.game.Game;
import engine.game.Timer;
import engine.pieces.Piece;
import engine.types.Move;

import engine.types.Position;
import engine.utils.MoveUtils;
import frontend.model.assets.AssetManager;
import frontend.view.game.BoardPanel;
import frontend.model.server.GameServerManager;

import frontend.view.game.GamePanel;
import frontend.view.game.Square;
import utils.Color;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController {
    private final GamePanel gamePanel;
    private final BoardPanel boardPanel;
    private final GameServerManager gameServerManager;
    private Game game;
    private Color color;

    // State Vars
    private boolean isPieceSelected = false; // Updated only during the up click event
    private Position selectedPosition = null;
    private Position markedPosition = null;
    private Move preMove = null;

    public GameController(GamePanel gamePanel, GameServerManager gameServerManager) {
        this.gamePanel = gamePanel;
        this.boardPanel = gamePanel.boardPanel;
        this.gameServerManager = gameServerManager;
    }

    public void startGame(Color color, Timer timer) {
        this.game = new Game(timer);
        this.color = color;
        setPerspective(color);

        // Add Listeners
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Get Position
                Position pressedPosition = boardPanel.pointToPosition(e.getPoint());
                if (color == Color.BLACK) {
                    pressedPosition = pressedPosition.inverse();
                }

                // Call method
                if (SwingUtilities.isLeftMouseButton(e)) {
                    onSquareButtonLeftDown(pressedPosition);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    onSquareButtonRightDown(pressedPosition);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Get Position
                Position releasePosition = boardPanel.pointToPosition(e.getPoint());
                if (color == Color.BLACK) {
                    releasePosition = releasePosition.inverse();
                }

                // Call method
                if (SwingUtilities.isLeftMouseButton(e)) {
                    onSquareButtonLeftUp(releasePosition);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    onSquareButtonRightUp(releasePosition);
                }
            }
        });
    }

    public void setPerspective(Color color) {
        boardPanel.setPerspective(color);
        boardPanel.loadPieces(game);
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

        // Clear Marked Red
        boardPanel.clearMarkedRed();

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

    private void setPreMove(Move move) {
        if (move == null && preMove == null) { return; }

        // Remove
        if (move == null) {
            boardPanel.setMarkedRed(preMove.initialPosition(), false);
            boardPanel.setMarkedRed(preMove.finalPosition(), false);
            preMove = null;
            return;
        }

        // Set
        boardPanel.setMarkedRed(move.initialPosition(), true);
        boardPanel.setMarkedRed(move.finalPosition(), true);
        preMove = move;

        // Play PreMove Sound
        AssetManager.getInstance().playSound("premove");
    }

    public void onSquareButtonRightDown(Position position) {
        if (position == null) { return; }
        markedPosition = position;
        setPreMove(null);
    }

    public void onSquareButtonRightUp(Position position) {
        if (position == null || markedPosition == null) { return; }

        // Mark Red
        if (position.equals(markedPosition)) {
            Square square = boardPanel.getSquare(position);
            boardPanel.setMarkedRed(position, !square.isMarkedRed());
        }
        markedPosition = null;
    }

    public void processPlayerMove(Move move) {
        // Deselect
        deselect();

        // Remove Premove
        if (preMove != null) {
            setPreMove(null);
        }

        // Not your move -> set preMove
        if (game.getTurn() != color) {
            setPreMove(move);
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
                if (preMove != null) {
                    processPlayerMove(preMove);
                }
                return null;
            }
        }.execute();
    }

    public void executeMove(Move move) {
        System.out.println("Move: " + MoveUtils.toAlgebraic(move, game));
        playMoveSound(move);
        game.move(move);
        boardPanel.loadPieces(game);
    }

    // Call before executing move
    public void playMoveSound(Move move) {
        // Capture
        if (MoveUtils.isCapture(move, game)) {
            AssetManager.getInstance().playSound("capture");
        }

        // Castle
        if (MoveUtils.isCastlingMove(move, game)) {
            AssetManager.getInstance().playSound("castle");
        }

        // Check
        if (MoveUtils.causesCheck(move, game)) {
            AssetManager.getInstance().playSound("move-check");
        }

        // Opponent Move
        if (game.getTurn() != color) {
            AssetManager.getInstance().playSound("move-opponent");
        }

        // Self Move
        if (game.getTurn() == color) {
            AssetManager.getInstance().playSound("move-self");
        }
    }
}
