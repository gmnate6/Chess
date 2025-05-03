package com.nathanholmberg.chess.client.controller;

import com.nathanholmberg.chess.client.controller.game.AbstractGameController;
import com.nathanholmberg.chess.client.controller.menu.TitleController;
import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.MainFrame;

public class MainController {
    private static MainController instance;
    private BaseController activeController;
    private final MainFrame mainFrame;

    public MainController(MainFrame mainFrame) {
        instance = this;
        this.mainFrame = mainFrame;

        // Load Title
        switchTo(new TitleController());
    }

    public static void switchTo(BaseController controller) {
        if (instance.activeController != null) {
            // Play Transition Sound
            if (!(controller instanceof AbstractGameController)) {
                AssetManager.playSound("menu-click");
            }

            instance.activeController.dispose();
        }
        instance.activeController = controller;
        instance.mainFrame.setContentPanel(controller.getPanel());
    }

    public static void forceRedraw() {
        instance.mainFrame.forceRedraw();
    }
}
