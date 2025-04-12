package frontend.view.game;

import frontend.model.assets.AssetManager;
import frontend.view.components.button.TranslucentButton;
import frontend.view.components.button.TransparentButton;
import frontend.view.components.panels.TranslucentPanel;
import frontend.view.components.panels.BackgroundImagedPanel;
import frontend.view.utils.SquareLayoutManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GamePanel extends JPanel {
    public BoardPanel boardPanel;
    public BannerPanel topBannerPanel;
    public BannerPanel bottomBannerPanel;

    private JPanel rightContentPanel;
    public JPanel buttonsPanel;
    public TransparentButton drawButton;
    public TransparentButton resignButton;

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
        historyBufferPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        TranslucentPanel historyPanel = new TranslucentPanel(new GridLayout(1, 2, 10, 0));
        historyBufferPanel.add(historyPanel, BorderLayout.CENTER);

        rightContentPanel.add(historyBufferPanel, BorderLayout.CENTER);

        // Buttons
        buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        rightContentPanel.add(buttonsPanel, BorderLayout.SOUTH);

        drawButton = new TransparentButton(new String(Character.toChars(0x2b)));
        drawButton.setFont(AssetManager.getInstance().getFont("chess_glyph", 32));
        drawButton.setPreferredSize(new Dimension(120, 40));
        drawButton.setMargin(new Insets(10, 10, 10, 10));
        buttonsPanel.add(drawButton);

        resignButton = new TransparentButton(new String(Character.toChars(0x59)));
        resignButton.setFont(AssetManager.getInstance().getFont("chess_glyph", 32));
        resignButton.setMargin(new Insets(10, 10, 10, 10));
        resignButton.setPreferredSize(new Dimension(120, 40));
        buttonsPanel.add(resignButton);

        // Back Button
        backButton = new TranslucentButton("Back");
        backButton.setMargin(new Insets(10, 10, 10, 10));
        backButton.setEnabled(false);
    }

    public void setTopTimer(String time) {
        topBannerPanel.timerLabel.setText(time);
    }
    public void setBottomTimer(String time) {
        bottomBannerPanel.timerLabel.setText(time);
    }
    public void setTopUsername(String username) {
        topBannerPanel.usernameLabel.setText(username);
    }
    public void setBottomUsername(String username) {
        bottomBannerPanel.usernameLabel.setText(username);
    }
    public void setTopAvatar(Image avatar) { topBannerPanel.avatarPanel.setImage(avatar);}
    public void setBottomAvatar(Image avatar) { bottomBannerPanel.avatarPanel.setImage(avatar);}

    public void setTimerEnabled(boolean enabled) {
        topBannerPanel.timerBufferPanel.setVisible(enabled);
        bottomBannerPanel.timerBufferPanel.setVisible(enabled);
    }

    public void showBackButton() {
        // Disable
        buttonsPanel.setVisible(false);
        drawButton.setEnabled(false);
        resignButton.setEnabled(false);
        rightContentPanel.remove(backButton);

        // Enable
        backButton.setVisible(true);
        backButton.setEnabled(true);
        rightContentPanel.add(backButton, BorderLayout.SOUTH);
    }
}
