package com.nathanholmberg.chess.client.controller.game;

import com.nathanholmberg.chess.client.controller.BaseController;
import com.nathanholmberg.chess.client.controller.MainController;
import com.nathanholmberg.chess.client.controller.game.listeners.BoardMouseListener;
import com.nathanholmberg.chess.client.controller.game.managers.HistoryManager;
import com.nathanholmberg.chess.client.controller.game.managers.MoveProcessor;
import com.nathanholmberg.chess.client.controller.game.managers.SelectionManager;
import com.nathanholmberg.chess.client.controller.game.managers.TimerManager;
import com.nathanholmberg.chess.client.controller.menu.TitleController;
import com.nathanholmberg.chess.client.model.SettingsManager;
import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.game.BoardPanel;
import com.nathanholmberg.chess.client.view.game.GamePanel;
import com.nathanholmberg.chess.client.view.components.ConfirmDialog;
import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.ChessGame;
import com.nathanholmberg.chess.engine.game.ChessTimer;
import com.nathanholmberg.chess.engine.pieces.Piece;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.types.Position;
import com.nathanholmberg.chess.engine.utils.MoveUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public abstract class AbstractGameController implements BaseController {
    // Core objects
    protected final ChessGame chessGame;
    protected ChessTimer chessTimer;
    protected Color color;
    private boolean inPlay = false;

    // View elements
    public final GamePanel gamePanel;
    public final BoardPanel boardPanel;

    // Helper classes
    protected final TimerManager timerManager;
    protected final MoveProcessor moveProcessor;
    protected final SelectionManager selectionManager;
    protected final BoardMouseListener boardMouseListener;
    protected final HistoryManager historyManager;

    public AbstractGameController(Color color, ChessTimer chessTimer) {
        this.chessGame = new ChessGame();
        this.chessTimer = chessTimer;
        this.color = color;

        // Setup View
        this.gamePanel = new GamePanel();
        this.boardPanel = gamePanel.boardPanel;
        boardPanel.setPerspective(color);
        boardPanel.loadPieces(chessGame);

        // Setup Bottom Banner
        gamePanel.setBottomAvatar(SettingsManager.getAvatar());
        gamePanel.setBottomUsername(SettingsManager.getUsername());

        // Timer Manager (encapsulates Swing timer logic)
        gamePanel.setTimerEnabled(chessTimer != null);
        timerManager = new TimerManager(chessTimer, gamePanel, color);

        // Initialize helper classes for move processing and selection management
        moveProcessor = new MoveProcessor(chessGame, boardPanel, color);
        selectionManager = new SelectionManager(boardPanel, chessGame, color);
        boardMouseListener = new BoardMouseListener(this);
        historyManager = new HistoryManager(this, gamePanel.historyPanel);

        // Setup Listeners
        setListeners();

        // Update Navigation Buttons
        updateEnableNavigationButtons();
    }

    public void start() {
        AssetManager.playSound("game-start");
        chessTimer.start();
        timerManager.start();
    }

    public boolean inPlay() {
        return inPlay;
    }

    public void stop() {
        inPlay = false;
        chessTimer.stop();
        timerManager.stop();
    }

    @Override
    public void dispose() {
        // Remove Mouse Listeners
        for (Component component : boardPanel.getComponents()) {
            for (MouseListener listener : component.getMouseListeners()) {
                component.removeMouseListener(listener);
            }
        }

        // Stop Timer Manager
        timerManager.stop();
    }

    @Override
    public JPanel getPanel() {
        return gamePanel;
    }

    private void setListeners() {
        // Use dedicated BoardMouseListener for board events
        boardPanel.addMouseListener(boardMouseListener);

        gamePanel.resignButton.addActionListener(e -> {
            if (ConfirmDialog.showDialog(gamePanel, "Are you sure you want to resign?")) {
                resign();
            }
        });

        gamePanel.drawButton.addActionListener(e -> {
            if (ConfirmDialog.showDialog(gamePanel, "Are you sure you want to offer draw?")) {
                System.out.println("Offering draw...");
            }
        });

        gamePanel.firstMoveButton.addActionListener(
                e -> stepFullBack()
        );

        gamePanel.previousMoveButton.addActionListener(
                e -> stepBack()
        );

        gamePanel.nextMoveButton.addActionListener(
                e -> stepForward()
        );

        gamePanel.lastMoveButton.addActionListener(
                e -> stepFullForward()
        );

        gamePanel.rematchButton.addActionListener(
                e -> rematch()
        );

        gamePanel.backButton.addActionListener(
                e -> MainController.switchTo(new TitleController())
        );
    }

    public void stepFullBack() {
        chessGame.stepFullBack();
        afterStep();
    }

    public void stepBack() {
        chessGame.stepBack();
        afterStep();
    }

    public void stepForward() {
        if (isAtLastMove()) { return; }
        chessGame.stepForward();
        afterStep();
    }

    public void stepFullForward() {
        if (isAtLastMove()) { return; }
        chessGame.stepFullForward();
        afterStep();
    }

    public boolean isAtLastMove() {
        return chessGame.getMoveHistory().isAtLastMove();
    }

    private void updateEnableNavigationButtons() {
        // Enable navigation buttons by default
        gamePanel.firstMoveButton.setEnabled(true);
        gamePanel.previousMoveButton.setEnabled(true);
        gamePanel.nextMoveButton.setEnabled(true);
        gamePanel.lastMoveButton.setEnabled(true);

        // Disable navigation as needed based on move history
        int step = chessGame.getMoveHistory().getCurrentMoveIndex();
        int min = -1;
        int max = chessGame.getMoveHistory().getSize() - 1;

        if (step <= min) {
            gamePanel.firstMoveButton.setEnabled(false);
            gamePanel.previousMoveButton.setEnabled(false);
        }
        if (step >= max) {
            gamePanel.nextMoveButton.setEnabled(false);
            gamePanel.lastMoveButton.setEnabled(false);
        }
    }

    public void loadGameStateAt(int moveIndex) {
        chessGame.loadGameStateAt(moveIndex);
        afterStep();
    }

    private void afterStep() {
        boardPanel.loadPieces(chessGame);

        updateEnableNavigationButtons();

        historyManager.selectMove(chessGame.getMoveHistory().getCurrentMoveIndex());

        // Play move sound for last move in history if available
        int step = chessGame.getMoveHistory().getCurrentMoveIndex();
        if (step >= 0) {
            Move move = chessGame.getMoveHistory().getLastMove();
            chessGame.stepBack();
            moveProcessor.playMoveSound(move);
            chessGame.stepForward();
        }
    }

    protected void rematch() {
        if (chessGame.inPlay()) {
            throw new IllegalStateException("Cannot rematch while game is in play.");
        }
    }

    protected void setColor(Color color) {
        this.color = color;
        selectionManager.setColor(color);
    }

    protected void setPerspective(Color color) {
        if (chessTimer != null && chessTimer.isActive()) {
            System.err.println("Cannot change perspective while timer is active.");
            return;
        }
        boardPanel.setPerspective(color);
        boardPanel.loadPieces(chessGame);
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

        if (!inPlay()) {
            return;
        }

        selectionManager.deselect();

        // Only allow moves with the controller’s own color piece.
        Piece pieceToMove = chessGame.board.getPieceAt(move.initialPosition());
        if (pieceToMove == null || pieceToMove.getColor() != color) {
            return;
        }

        moveProcessor.clearPreMove();

        if (chessGame.getTurn() != color) {
            moveProcessor.setPreMove(move);
            return;
        }

        // Check promotion possibility
        if (MoveUtils.causesPromotion(move, chessGame)) {
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

        if (!chessGame.isMoveLegal(move)) {
            // Check for unsafe move (e.g., putting king in check)
            if (!chessGame.isMoveSafe(move) && pieceToMove.isMoveValid(move, chessGame.board)) {
                AssetManager.playSound("illegal");
                boardPanel.setMarkedRed(chessGame.board.getKingPosition(color), true);
            }
            return;
        }

        executeMove(move);
    }

    protected void executeMove(Move move) {
        if (!chessGame.inPlay()) {
            return;
        }

        // Update History
        historyManager.addMove(MoveUtils.toAlgebraic(move, chessGame));

        // Execute Move
        moveProcessor.executeMove(move);

        // Update Timer
        if (chessTimer != null) {
            chessTimer.switchTurn();
        }

        // Update Navigation Buttons
        updateEnableNavigationButtons();

        // End Game
        if (!chessGame.inPlay()) {
            endGame();
        }
    }

    protected void resign() {
        chessGame.winByResign(color.inverse());
        endGame();
    }

    protected void endGame() {
        if (chessGame.inPlay()) {
            System.err.println("Called endGame while game is still in play.");
            return;
        }

        moveProcessor.playEndSound();
        timerManager.stop();
        gamePanel.showPostGameActionPanel();
    }
}
