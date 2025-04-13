package frontend.view.game;

import frontend.model.assets.AssetManager;
import frontend.view.components.panels.TranslucentPanel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class HistoryPanel extends TranslucentPanel {
    private final int rowHeight = 32;
    private int rowCount = 0;

    private JPanel currentRow;
    private JPanel movesPanel;

    private final JPanel contentPanel;
    private final JScrollPane scrollPane;

    public HistoryPanel() {
        super();
        setLayout(new BorderLayout());

        // --- content goes here ---
        contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // --- wrap in a transparent scrollPane ---
        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setViewportBorder(null);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(new Color(0,0,0,0));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // --- customize the vertical scrollbar to a simple black thumb ---
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setOpaque(false);
        vBar.setBackground(new Color(0,0,0,0));
        vBar.setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = AssetManager.getInstance().getThemeColor("translucent");
                trackColor = new Color(0,0,0,0);
            }
            @Override protected JButton createDecreaseButton(int orientation) {
                return makeZeroButton();
            }
            @Override protected JButton createIncreaseButton(int orientation) {
                return makeZeroButton();
            }
            private JButton makeZeroButton() {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(0,0));
                btn.setMinimumSize(new Dimension(0,0));
                btn.setMaximumSize(new Dimension(0,0));
                return btn;
            }
        });
        vBar.setUnitIncrement(rowHeight); // scroll speed = one row at a time

        add(scrollPane, BorderLayout.CENTER);
    }

    public void addMove(String move) {
        boolean completed = false;

        if (currentRow == null) {
            rowCount++;
            // alternate background
            if (rowCount % 2 == 0) {
                currentRow = new JPanel(new BorderLayout());
                currentRow.setOpaque(false);
            } else {
                currentRow = new TranslucentPanel(new BorderLayout());
            }
            currentRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));

            // row number on the left
            JLabel countLabel = new JLabel(rowCount + ".");
            countLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
            countLabel.setHorizontalAlignment(SwingConstants.LEFT);
            countLabel.setVerticalAlignment(SwingConstants.CENTER);
            countLabel.setFont(AssetManager.getInstance().getFont("chess_font", 16));
            countLabel.setForeground(AssetManager.getInstance().getThemeColor("text"));
            countLabel.setOpaque(false);
            countLabel.setPreferredSize(new Dimension(40, rowHeight));
            currentRow.add(countLabel, BorderLayout.WEST);

            // container for the two moves
            movesPanel = new JPanel(new GridLayout(1,2));
            movesPanel.setOpaque(false);
            currentRow.add(movesPanel, BorderLayout.CENTER);

            contentPanel.add(currentRow);
        } else {
            completed = true;
        }

        // create and align the move label
        JLabel moveLabel = new JLabel(move);
        moveLabel.setVerticalAlignment(SwingConstants.CENTER);
        moveLabel.setOpaque(false);
        moveLabel.setFont(AssetManager.getInstance().getFont("chess_font", 16));
        moveLabel.setForeground(AssetManager.getInstance().getThemeColor("text"));
        moveLabel.setHorizontalAlignment(completed
                ? SwingConstants.CENTER  // second move
                : SwingConstants.LEFT);  // first (or only) move

        movesPanel.add(moveLabel);

        if (completed) {
            currentRow = null;
            movesPanel = null;
        }

        // re‑layout & auto‑scroll to bottom
        contentPanel.revalidate();
        contentPanel.repaint();
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = scrollPane.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }
}
