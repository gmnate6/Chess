package com.nathanholmberg.chess.client.view.utils;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.model.server.endpoint.PingEndpoint;
import com.nathanholmberg.chess.client.view.components.panels.TranslucentPanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ServerStatusPanel extends TranslucentPanel {
    private final JLabel statusLabel;

    public ServerStatusPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Server Status:");
        titleLabel.setOpaque(false);
        titleLabel.setFont(AssetManager.getFont("chess_font", 16));
        titleLabel.setForeground(AssetManager.getThemeColor("text"));
        add(titleLabel);

        add(Box.createHorizontalStrut(5));

        statusLabel = new JLabel("Offline");
        statusLabel.setOpaque(false);
        statusLabel.setFont(AssetManager.getFont("chess_font", 16));
        statusLabel.setForeground(AssetManager.getThemeColor("text"));
        add(statusLabel);

        add(Box.createHorizontalStrut(5));

        revalidate();
        repaint();

        updateStatus();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateStatus();
            }
        });
    }

    public void updateStatus() {
        statusLabel.setText("-------");
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return PingEndpoint.ping();
            }

            @Override
            protected void done() {
                try {
                    boolean isServerAvailable = get();
                    statusLabel.setText(isServerAvailable ? "Online" : "Offline");
                } catch (Exception e) {
                    statusLabel.setText("Error");
                }
            }
        }.execute();
    }
}
