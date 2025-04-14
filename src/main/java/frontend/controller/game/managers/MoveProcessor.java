package frontend.controller.game.managers;

import engine.game.Game;
import engine.types.Move;
import engine.utils.MoveUtils;
import frontend.model.assets.AssetManager;
import frontend.view.game.BoardPanel;
import frontend.view.game.GamePanel;
import utils.Color;

public class MoveProcessor {
    private final Game game;
    private final BoardPanel boardPanel;
    private final GamePanel gamePanel;
    private final Color color;

    private Move preMove = null;

    public MoveProcessor(Game game, BoardPanel boardPanel, GamePanel gamePanel, Color color) {
        this.game = game;
        this.boardPanel = boardPanel;
        this.gamePanel = gamePanel;
        this.color = color;
    }

    public void clearPreMove() {
        if (preMove != null) {
            boardPanel.setMarkedRed(preMove.initialPosition(), false);
            boardPanel.setMarkedRed(preMove.finalPosition(), false);
            preMove = null;
        }
    }

    public void setPreMove(Move move) {
        if (move == null && preMove == null)
            return;
        if (move == null) {
            clearPreMove();
            return;
        }
        boardPanel.setMarkedRed(move.initialPosition(), true);
        boardPanel.setMarkedRed(move.finalPosition(), true);
        preMove = move;
        AssetManager.playSound("premove");
    }

    public boolean hasPreMove() {
        return preMove != null;
    }

    public Move getPreMove() {
        return preMove;
    }

    public boolean causesPromotion(Move move) {
        return engine.utils.MoveUtils.causesPromotion(move, game);
    }

    public void executeMove(Move move) {
        if (!MoveUtils.causesCheckmate(move, game)) {
            playMoveSound(move);
        }

        gamePanel.historyPanel.addMove(MoveUtils.toAlgebraic(move, game));
        game.move(move);
        boardPanel.loadPieces(game);
    }

    public void playMoveSound(Move move) {
        if (MoveUtils.causesCheck(move, game)) {
            AssetManager.playSound("move-check");
            return;
        }
        if (MoveUtils.isCapture(move, game)) {
            AssetManager.playSound("capture");
            return;
        }
        if (MoveUtils.isCastlingMove(move, game)) {
            AssetManager.playSound("castle");
            return;
        }
        if (game.getTurn() != color) {
            AssetManager.playSound("move-opponent");
            return;
        }
        if (game.getTurn() == color) {
            AssetManager.playSound("move-self");
        }
    }

    public void playEndSound() {
        if (game.inPlay())
            return;
        Color winner = game.getResult().getWinner();
        if (winner == null) {
            AssetManager.playSound("game-draw");
            return;
        }
        if (color == winner) {
            AssetManager.playSound("game-win");
            return;
        }
        AssetManager.playSound("game-end");
    }
}
