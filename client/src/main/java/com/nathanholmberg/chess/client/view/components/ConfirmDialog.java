package com.nathanholmberg.chess.client.view.components;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.components.button.TranslucentButton;
import com.nathanholmberg.chess.client.view.components.panels.TranslucentPanel;

import javax.swing.*;
import java.awt.*;

public class ConfirmDialog extends JDialog {
    private boolean confirmed = false;

    public ConfirmDialog(Frame parent, String message) {
        super(parent, "Confirm", true);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // Buffer Panel
        JPanel bufferPanel = new JPanel(new BorderLayout(10, 10));
        bufferPanel.setBackground(AssetManager.getThemeColor("opaque"));
        bufferPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(bufferPanel, BorderLayout.CENTER);

        // Content Panel
        JPanel contentPanel = new TranslucentPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bufferPanel.add(contentPanel, BorderLayout.CENTER);

        // Custom message label
        String wrappedMessage = "<html><div style='text-align: center; width: 300px;'>" + message + "</div></html>";
        JLabel messageLabel = new JLabel(wrappedMessage);
        messageLabel.setForeground(AssetManager.getThemeColor("text"));
        messageLabel.setFont(AssetManager.getFont("chess_font", 32));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(messageLabel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Yes
        JButton yesButton = new TranslucentButton("Yes");
        yesButton.setPreferredSize(new Dimension(100, 40));
        buttonPanel.add(yesButton);
        yesButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        // No
        JButton noButton = new TranslucentButton("No");
        noButton.setPreferredSize(new Dimension(100, 40));
        buttonPanel.add(noButton);
        noButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public static boolean showDialog(Component parent, String message) {
        Frame frame = JOptionPane.getFrameForComponent(parent);
        ConfirmDialog dialog = new ConfirmDialog(frame, message);

        // Sound effect
        AssetManager.playSound("notify");

        dialog.setVisible(true);

        return dialog.isConfirmed();
    }
}