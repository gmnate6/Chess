package frontend.controller.game;

import engine.game.ChessTimer;
import engine.game.Game;
import engine.pieces.Piece;
import engine.types.Move;
import engine.types.Position;
import engine.utils.MoveUtils;
import frontend.model.assets.AssetManager;
import frontend.view.game.BoardPanel;
import frontend.view.game.GamePanel;
import frontend.view.game.Square;
import utils.Color;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class AbstractGameController {
    protected final GamePanel gamePanel;
    protected final BoardPanel boardPanel;
    protected Game game;
    protected Color color;

    // State Vars
    protected boolean isPieceSelected = false; // Updated only during the up click event
    protected Position selectedPosition = null;
    protected Position markedPosition = null;
    protected Move preMove = null;

    public AbstractGameController(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.boardPanel = gamePanel.boardPanel;
    }

    public void startGame(Color color, ChessTimer chessTimer) {
        this.game = new Game(chessTimer);
        this.color = color;
        boardPanel.loadPieces(game);

        // Play Sound
        AssetManager.getInstance().playSound("game-start");

        // Update Timer
        gamePanel.setTimerEnabled(game.getTimer() != null);
        Timer swingTimer = new Timer(200, e -> {
            if (game.getTimer() == null) { return; }
            gamePanel.setTopTimer(game.getTimer().getFormatedTimeLeft(color.inverse()));
            gamePanel.setBottomTimer(game.getTimer().getFormatedTimeLeft(color));
        });
        swingTimer.start();

        // Add Listeners
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Get Position
                Position pressedPosition = boardPanel.pointToPosition(e.getPoint());

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

    // Helper method for onSquareButtonLeftDown, returns true if selection can be made
    private boolean canSelectPosition(Position position) {
        assert position != null;
        Piece piece = game.board.getPieceAt(position);

        // Cannot select non piece
        if (piece == null) { return false; }

        // My piece
        if (piece.getColor() == color) { return true; }

        // Select opponents piece if nothing is selected
        if (selectedPosition == null) { return true; }

        // Select opponents piece if move not legal
        Move move = new Move(selectedPosition, position, '\0');
        return !game.isMoveLegal(move);
    }

    public void onSquareButtonLeftDown(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Error: Position is null.");
        }

        // Select
        if (canSelectPosition(position)) {
            // Deselect
            if (!position.equals(selectedPosition)) {
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
            isPieceSelected = true;
            processPlayerMove(new Move(selectedPosition, position, '\0'));
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
        if (game.getTurn() == color) {
            for (Position pos : game.getLegalMoves(position)) {
                boardPanel.setHint(pos, true);
            }
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

    public void processPlayerMove(Move move) {
        // Deselect
        deselect();

        // Not your piece
        Piece piece = game.board.getPieceAt(move.initialPosition());
        if (piece != null && piece.getColor() != color) {
            return;
        }

        // Remove Pre move
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
        if (!game.isMoveLegal(move)) {
            // Puts king in check
            if (!game.isMoveSafe(move)) {
                AssetManager.getInstance().playSound("illegal");
                boardPanel.setMarkedRed(game.board.getKingPosition(color), true);
            }
            return;
        }

        // Execute Move
        executeMove(move);
    }

    public void executeMove(Move move) {
        // Play Move sound
        if (!MoveUtils.causesCheckmate(move, game)) {
            playMoveSound(move);
        }

        // Move
        game.move(move);
        boardPanel.loadPieces(game);

        // Play end game sound
        if (game.inPlay()) { return; }
        playEndSound();
    }

    // Call before executing move
    public void playMoveSound(Move move) {
        // Check
        if (MoveUtils.causesCheck(move, game)) {
            AssetManager.getInstance().playSound("move-check");
            return;
        }

        // Capture
        if (MoveUtils.isCapture(move, game)) {
            AssetManager.getInstance().playSound("capture");
            return;
        }

        // Castle
        if (MoveUtils.isCastlingMove(move, game)) {
            AssetManager.getInstance().playSound("castle");
            return;
        }

        // Opponent Move
        if (game.getTurn() != color) {
            AssetManager.getInstance().playSound("move-opponent");
            return;
        }

        // Self Move
        if (game.getTurn() == color) {
            AssetManager.getInstance().playSound("move-self");
        }
    }

    public void playEndSound() {
        if (game.inPlay()) { return; }
        Color winner = game.getResult().getWinner();

        // Draw
        if (winner == null) {
            AssetManager.getInstance().playSound("game-draw");
            return;
        }

        // Win
        if (color == winner) {
            AssetManager.getInstance().playSound("game-win");
            return;
        }

        // Lose
        AssetManager.getInstance().playSound("game-end");
        System.out.println("hit");
    }
}
