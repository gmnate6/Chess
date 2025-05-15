package com.nathanholmberg.chess.client.controller.game;

import com.nathanholmberg.chess.client.controller.MainController;
import com.nathanholmberg.chess.engine.ai.ChessAI;
import com.nathanholmberg.chess.engine.ai.StockfishAI;
import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.types.Move;

import javax.swing.*;

public class BotGameController extends AbstractGameController {
    private final ChessAI ai;

    public BotGameController(Color color, ChessAI ai) {
        super(color);
        start();

        this.ai = ai;
        configureBotGameSettings();

        // If the bot is meant to move first, initiate the bot move.
        if (color == Color.BLACK) {
            playBotMove();
        }
    }

    private void configureBotGameSettings() {
        // Update top banner to show AI
        if (ai instanceof StockfishAI) {
            gamePanel.setTopAvatar("engine");
        } else {
            gamePanel.setTopAvatar("bot");
        }
        gamePanel.setTopUsername(ai.toString());

        gamePanel.drawButton.setEnabled(false);
    }

    @Override
    protected void rematch() {
        super.rematch();
        MainController.switchTo(new BotGameController(color, ai));
    }

    public void playBotMove() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                Move botMove = ai.getMove(chessGame);

                // Execute move on the EDT.
                SwingUtilities.invokeLater(() -> processBotMove(botMove));
                return null;
            }
        }.execute();
    }

    private void processBotMove(Move botMove) {
        executeMove(botMove);

        // Process Pre Move
        if (moveProcessor.hasPreMove()) {
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    processPlayerMove(moveProcessor.getPreMove());
                    return null;
                }
            }.execute();
        }
    }

    @Override
    public void executeMove(Move move) {
        super.executeMove(move);

        // Play Bot Move
        if (chessGame.inPlay() && chessGame.getTurn() != color) {
            playBotMove();
        }
    }
}
