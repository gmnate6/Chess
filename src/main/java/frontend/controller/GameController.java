package frontend.controller;

import engine.ai.*;
import engine.game.Game;
import engine.types.Position;
import engine.pieces.Piece;
import engine.game.Timer;
import engine.types.Move;

import engine.utils.MoveUtils;
import frontend.view.game.BoardPanel;
import frontend.view.game.SquareButton;
import frontend.model.GameModel;

import utils.Color;

import java.util.List;
import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class GameController {
    private final BoardPanel boardPanel;
    private final GameModel gameModel;
    private Game game;

    private Color color;
    private Position selectedPosition = null;

    public GameController(BoardPanel boardPanel, GameModel gameModel) {
        this.boardPanel = boardPanel;
        this.gameModel = gameModel;
    }

    // Creates New Game
    public void startGame(Color color, long initialTime, long increment) {
        game = new Game(new Timer(initialTime, increment));
        this.color = color;

        // Initialize
        boardPanel.initializeBoard(color);
        initializeSquareButtons();

        // Load Board
        boardPanel.loadFromBoard(game.board);
    }

    // Initializes Square Button Listeners
    private void initializeSquareButtons() {
        for (int file = 0; file < BoardPanel.SIZE; file++) {
            for (int rank = 0; rank < BoardPanel.SIZE; rank++) {
                final int f = file;
                final int r = rank;

                SquareButton squareButton = boardPanel.getSquareButton(file, rank);
                squareButton.addActionListener(e -> handleSquareClick(new Position(f, r)));
            }
        }
    }

    // Select Piece
    private void selectPiece(Position position) {
        if (position == null) {
            throw new NullPointerException("Cannot Select Null Piece Position");
        }

        // Clear Piece Overlays
        boardPanel.clearPieceOverlays();

        // Unselect Piece
        if (position.equals(selectedPosition)) {
            selectedPosition = null;
            return;
        }

        // Get Selected Piece
        Piece selectedPiece = game.board.getPieceAt(position);

        // Empty Click
        if (selectedPiece == null) {
            selectedPosition = null;
            return;
        }

        // Wrong Color
        if (selectedPiece.getColor() != this.color) {
            selectedPosition = null;
            return;
        }

        // Get SquareButton
        SquareButton squareButton = boardPanel.getSquareButton(position.file(), position.rank());

        // Select Square
        squareButton.setHighLight(true);
        selectedPosition = position;

        // Show Move Hints
        List<Position> finalPositions = game.getLegalMoves(position);
        for (Position pos : finalPositions) {
            boardPanel.getSquareButton(pos.file(), pos.rank()).setHint(true);
        }
    }

    // Make Move
    public void makeMove(Move move) {
        // Update Promotion Piece if needed
        if (MoveUtils.causesPromotion(move, game)) {
            move = new Move(move.initialPosition(), move.finalPosition(), 'Q');
        }

        // Make Move
        game.move(move);

        // Update BoardPanel
        boardPanel.loadFromBoard(game.board);

        // Add High lights
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        boardPanel.getSquareButton(initialPosition.file(), initialPosition.rank()).setHighLight(true);
        boardPanel.getSquareButton(finalPosition.file(), finalPosition.rank()).setHighLight(true);
    }

    // Square Button Listeners
    private void handleSquareClick(Position clickedPosition) {
        if (!game.isGameInPlay()) {
            return;
        }

        // Try to make move
        if (selectedPosition != null) {
            Move move = new Move(selectedPosition, clickedPosition, '\0');
            if (game.isMoveLegal(move)) {
                makeMove(move);

                /// Random Black Move
                new SwingWorker<Move, Void>() {
                    @Override
                    protected Move doInBackground() {
                        return StockfishAI.getMove(game); // Runs in a background thread
                    }

                    @Override
                    protected void done() {
                        try {
                            Move bestMove = get(); // Retrieve the computed move
                            makeMove(bestMove);
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }.execute();
                /// Random Black Move

                return;
            }
        }

        // Handle Selected Piece Position
        selectPiece(clickedPosition);
    }
}
