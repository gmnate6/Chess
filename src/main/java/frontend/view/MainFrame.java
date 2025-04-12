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
        add(background, BorderLayout.CENTER);

        // Set Content
        setContentPanel(null);
    }

    public void setContentPanel(JPanel newContent) {
        // Remove old content if present
        if (contentPanel != null) {
            background.remove(contentPanel);
        }

        // Add new content if provided
        contentPanel = newContent;
        if (contentPanel != null) {
            background.add(contentPanel, BorderLayout.CENTER);
        }

        // Refresh
        background.revalidate();
        background.repaint();
    }

    public void forceRedraw () {
        background.setImage(AssetManager.getInstance().getThemeImage("background"));
        background.revalidate();
        background.repaint();
    }
}
