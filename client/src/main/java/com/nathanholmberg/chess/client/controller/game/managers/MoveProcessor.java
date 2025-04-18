package com.nathanholmberg.chess.client.controller.game.managers;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.game.BoardPanel;
import com.nathanholmberg.chess.client.view.game.GamePanel;
import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.Game;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.utils.MoveUtils;

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
