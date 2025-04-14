package frontend.view.game;

import frontend.model.assets.AssetManager;
import frontend.view.components.panels.DynamicImagedPanel;
import utils.Color;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class PromotionPanel extends JPanel {
    private final Color color;
    private final boolean rightSideUp;
    private final Consumer<Character> onPieceChosen;

    private DynamicImagedPanel queenPanel;
    private DynamicImagedPanel rookPanel;
    private DynamicImagedPanel bishopPanel;
    private DynamicImagedPanel knightPanel;

    public PromotionPanel(Color color, boolean rightSideUp, int squareSize, Consumer<Character> onPieceChosen) {
        this.color = color;
        this.rightSideUp = rightSideUp;
        this.onPieceChosen = onPieceChosen;

        setPreferredSize(new Dimension(squareSize, 4 * squareSize));
        setLayout(new GridLayout(4, 1));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        AssetManager assetManager = AssetManager.getInstance();
        setBackground(assetManager.getThemeColor("boardWhite"));
        setBorder(new LineBorder(assetManager.getThemeColor("boardBlack")));

        build();
        addListeners();
    }

    private void build() {
        AssetManager assetManager = AssetManager.getInstance();
        boolean isWhite = color == Color.WHITE;

        queenPanel = new DynamicImagedPanel();
        queenPanel.setImage(assetManager.getThemeImage(getPieceKey('q', isWhite)));

        rookPanel = new DynamicImagedPanel();
        rookPanel.setImage(assetManager.getThemeImage(getPieceKey('r', isWhite)));

        bishopPanel = new DynamicImagedPanel();
        bishopPanel.setImage(assetManager.getThemeImage(getPieceKey('b', isWhite)));

        knightPanel = new DynamicImagedPanel();
        knightPanel.setImage(assetManager.getThemeImage(getPieceKey('n', isWhite)));

        if (rightSideUp) {
            add(queenPanel);
            add(rookPanel);
            add(bishopPanel);
            add(knightPanel);
        } else {
            add(knightPanel);
            add(bishopPanel);
            add(rookPanel);
            add(queenPanel);
        }
    }

    private void addListeners() {
        addPieceListener(queenPanel, 'Q');
        addPieceListener(rookPanel, 'R');
        addPieceListener(bishopPanel, 'B');
        addPieceListener(knightPanel, 'N');
    }

    private void addPieceListener(DynamicImagedPanel panel, char pieceChar) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onPieceChosen.accept(pieceChar);
            }
        });
    }

    private String getPieceKey(char piece, boolean isWhite) {
        return String.valueOf(isWhite ? Character.toUpperCase(piece) : Character.toLowerCase(piece));
    }
}
