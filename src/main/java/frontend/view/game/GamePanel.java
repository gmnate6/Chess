package frontend.view.game;

import frontend.view.utils.SquareLayoutManager;

import javax.swing.*;
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
        setBackground(Color.red);

        /// Left Panel
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.BLUE);

        // Top Player Panel
        topBannerPanel = new BannerPanel();
        topBannerPanel.setBackground(Color.green);
        topBannerPanel.add(new JLabel("Top Player Panel")); // TODO: Remove

        // Board Buffer Panel
        JPanel boardBufferPanel = new JPanel(new SquareLayoutManager());
        boardBufferPanel.setOpaque(false);

        // Board Panel
        boardPanel = new BoardPanel();
        boardBufferPanel.add(boardPanel);

        // Bottom Player Panel
        bottomBannerPanel = new BannerPanel();
        bottomBannerPanel.setBackground(Color.green);
        bottomBannerPanel.add(new JLabel("Bottom Player Panel")); // TODO: Remove

        // Add
        leftPanel.add(topBannerPanel, BorderLayout.NORTH);
        leftPanel.add(boardBufferPanel, BorderLayout.CENTER);
        leftPanel.add(bottomBannerPanel, BorderLayout.SOUTH);

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
