package frontend.view.menu;

import frontend.view.utils.Styler;

import javax.swing.*;
        import java.awt.*;

public class LobbyPanel extends JPanel {
    private final double contentWidthPercent = .8;
    private final double contentHeightPercent = .8;

    public LobbyPanel(MenuPanel mainMenuPanel) {
        setLayout(new GridBagLayout());
        setOpaque(false);

        // Translucent Panel
        TranslucentPanel translucentPanel = new TranslucentPanel(mainMenuPanel.WIDTH * contentWidthPercent, mainMenuPanel.HEIGHT * contentHeightPercent);
        translucentPanel.setLayout(new GridLayout(1, 2, 20, 0)); // Horizontal layout with a gap of 20px

        // Left Panel: Available Games
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Available Games"));

        // Fake Game List (use JList or JTable for real implementation)
        DefaultListModel<String> gameListModel = new DefaultListModel<>();
        gameListModel.addElement("Game 1 - Waiting");
        gameListModel.addElement("Game 2 - In Progress");
        gameListModel.addElement("Game 3 - Completed");
        JList<String> gameList = new JList<>(gameListModel);
        gameList.setOpaque(false);
        gameList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        // Scroll Pane for the Game List
        JScrollPane scrollPane = new JScrollPane(gameList);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        leftPanel.add(scrollPane);

        // Right Panel: Create a Game
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Create a Game"));

        // Create Game Form
        JLabel gameNameLabel = new JLabel("Game Name:");
        JTextField gameNameField = new JTextField();
        gameNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton createGameButton = new JButton("Create Game");
        Styler.styleButton(createGameButton); // Reuse styleButton method
        createGameButton.addActionListener(e -> {
            String gameName = gameNameField.getText();
            if (!gameName.isEmpty()) {
                // Action for creating a game
                JOptionPane.showMessageDialog(null, "Game \"" + gameName + "\" created!");
            } else {
                JOptionPane.showMessageDialog(null, "Game name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        rightPanel.add(gameNameLabel);
        rightPanel.add(Box.createVerticalStrut(10)); // Spacer
        rightPanel.add(gameNameField);
        rightPanel.add(Box.createVerticalStrut(20)); // Spacer
        rightPanel.add(createGameButton);

        // Add both panels to the translucent panel
        translucentPanel.add(leftPanel);
        translucentPanel.add(rightPanel);

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

