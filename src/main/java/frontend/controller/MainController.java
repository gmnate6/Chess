package frontend.controller;

import frontend.controller.menu.TitleMenuController;
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
        switchTo(new TitleMenuController());
    }

    public static MainController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MainController is not initialized");
        }
        return instance;
    }

    public void switchTo(BaseController controller) {
        if (activeController != null) {
            activeController.dispose();
        }
        activeController = controller;
        mainFrame.setContentPanel(controller.getPanel());
    }
}
