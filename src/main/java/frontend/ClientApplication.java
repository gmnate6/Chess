package frontend;

import frontend.controller.MainController;
import frontend.view.MainFrame;

public class ClientApplication {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        new MainController(mainFrame);
        mainFrame.setVisible(true);
    }
}
