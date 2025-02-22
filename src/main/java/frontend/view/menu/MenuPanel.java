package frontend.view.menu;

import frontend.view.utils.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JLayeredPane {
    public int WIDTH = 800;
    public int HEIGHT = 600;

    // Content
    public final JPanel contentPanel;
    public final CardLayout cardLayout;

    public MenuPanel() {
        setBackground(new Color(0xE5E5E5));
        setLayout(null);

        /// Background Panel
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setOpaque(false);

        // Constraints for positioning
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1; // Allow stretching horizontally to the right
        gbc.gridy = 1; // Allow stretching vertically to the bottom
        gbc.weightx = 1.0; // Expand horizontally
        gbc.weighty = 1.0; // Expand vertically
        gbc.anchor = GridBagConstraints.SOUTHEAST; // Snap to South-East corner

        // Decal Label
        ImageIcon originalDecal = ImageLoader.getImageIcon("decal.png");
        JLabel decalLabel = new JLabel(originalDecal);

        // Add Decal Icon
        Image scaledImage = originalDecal.getImage().getScaledInstance(HEIGHT, HEIGHT, Image.SCALE_SMOOTH);
        decalLabel.setIcon(new ImageIcon(scaledImage));

        // Add Decal Label
        backgroundPanel.add(decalLabel, gbc);

        /// Content Panel
        contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        // Add Layered Content
        contentPanel.add(new TitlePanel(this), "TitlePanel");
        contentPanel.add(new LobbyPanel(this), "LobbyPanel");

        // Add Panels to Self (Set Default Sizes)
        backgroundPanel.setBounds(0, 0, WIDTH, HEIGHT); // Fill the entire space
        contentPanel.setBounds(0, 0, WIDTH, HEIGHT);    // Fill the entire space
        add(backgroundPanel, JLayeredPane.DEFAULT_LAYER); // Background
        add(contentPanel, JLayeredPane.PALETTE_LAYER);   // Foreground

        /// Show Title Panel Initially
        cardLayout.show(contentPanel, "TitlePanel");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Load the wood texture image
        Image woodTexture = ImageLoader.getImageIcon("wood.png").getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);;
        // Draw the image on the entire background
        g.drawImage(woodTexture, 0, 0, getWidth(), getHeight(), this);
    }
}