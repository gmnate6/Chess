package frontend.controller.game;

import engine.ai.ChessAI;
import engine.types.Move;
import frontend.model.assets.AssetManager;
import frontend.view.game.GamePanel;
import utils.Color;

import javax.swing.*;

public class LocalBotGameController extends AbstractGameController {
    private final ChessAI ai;

    public LocalBotGameController(GamePanel gamePanel, Color color, ChessAI ai) {
        super(gamePanel);
        startGame(color, null);
        setPerspective(color);

        // Set AI
        this.ai = ai;
        gamePanel.setTopUsername(ai.getName());

        // First move bot
        if (color == Color.BLACK) {
            playBotMove();
        }
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
