package frontend;

import frontend.controller.MainController;
import frontend.view.MainFrame;

import javax.swing.*;

public class ClientApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            new MainController(mainFrame);
            mainFrame.setVisible(true);
        });
    }
}
