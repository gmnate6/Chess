package frontend.controller;

import frontend.view.game.BoardPanel;
import frontend.view.game.SquareButton;

import frontend.model.GameModel;

import engine.Game;
import engine.board.Position;
import engine.pieces.Piece;

import utils.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GameController {
    private final BoardPanel boardPanel;
    private final GameModel gameModel;
    private Game game;

    private Color color;
    private Position selectedPiecePosition = null;

    public GameController(BoardPanel boardPanel, GameModel gameModel) {
        this.boardPanel = boardPanel;
        this.gameModel = gameModel;
    }

    // Creates New Game
    public void startGame(Color color, long initialTime, long increment) {
        game = new Game(initialTime, increment);
        this.color = color;

        // Initialize
        boardPanel.initializeBoard(color);
        initializeSquareButtons();

        // Load Board
        boardPanel.loadFromBoard(game.getBoard());
    }

    // Initializes Square Button Listeners
    private void initializeSquareButtons() {
        for (int file = 0; file < BoardPanel.SIZE; file++) {
            for (int rank = 0; rank < BoardPanel.SIZE; rank++) {
                final int f = file;
                final int r = rank;

                SquareButton squareButton = boardPanel.getSquareButton(file, rank);
                squareButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleSquareClick(new Position(f, r)); // Handle clicks
                    }
                });
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

        // Get Selected Piece
        Piece selectedPiece = game.getBoard().getPieceAt(position);

        if (selectedPiece == null) {
            selectedPiecePosition = null;
            return;
        }

        if (selectedPiece.getColor() != this.color) {
            selectedPiecePosition = null;
            return;
        }

        // Get SquareButton
        SquareButton squareButton = boardPanel.getSquareButton(position.file(), position.rank());

        // Select Square
        squareButton.setActive(true);
        selectedPiecePosition = position;

        // Show Move Hints
        List<Position> finalPositions = game.getLegalMoves(position);
        for (Position pos : finalPositions) {
            boardPanel.getSquareButton(pos.file(), pos.rank()).setHint(true);
        }
    }

    // Square Button Listeners
    private void handleSquareClick(Position position) {
        Piece selectedPiece = game.getBoard().getPieceAt(position);

        // Handle Selected Piece Position
        selectPiece(position);
    }
}
