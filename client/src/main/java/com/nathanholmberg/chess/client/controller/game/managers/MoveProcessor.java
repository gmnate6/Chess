package com.nathanholmberg.chess.client.controller.game.managers;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.game.BoardPanel;
import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.ChessGame;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.utils.MoveUtils;

public class MoveProcessor {
    private final ChessGame chessGame;
    private final BoardPanel boardPanel;
    private final Color color;

    private Move preMove = null;

    public MoveProcessor(ChessGame chessGame, BoardPanel boardPanel, Color color) {
        this.chessGame = chessGame;
        this.boardPanel = boardPanel;
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
        if (!MoveUtils.causesCheckmate(move, chessGame)) {
            playMoveSound(move);
        }

        chessGame.move(move);
        boardPanel.loadPieces(chessGame);
    }

    public void playMoveSound(Move move) {
        if (MoveUtils.causesCheck(move, chessGame)) {
            AssetManager.playSound("move-check");
            return;
        }
        if (MoveUtils.isCapture(move, chessGame)) {
            AssetManager.playSound("capture");
            return;
        }
        if (MoveUtils.isCastlingMove(move, chessGame)) {
            AssetManager.playSound("castle");
            return;
        }
        if (chessGame.getTurn() != color) {
            AssetManager.playSound("move-opponent");
            return;
        }
        if (chessGame.getTurn() == color) {
            AssetManager.playSound("move-self");
        }
    }

    public void playEndSound() {
        if (chessGame.inPlay())
            return;
        Color winner = chessGame.getResult().getWinner();
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
