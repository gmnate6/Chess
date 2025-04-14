package frontend.controller;

import frontend.controller.game.AbstractGameController;
import frontend.controller.menu.TitleController;
import frontend.model.SettingsManager;
import frontend.model.assets.AssetManager;
import frontend.view.MainFrame;

public class MainController {
    private static MainController instance;
    private BaseController activeController;
    private final MainFrame mainFrame;

    public MainController(MainFrame mainFrame) {
        instance = this;
        this.mainFrame = mainFrame;

        // Load Assets / Settings
        AssetManager.getInstance();
        SettingsManager.getInstance();

        // Load Title
        switchTo(new TitleController());
    }

    private static MainController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MainController is not initialized");
        }
        return instance;
    }

    public static void switchTo(BaseController controller) {
        if (instance.activeController != null) {

            // Play Transition Sound
            if (!(controller instanceof AbstractGameController)) {
                AssetManager.getInstance().playSound("menu-click");
            }

            instance.activeController.dispose();
        }
        instance.activeController = controller;
        instance.mainFrame.setContentPanel(controller.getPanel());
    }

    public static void forceRedraw() {
        MainController instance = MainController.getInstance();
        instance.mainFrame.forceRedraw();
    }
}
