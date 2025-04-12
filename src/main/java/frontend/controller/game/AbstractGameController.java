package frontend.controller.game;

import engine.game.ChessTimer;
import engine.game.Game;
import engine.pieces.Piece;
import engine.types.Move;
import engine.types.Position;
import engine.utils.MoveUtils;
import frontend.controller.BaseController;
import frontend.controller.MainController;
import frontend.controller.menu.TitleController;
import frontend.model.SettingsManager;
import frontend.model.assets.AssetManager;
import frontend.view.game.BoardPanel;
import frontend.view.game.GamePanel;
import frontend.view.game.Square;
import utils.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class AbstractGameController implements BaseController {
    protected final GamePanel gamePanel;
    protected final BoardPanel boardPanel;
    protected Game game;
    protected Color color;
    protected Timer swingTimer;

    // State Vars
    protected boolean isPieceSelected = false; // Updated only during the up click event
    protected Position selectedPosition = null;
    protected Position markedPosition = null;
    protected Move preMove = null;

    public AbstractGameController() {
        this.gamePanel = new GamePanel();
        this.boardPanel = gamePanel.boardPanel;
    }

    @Override
    public void dispose() {
        // Remove Mouse Listeners
        for (Component component : boardPanel.getComponents()) {
            for (MouseListener listener : component.getMouseListeners()) {
                component.removeMouseListener(listener);
            }
        }

        // Stop Timer
        swingTimer.stop();
    }

    @Override
    public JPanel getPanel() {
        return gamePanel;
    }

    protected void startGame(Color color, ChessTimer chessTimer) {
        this.game = new Game(chessTimer);
        this.color = color;
        boardPanel.loadPieces(game);

        // Play Sound
        AssetManager.getInstance().playSound("game-start");

        // Set Bottom Banner
        gamePanel.setBottomAvatar(AssetManager.getInstance().getAvatar(SettingsManager.getInstance().getAvatar()));
        gamePanel.setBottomUsername(SettingsManager.getInstance().getUsername());

        // Update Timer
        gamePanel.setTimerEnabled(game.getTimer() != null);
        swingTimer = new Timer(200, e -> {
            if (game.getTimer() == null) { return; }
            gamePanel.setTopTimer(game.getTimer().getFormatedTimeLeft(color.inverse()));
            gamePanel.setBottomTimer(game.getTimer().getFormatedTimeLeft(color));
        });
        swingTimer.start();

        // Back Button Listener
        gamePanel.backButton.addActionListener(
                e -> MainController.switchTo(new TitleController())
        );

        // Resign Button Listener
        gamePanel.resignButton.addActionListener(e -> {
            // Run the dialog box in a new thread to avoid blocking the game
            new Thread(() -> {
                int response = JOptionPane.showConfirmDialog(
                        gamePanel,                             // Parent component
                        "Are you sure you want to resign?",    // Message
                        "Confirm Resignation",                 // Title
                        JOptionPane.YES_NO_OPTION,             // Option type
                        JOptionPane.QUESTION_MESSAGE           // Message type
                );

                // Handle the response
                if (response == JOptionPane.YES_OPTION) {
                    resign();
                }
            }).start();
        });


        // Board Listeners
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

    protected void setPerspective(Color color) {
        if (game.getTimer() != null && game.getTimer().isActive()) {
            System.err.println("Cannot change perspective while timer is active.");
            return;
        }
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

    private void onSquareButtonLeftDown(Position position) {
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

    private void onSquareButtonLeftUp(Position position) {
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

    private void onSquareButtonRightDown(Position position) {
        if (position == null) { return; }
        markedPosition = position;
        setPreMove(null);
    }

    private void onSquareButtonRightUp(Position position) {
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

    protected void processPlayerMove(Move move) {
        if (!game.inPlay()) { return; }

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

    protected void resign() {
        // Resign game
        game.resign(color);
        endGame();
    }

    protected void executeMove(Move move) {
        // Play Move sound
        if (!MoveUtils.causesCheckmate(move, game)) {
            playMoveSound(move);
        }

        // Move
        game.move(move);
        boardPanel.loadPieces(game);

        // End Game
        if (!game.inPlay()) {
            endGame();
        }
    }

    // Call before executing move
    protected void playMoveSound(Move move) {
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

    protected void endGame() {
        if (game.inPlay()) {
            System.err.println("Called endGame while game is still in play.");
            return;
        }

        // Play sound
        playEndSound();

        // Stop Timer
        swingTimer.stop();

        // Update view
        gamePanel.showBackButton();
    }

    protected void playEndSound() {
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
    }
}
