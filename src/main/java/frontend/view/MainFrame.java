package frontend.view;

import frontend.view.menu.MenuPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chess - Main Menu");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(800, 600));
            frame.setResizable(false);

            // Add the MainMenuPanel to the frame
            frame.add(new MenuPanel());
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
