package frontend.controller;

import engine.ai.*;
import engine.game.Game;
import engine.pieces.Pawn;
import engine.types.Position;
import engine.pieces.Piece;
import engine.game.Timer;
import engine.types.Move;

import engine.utils.MoveUtils;
import engine.utils.PGN;
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

        // TODO
        if (!StockfishAI.doesStockfishExist()) {
            JOptionPane.showMessageDialog(null, "Stockfish is missing, you will play against randomAI.", "Missing Dependencies", JOptionPane.INFORMATION_MESSAGE);
        }
        if (color == Color.BLACK) {
            aiMove();
        }
        // AI vs AI
        if (color == null) {
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    while (game.inPlay()) {
                        makeMove(StockfishAI.getMove(game));
                    }
                    return null;
                }
            }.execute();
            color = Color.WHITE;
        }

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

    // Black's Move TODO
    public void aiMove() {
        if (!game.inPlay()) { return; }
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
    }

    // Make Move
    public void makeMove(Move move) {
        // Make Move
        game.move(move);

        // Update BoardPanel
        boardPanel.loadFromBoard(game.board);

        // Add High lights
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        boardPanel.getSquareButton(initialPosition.file(), initialPosition.rank()).setHighLight(true);
        boardPanel.getSquareButton(finalPosition.file(), finalPosition.rank()).setHighLight(true);

        // Print PGN
        if (!game.inPlay()) {
            String pgn = PGN.getPGN(game);
            String result = game.getResult().toString().replace("_", " ");
            String message =
                    "<html><body style='width: 300px;'>" + pgn + "</body></html>";
            JOptionPane.showMessageDialog(null, message, result, JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    // Handle Promotion
    private Move handlePromotion(Move move) {
        Piece pieceToMove = game.board.getPieceAt(move.initialPosition());
        if (move.promotionPiece() != '\0') { return move;}
        if (!(pieceToMove instanceof Pawn pawn)) { return move; }
        if (!pawn.isLegalPromotion(move, game.board)) { return move; }
        return new Move(move.initialPosition(), move.finalPosition(), 'Q');
    }

    // Square Button Listeners
    private void handleSquareClick(Position clickedPosition) {
        if (!game.inPlay()) { return; }

        // Try to make move
        if (selectedPosition != null) {
            Move move = new Move(selectedPosition, clickedPosition, '\0');
            move = handlePromotion(move);
            if (game.isMoveLegal(move)) {
                makeMove(move);
                aiMove(); // TODO
                return;
            }
        }

        // Handle Selected Piece Position
        selectPiece(clickedPosition);
    }
}
