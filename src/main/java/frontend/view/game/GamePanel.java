package frontend.view.game;

import frontend.model.assets.AssetManager;
import frontend.view.components.button.ColorButton;
import frontend.view.components.button.TranslucentButton;
import frontend.view.components.panels.TranslucentPanel;
import frontend.view.utils.SquareLayoutManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GamePanel extends JPanel {
    public BoardPanel boardPanel;
    public BannerPanel topBannerPanel;
    public BannerPanel bottomBannerPanel;
    private final JPanel rightContentPanel;
    public HistoryPanel historyPanel;

    // Game Action Panel
    public JPanel gameActionPanel;
    public TranslucentButton drawButton;
    public TranslucentButton resignButton;

    // Post Game Action Panel
    public JPanel postGameActionPanel;
    public TranslucentButton firstMoveButton;
    public TranslucentButton previousMoveButton;
    public TranslucentButton nextMoveButton;
    public TranslucentButton lastMoveButton;
    public ColorButton rematchButton;
    public TranslucentButton backButton;

    public GamePanel() {
        /// Setup
        setOpaque(false);
        setLayout(new BorderLayout());

        /// Left Panel
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

        /// Right Panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(350, 600));
        rightPanel.setBorder(new EmptyBorder(10, 0, 10, 10));
        add(rightPanel, BorderLayout.EAST);

        // Right Content Panel
        rightContentPanel = new TranslucentPanel(new BorderLayout());
        rightContentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        rightPanel.add(rightContentPanel, BorderLayout.CENTER);

        // Title
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Chess", SwingConstants.CENTER);
        titleLabel.setFont(AssetManager.getInstance().getFont("chess_font", 60));
        titleLabel.setForeground(AssetManager.getInstance().getThemeColor("text"));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        rightContentPanel.add(titlePanel, BorderLayout.NORTH);

        // History
        JPanel historyBufferPanel = new JPanel(new BorderLayout());
        historyBufferPanel.setOpaque(false);
        historyBufferPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
        rightContentPanel.add(historyBufferPanel, BorderLayout.CENTER);

        historyPanel = new HistoryPanel();
        historyBufferPanel.add(historyPanel, BorderLayout.CENTER);

        // Game Action Panel
        createGameActionPanel();
        rightContentPanel.add(gameActionPanel, BorderLayout.SOUTH);

        // Post Game Action Panel
        createPostGameActionPanel();
    }

    private void createGameActionPanel() {
        gameActionPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        gameActionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        gameActionPanel.setOpaque(false);
        rightContentPanel.add(gameActionPanel, BorderLayout.SOUTH);

        drawButton = new TranslucentButton(new String(Character.toChars(0x2b)));
        drawButton.setFont(AssetManager.getInstance().getFont("chess_glyph", 32));
        drawButton.setPreferredSize(new Dimension(120, 40));
        drawButton.setMargin(new Insets(10, 10, 10, 10));
        gameActionPanel.add(drawButton);

        resignButton = new TranslucentButton(new String(Character.toChars(0x59)));
        resignButton.setFont(AssetManager.getInstance().getFont("chess_glyph", 32));
        resignButton.setMargin(new Insets(10, 10, 10, 10));
        resignButton.setPreferredSize(new Dimension(120, 40));
        gameActionPanel.add(resignButton);
    }

    private void createPostGameActionPanel() {
        postGameActionPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        postGameActionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        postGameActionPanel.setOpaque(false);

        // Navigation Top Panel
        JPanel navigationTopPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        navigationTopPanel.setOpaque(false);
        postGameActionPanel.add(navigationTopPanel);

        firstMoveButton = new TranslucentButton(new String(Character.toChars(0x23)));
        firstMoveButton.setFont(AssetManager.getInstance().getFont("chess_glyph", 32));
        navigationTopPanel.add(firstMoveButton);

        previousMoveButton = new TranslucentButton(new String(Character.toChars(0x2c)));
        previousMoveButton.setFont(AssetManager.getInstance().getFont("chess_glyph", 32));
        navigationTopPanel.add(previousMoveButton);

        nextMoveButton = new TranslucentButton(new String(Character.toChars(0x2026)));
        nextMoveButton.setFont(AssetManager.getInstance().getFont("chess_glyph", 32));
        navigationTopPanel.add(nextMoveButton);

        lastMoveButton = new TranslucentButton(new String(Character.toChars(0x40)));
        lastMoveButton.setFont(AssetManager.getInstance().getFont("chess_glyph", 32));
        navigationTopPanel.add(lastMoveButton);

        // Navigation Bottom Panel
        JPanel navigationBottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        navigationBottomPanel.setOpaque(false);
        postGameActionPanel.add(navigationBottomPanel);

        rematchButton = new ColorButton("Rematch");
        navigationBottomPanel.add(rematchButton);

        backButton = new TranslucentButton("Back");
        navigationBottomPanel.add(backButton);
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
        // Remove Game Action Panel
        rightContentPanel.remove(gameActionPanel);

        // Add Post Game Action Panel
        rightContentPanel.add(postGameActionPanel, BorderLayout.SOUTH);

        // Update
        revalidate();
        repaint();
    }
}
