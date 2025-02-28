package frontend.view.game;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    BoardPanel boardPanel;
    PlayerPanel topPlayerPanel;
    PlayerPanel bottomPlayerPanel;

    public GamePanel() {
        /// Setup
        setLayout(new BorderLayout());
        setBackground(Color.red);

        /// Left Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.BLUE);

        // Top Player Panel
        topPlayerPanel = new PlayerPanel();
        topPlayerPanel.setBackground(Color.green);
        topPlayerPanel.add(new JLabel("Top Player Panel")); // TODO: Remove

        // Board Panel
        boardPanel = new BoardPanel();
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
            frame.setVisible(true);
        });
    }
    public static void main(String[] args) {
        GamePanel gamePanel = new GamePanel();
        gamePanel.createJFrame();
    }
}
