package frontend.view.game;

import frontend.view.utils.SquareLayoutManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GamePanel extends JPanel {
    public BoardPanel boardPanel;
    public BannerPanel topBannerPanel;
    public BannerPanel bottomBannerPanel;

    public GamePanel() {
        /// Setup
        setLayout(new BorderLayout());
        setBackground(Color.orange);

        /// Left Panel
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);

        // Top Player Panel
        JPanel topBufferPanel = new JPanel(new BorderLayout());
        topBufferPanel.setOpaque(false);
        topBufferPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topBannerPanel = new BannerPanel();
        topBufferPanel.add(topBannerPanel, BorderLayout.CENTER);

        // Board Buffer Panel
        JPanel boardBufferPanel = new JPanel(new SquareLayoutManager());
        boardBufferPanel.setOpaque(false);

        // Board Panel
        boardPanel = new BoardPanel();
        boardBufferPanel.add(boardPanel);

        // Bottom Player Panel
        JPanel bottomBufferPanel = new JPanel(new BorderLayout());
        bottomBufferPanel.setOpaque(false);
        bottomBufferPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        bottomBannerPanel = new BannerPanel();
        bottomBufferPanel.add(bottomBannerPanel, BorderLayout.CENTER);

        // Add
        leftPanel.add(topBufferPanel, BorderLayout.NORTH);
        leftPanel.add(boardBufferPanel, BorderLayout.CENTER);
        leftPanel.add(bottomBufferPanel, BorderLayout.SOUTH);

        /// Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(300, 600));

        // Title
        // History
        // Buttons

        add(leftPanel, BorderLayout.CENTER); // Add left panel to the WEST
        add(rightPanel, BorderLayout.EAST); // Add right panel to the EAST
    }













    public void createJFrame() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chess - GamePanel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(900, 600));
            frame.setMinimumSize(new Dimension(900, 600));
            //frame.setResizable(false);

            // Add the BoardPanel to the frame
            frame.add(this);
            frame.setVisible(true);
        });
    }
    public static void main(String[] args) {
        GamePanel gamePanel = new GamePanel();
        gamePanel.createJFrame();
    }
}
