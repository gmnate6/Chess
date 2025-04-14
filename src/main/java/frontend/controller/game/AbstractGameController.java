package frontend.controller.game;

import engine.game.ChessTimer;
import engine.game.Game;
import engine.types.Move;
import engine.types.Position;
import engine.utils.MoveUtils;
import frontend.controller.BaseController;
import frontend.controller.MainController;
import frontend.controller.game.listeners.BoardMouseListener;
import frontend.controller.game.managers.MoveProcessor;
import frontend.controller.game.managers.SelectionManager;
import frontend.controller.game.managers.TimerManager;
import frontend.controller.menu.TitleController;
import frontend.model.SettingsManager;
import frontend.model.assets.AssetManager;
import frontend.view.game.BoardPanel;
import frontend.view.game.GamePanel;
import utils.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public abstract class AbstractGameController implements BaseController {
    // Core objects
    protected Game game;
    protected Color color;

    // View elements
    public final GamePanel gamePanel;
    public final BoardPanel boardPanel;

    // Helper classes
    protected TimerManager timerManager;
    protected MoveProcessor moveProcessor;
    protected SelectionManager selectionManager;
    protected BoardMouseListener boardMouseListener;

    public AbstractGameController(Color color, ChessTimer chessTimer) {
        this.game = new Game(chessTimer);
        this.color = color;

        // Setup View
        this.gamePanel = new GamePanel();
        this.boardPanel = gamePanel.boardPanel;
        boardPanel.setPerspective(color);
        boardPanel.loadPieces(game);

        AssetManager.playSound("game-start");

        // Setup Bottom Banner
        gamePanel.setBottomAvatar(SettingsManager.getAvatar());
        gamePanel.setBottomUsername(SettingsManager.getUsername());

        // Timer Manager (encapsulates Swing timer logic)
        gamePanel.setTimerEnabled(game.getTimer() != null);
        timerManager = new TimerManager(game, gamePanel, color);
        timerManager.start();

        // Initialize helper classes for move processing and selection management
        moveProcessor = new MoveProcessor(game, boardPanel, gamePanel, color);
        selectionManager = new SelectionManager(boardPanel, game, color);

        // Setup Listeners
        setListeners();
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
        timerManager.stop();
    }

    @Override
    public JPanel getPanel() {
        return gamePanel;
    }

    protected void rematch() {
        if (game.inPlay()) {
            throw new IllegalStateException("Cannot rematch while game is in play.");
        }
    }

    private void setListeners() {
        // Use dedicated BoardMouseListener for board events
        boardMouseListener = new BoardMouseListener(this);
        boardPanel.addMouseListener(boardMouseListener);

        gamePanel.resignButton.addActionListener(e -> {
            if (confirm("Are you sure you want to resign?")) {
                resign();
            }
        });

        gamePanel.drawButton.addActionListener(e -> {
            if (confirm("Are you sure you want to offer draw?")) {
                System.out.println("Offering draw...");
            }
        });

        gamePanel.firstMoveButton.addActionListener(e -> {
            game.stepFullBack();
            afterStep();
        });

        gamePanel.previousMoveButton.addActionListener(e -> {
            game.stepBack();
            afterStep();
        });

        gamePanel.nextMoveButton.addActionListener(e -> {
            game.stepForward();
            afterStep();
        });

        gamePanel.lastMoveButton.addActionListener(e -> {
            game.stepFullForward();
            afterStep();
        });

        gamePanel.rematchButton.addActionListener(
                e -> rematch()
        );

        gamePanel.backButton.addActionListener(
                e -> MainController.switchTo(new TitleController())
        );
    }

    protected boolean confirm(String message) {
        AssetManager.playSound("notify");
        int response = JOptionPane.showConfirmDialog(
                gamePanel, message, "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        return response == JOptionPane.YES_OPTION;
    }

    private void updateEnableNavigationButtons() {
        // Enable navigation buttons by default
        gamePanel.firstMoveButton.setEnabled(true);
        gamePanel.previousMoveButton.setEnabled(true);
        gamePanel.nextMoveButton.setEnabled(true);
        gamePanel.lastMoveButton.setEnabled(true);

        // Disable navigation as needed based on move history
        int step = game.getMoveHistory().getCurrentMoveIndex();
        int min = -1;
        int max = game.getMoveHistory().getSize() - 1;

        if (step <= min) {
            gamePanel.firstMoveButton.setEnabled(false);
            gamePanel.previousMoveButton.setEnabled(false);
        }
        if (step >= max) {
            gamePanel.nextMoveButton.setEnabled(false);
            gamePanel.lastMoveButton.setEnabled(false);
        }
    }

    private void afterStep() {
        boardPanel.loadPieces(game);

        updateEnableNavigationButtons();

        // Play move sound for last move in history if available
        int step = game.getMoveHistory().getCurrentMoveIndex();
        if (step >= 0) {
            Move move = game.getMoveHistory().getLastMove();
            game.stepBack();
            moveProcessor.playMoveSound(move);
            game.stepForward();
        }
    }

    protected void setColor(Color color) {
        this.color = color;
        selectionManager.setColor(color);
    }

    protected void setPerspective(Color color) {
        if (game.getTimer() != null && game.getTimer().isActive()) {
            System.err.println("Cannot change perspective while timer is active.");
            return;
        }
        boardPanel.setPerspective(color);
        boardPanel.loadPieces(game);
    }

    public void onSquareButtonLeftDown(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Error: Position is null.");
        }

        // First check if a selection can be made.
        if (selectionManager.canSelectPosition(position)) {
            if (!position.equals(selectionManager.getSelectedPosition())) {
                selectionManager.deselect();
            }
            selectionManager.select(position);
            return;
        }

        if (selectionManager.getSelectedPosition() == null) {
            return;
        }

        // Process Player Move
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                Move move = new Move(selectionManager.getSelectedPosition(), position, '\0');
                processPlayerMove(move);
                return null;
            }
        }.execute();
    }

    public void onSquareButtonLeftUp(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Error: Position is null.");
        }

        if (selectionManager.getSelectedPosition() == null) {
            return;
        }

        boardPanel.dropPiece();

        // Drag move
        if (!selectionManager.getSelectedPosition().equals(position)) {
            selectionManager.setPieceSelected(true);

            // Process Player Move
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    Move move = new Move(selectionManager.getSelectedPosition(), position, '\0');
                    processPlayerMove(move);
                    return null;
                }
            }.execute();
            return;
        }

        if (!selectionManager.isPieceSelected()) {
            selectionManager.setPieceSelected(true);
            return;
        }

        selectionManager.deselect();
    }

    public void onSquareButtonRightDown(Position position) {
        if (position == null) {
            return;
        }
        selectionManager.setMarkedPosition(position);
        moveProcessor.clearPreMove();
    }

    public void onSquareButtonRightUp(Position position) {
        if (position == null || selectionManager.getMarkedPosition() == null) {
            return;
        }

        if (position.equals(selectionManager.getMarkedPosition())) {
            boardPanel.setMarkedRed(position, !boardPanel.getSquare(position).isMarkedRed());
        }
        selectionManager.clearMarkedPosition();
    }

    protected void processPlayerMove(Move move) {
        if (SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("This method must not be called on the Event Dispatch Thread (EDT)!");
        }

        if (!game.inPlay()) {
            return;
        }

        selectionManager.deselect();

        // Only allow moves with the controller’s own color piece.
        if (
                game.board.getPieceAt(move.initialPosition()) != null &&
                game.board.getPieceAt(move.initialPosition()).getColor() != color
        ) {
            return;
        }

        moveProcessor.clearPreMove();

        if (game.getTurn() != color) {
            moveProcessor.setPreMove(move);
            return;
        }

        // Check promotion possibility
        if (MoveUtils.causesPromotion(move, game)) {
            CompletableFuture<Character> future = boardPanel.promptPromotion(move.finalPosition(), color);

            // Remove Board Mouse Listener
            boardPanel.removeMouseListener(boardMouseListener);

            // Get promotion piece
            char selectedPiece;
            try {
                selectedPiece = future.get();
            } catch (ExecutionException | InterruptedException e) {
                selectedPiece = 'Q';
            }

            // Re add  Board Mouse Listener
            boardPanel.addMouseListener(boardMouseListener);

            // Apply promotion piece
            move = new Move(move.initialPosition(), move.finalPosition(), selectedPiece);
        }

        if (!game.isMoveLegal(move)) {
            // Check for unsafe move (e.g., putting king in check)
            if (!game.isMoveSafe(move)) {
                AssetManager.playSound("illegal");
                boardPanel.setMarkedRed(game.board.getKingPosition(color), true);
            }
            return;
        }

        executeMove(move);
    }

    protected void executeMove(Move move) {
        if (!game.inPlay()) {
            return;
        }
        moveProcessor.executeMove(move);

        // End Game
        if (!game.inPlay()) {
            endGame();
        }
    }

    protected void resign() {
        game.resign(color);
        endGame();
    }

    protected void endGame() {
        if (game.inPlay()) {
            System.err.println("Called endGame while game is still in play.");
            return;
        }

        moveProcessor.playEndSound();
        timerManager.stop();
        gamePanel.showPostGameActionPanel();
        updateEnableNavigationButtons();
    }
}
