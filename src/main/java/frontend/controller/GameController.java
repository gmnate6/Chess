package frontend.controller;

import engine.game.Timer;
import engine.utils.Move;
import frontend.view.game.BoardPanel;
import frontend.view.game.SquareButton;

import frontend.model.GameModel;

import engine.game.Game;
import engine.utils.Position;
import engine.pieces.Piece;

import utils.Color;
import utils.RandomMoveAI;

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
        game = new Game(new Timer(600_000, 0));
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
        squareButton.setHighLight(true);
        selectedPiecePosition = position;

        // Show Move Hints
        List<Position> finalPositions = game.getLegalMoves(position);
        for (Position pos : finalPositions) {
            boardPanel.getSquareButton(pos.file(), pos.rank()).setHint(true);
        }
    }

    // Make Move
    public void makeMove(Move move) {
        // Make Move
        game.move(move);

        // Update BoardPanel
        boardPanel.loadFromBoard(game.getBoard());

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
        if (selectedPiecePosition != null) {
            Move move = new Move(selectedPiecePosition, clickedPosition, 'q');
            if (game.isMoveLegal(move)) {
                makeMove(move);

                /// Random Black Move
                makeMove(RandomMoveAI.getMove(game));
                /// Random Black Move
                return;
            }
        }

        // Handle Selected Piece Position
        selectPiece(clickedPosition);
    }
}
