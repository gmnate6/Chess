package frontend.view.game;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    BoardPanel boardPanel;
    PlayerPanel topPlayerPanel;
    PlayerPanel bottomPlayerPanel;

    public GamePanel() {
        /// Setup
        setSize(new Dimension(900, 600));
        setLayout(new BorderLayout());
        setBackground(Color.red);

        /// Left Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.BLUE);
        leftPanel.setSize(new Dimension(560, 600));

        // Top Player Panel
        topPlayerPanel = new PlayerPanel();
        topPlayerPanel.setBackground(Color.green);
        topPlayerPanel.add(new JLabel("Top Player Panel")); // TODO: Remove

        // Board Panel
        boardPanel = new BoardPanel();
        boardPanel.setSize(new Dimension(560, 560));
        boardPanel.initializeBoard(utils.Color.WHITE);

        // Bottom Player Panel
        bottomPlayerPanel = new PlayerPanel();
        bottomPlayerPanel.setBackground(Color.green);
        bottomPlayerPanel.add(new JLabel("Bottom Player Panel")); // TODO: Remove

        // Add
        leftPanel.add(topPlayerPanel);
        leftPanel.add(boardPanel);
        leftPanel.add(bottomPlayerPanel);

        /// Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(300, 0));

        // Title
        // History
        // Buttons

        add(leftPanel, BorderLayout.WEST); // Add left panel to the WEST
        add(rightPanel, BorderLayout.EAST); // Add right panel to the EAST
    }













    public void createJFrame() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chess - BoardPanel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(900, 600));
            frame.setResizable(false);

            // Add the BoardPanel to the frame
            frame.add(this);
            frame.pack();
            frame.setVisible(true);
        });
    }
    public static void main(String[] args) {
        GamePanel gamePanel = new GamePanel();
        gamePanel.createJFrame();
    }
}
