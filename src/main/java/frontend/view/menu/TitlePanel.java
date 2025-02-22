package frontend.gui.main;

import frontend.gui.utils.Styler;

import javax.swing.*;
import java.awt.*;

public class TitlePanel extends JPanel {
    private final double contentWidthPercent = .4;
    private final double contentHeightPercent = .8;

    public TitlePanel(MainMenuPanel mainMenuPanel) {
        setLayout(new GridBagLayout());
        setOpaque(false);

        // Translucent Panel
        TranslucentPanel translucentPanel = new TranslucentPanel(mainMenuPanel.WIDTH * contentWidthPercent, mainMenuPanel.HEIGHT * contentHeightPercent);
        translucentPanel.setLayout(new BoxLayout(translucentPanel, BoxLayout.Y_AXIS));

        // Title Label
        JLabel titleLabel = new JLabel("Chess");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 60)); // Big and bold font
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center horizontally
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Align center in BoxLayout

        // Separator (line below the title)
        JSeparator separator = new JSeparator();
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        separator.setMaximumSize(new Dimension(200, 2)); // Set the size of the separator
        separator.setForeground(new Color(0, 0, 0, 50)); // Line color

        // Play Button
        JButton playButton = new JButton("Play");
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        Styler.styleButton(playButton);
        playButton.addActionListener(e -> mainMenuPanel.cardLayout.show(mainMenuPanel.contentPanel, "LobbyPanel"));

        // Add components to the translucent panel
        translucentPanel.add(Box.createVerticalStrut(50)); // Spacer for vertical spacing above the title
        translucentPanel.add(titleLabel);
        translucentPanel.add(Box.createVerticalStrut(10)); // Spacer between the title and the separator
        translucentPanel.add(separator);
        translucentPanel.add(Box.createVerticalStrut(30)); // Spacer between the line and the button
        translucentPanel.add(playButton);

        // Use GridBagConstraints to center the translucent panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // No stretching horizontally
        gbc.weighty = 0; // No stretching vertically
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment
        add(translucentPanel, gbc);
    }
}
