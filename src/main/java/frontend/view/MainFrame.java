package frontend.view;

import frontend.model.assets.AssetManager;
import frontend.view.components.panels.BackgroundImagedPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final BackgroundImagedPanel background;
    private JPanel contentPanel;

    public MainFrame() {
        super("Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(900, 600));
        setMinimumSize(new Dimension(900, 600));
        setLayout(new BorderLayout());

        // Background
        background = new BackgroundImagedPanel();
        background.setLayout(new BorderLayout());
        background.setImage(AssetManager.getInstance().getThemeImage("background"));
        add(background, BorderLayout.CENTER);
    }

    public void setContentPanel(JPanel contentPanel) {
        if (this.contentPanel != null) {
            background.remove(this.contentPanel);
        }
        this.contentPanel = contentPanel;
        background.add(contentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
