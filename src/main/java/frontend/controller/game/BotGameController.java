package frontend.controller.game;

import engine.ai.ChessAI;
import engine.types.Move;
import frontend.controller.MainController;
import utils.Color;

import javax.swing.*;

public class BotGameController extends AbstractGameController {
    private final ChessAI ai;

    public BotGameController(Color color, ChessAI ai) {
        super(color, null);

        // Set AI
        this.ai = ai;
        gamePanel.setTopAvatar("bot");
        gamePanel.setTopUsername(ai.toString());

        // Disable Draw
        gamePanel.drawButton.setEnabled(false);

        // First move bot
        if (color == Color.BLACK) {
            playBotMove();
        }
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
                executeMove(ai.getMove(game));
                if (preMove != null) {
                    processPlayerMove(preMove);
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void executeMove(Move move) {
        super.executeMove(move);

        // Play sound
        if (!game.inPlay()) {
            playEndSound();
        }

        // Play Bot Move
        if (game.getTurn() != color) {
            playBotMove();
        }
    }
}
