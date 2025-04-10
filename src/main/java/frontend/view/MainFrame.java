package frontend.view;

import frontend.model.assets.AssetManager;
import frontend.view.utils.BackgroundImagedPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(900, 600));
        setMinimumSize(new Dimension(900, 600));

        // Background
        BackgroundImagedPanel background = new BackgroundImagedPanel();
        background.setImage(AssetManager.getInstance().getThemeImage("background"));
        setLayout(new BorderLayout());
        add(background, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }
}
