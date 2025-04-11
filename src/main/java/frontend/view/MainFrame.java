package frontend.view;

import frontend.model.assets.AssetManager;
import frontend.view.menu.ContentPanel;
import frontend.view.menu.TitlePanel;
import frontend.view.utils.BackgroundImagedPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final BackgroundImagedPanel background;
    private ContentPanel contentPanel;

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

        // Title Panel
        setContentPanel(new TitlePanel());
    }

    public void setContentPanel(ContentPanel contentPanel) {
        if (this.contentPanel != null) {
            remove(this.contentPanel);
        }
        this.contentPanel = contentPanel;
        background.add(contentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }
}
