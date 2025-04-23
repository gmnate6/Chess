package com.nathanholmberg.chess.client.view.game;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.components.button.ColorButton;
import com.nathanholmberg.chess.client.view.components.button.TranslucentButton;
import com.nathanholmberg.chess.client.view.components.panels.TranslucentPanel;
import com.nathanholmberg.chess.client.view.utils.SquareLayoutManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GamePanel extends JPanel {
    public BoardPanel boardPanel;
    public BannerPanel topBannerPanel;
    public BannerPanel bottomBannerPanel;
    public HistoryPanel historyPanel;

    // Action Panel
    private final JPanel actionPanel;
    public TranslucentButton firstMoveButton;
    public TranslucentButton previousMoveButton;
    public TranslucentButton nextMoveButton;
    public TranslucentButton lastMoveButton;

    // In Game Action Panel
    public JPanel inGameActionPanel;
    public TranslucentButton drawButton;
    public TranslucentButton resignButton;

    // Post Game Action Panel
    public JPanel postGameActionPanel;
    public ColorButton rematchButton;
    public TranslucentButton backButton;

    public GamePanel() {
        setOpaque(false);
        setLayout(new BorderLayout());

        // Left Panel
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        leftPanel.setOpaque(false);
        add(leftPanel, BorderLayout.CENTER);

        // Top Player Panel
        JPanel topBufferPanel = new JPanel(new BorderLayout());
        topBufferPanel.setOpaque(false);
        topBannerPanel = new BannerPanel();
        topBufferPanel.add(topBannerPanel, BorderLayout.CENTER);

        // Board Buffer Panel
        JPanel boardBufferPanel = new JPanel(new SquareLayoutManager());
        boardBufferPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        boardBufferPanel.setOpaque(false);

        // Board Panel
        boardPanel = new BoardPanel();
        boardBufferPanel.add(boardPanel);

        // Bottom Player Panel
        JPanel bottomBufferPanel = new JPanel(new BorderLayout());
        bottomBufferPanel.setOpaque(false);
        bottomBannerPanel = new BannerPanel();
        bottomBufferPanel.add(bottomBannerPanel, BorderLayout.CENTER);

        // Add
        leftPanel.add(topBufferPanel, BorderLayout.NORTH);
        leftPanel.add(boardBufferPanel, BorderLayout.CENTER);
        leftPanel.add(bottomBufferPanel, BorderLayout.SOUTH);

        // Right Panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(350, 600));
        rightPanel.setBorder(new EmptyBorder(10, 0, 10, 10));
        add(rightPanel, BorderLayout.EAST);

        // Right Content Panel
        JPanel rightContentPanel = new TranslucentPanel(new BorderLayout());
        rightContentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        rightPanel.add(rightContentPanel, BorderLayout.CENTER);

        // Title
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Chess", SwingConstants.CENTER);
        titleLabel.setFont(AssetManager.getFont("chess_font", 60));
        titleLabel.setForeground(AssetManager.getThemeColor("text"));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        rightContentPanel.add(titlePanel, BorderLayout.NORTH);

        // History
        JPanel historyBufferPanel = new JPanel(new BorderLayout());
        historyBufferPanel.setOpaque(false);
        historyBufferPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
        rightContentPanel.add(historyBufferPanel, BorderLayout.CENTER);

        historyPanel = new HistoryPanel();
        historyBufferPanel.add(historyPanel, BorderLayout.CENTER);

        // Action Panel
        actionPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        actionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        actionPanel.setOpaque(false);
        rightContentPanel.add(actionPanel, BorderLayout.SOUTH);

        // Action Top Panel
        JPanel actionTopPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        actionTopPanel.setOpaque(false);
        actionPanel.add(actionTopPanel);

        firstMoveButton = new TranslucentButton(new String(Character.toChars(0x23)));
        firstMoveButton.setFont(AssetManager.getFont("chess_glyph", 32));
        actionTopPanel.add(firstMoveButton);

        previousMoveButton = new TranslucentButton(new String(Character.toChars(0x2c)));
        previousMoveButton.setFont(AssetManager.getFont("chess_glyph", 32));
        actionTopPanel.add(previousMoveButton);

        nextMoveButton = new TranslucentButton(new String(Character.toChars(0x2026)));
        nextMoveButton.setFont(AssetManager.getFont("chess_glyph", 32));
        actionTopPanel.add(nextMoveButton);

        lastMoveButton = new TranslucentButton(new String(Character.toChars(0x40)));
        lastMoveButton.setFont(AssetManager.getFont("chess_glyph", 32));
        actionTopPanel.add(lastMoveButton);

        // In Game Action Panel
        createInGameActionPanel();
        actionPanel.add(inGameActionPanel);

        // Post Game Action Panel
        createPostGameActionPanel();
    }

    private void createInGameActionPanel() {
        inGameActionPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        inGameActionPanel.setOpaque(false);

        drawButton = new TranslucentButton(new String(Character.toChars(0x2b)));
        drawButton.setFont(AssetManager.getFont("chess_glyph", 32));
        // drawButton.setPreferredSize(new Dimension(120, 40));
        drawButton.setMargin(new Insets(10, 10, 10, 10));
        inGameActionPanel.add(drawButton);

        resignButton = new TranslucentButton(new String(Character.toChars(0x59)));
        resignButton.setFont(AssetManager.getFont("chess_glyph", 32));
        resignButton.setMargin(new Insets(10, 10, 10, 10));
        // resignButton.setPreferredSize(new Dimension(120, 40));
        inGameActionPanel.add(resignButton);
    }

    private void createPostGameActionPanel() {
        postGameActionPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        postGameActionPanel.setOpaque(false);

        rematchButton = new ColorButton("Rematch");
        postGameActionPanel.add(rematchButton);

        backButton = new TranslucentButton("Back");
        postGameActionPanel.add(backButton);
    }

    public void setTopTimer(String time) {
        topBannerPanel.timerLabel.setText(time);
    }
    public void setBottomTimer(String time) {
        bottomBannerPanel.timerLabel.setText(time);
    }
    public void setTopUsername(String username) {
        topBannerPanel.profilePanel.setUsername(username);
    }
    public void setBottomUsername(String username) {
        bottomBannerPanel.profilePanel.setUsername(username);
    }
    public void setTopAvatar(String avatar) { topBannerPanel.profilePanel.setAvatar(avatar);}
    public void setBottomAvatar(String avatar) { bottomBannerPanel.profilePanel.setAvatar(avatar);}

    public void setTimerEnabled(boolean enabled) {
        topBannerPanel.timerBufferPanel.setVisible(enabled);
        bottomBannerPanel.timerBufferPanel.setVisible(enabled);
    }

    public void showPostGameActionPanel() {
        // Remove In Game Action Panel
        actionPanel.remove(inGameActionPanel);

        // Add Post Game Action Panel
        actionPanel.add(postGameActionPanel, BorderLayout.SOUTH);

        // Update
        revalidate();
        repaint();
    }
}
