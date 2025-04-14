package frontend.view.game;

import frontend.model.assets.AssetManager;
import frontend.view.components.panels.DynamicImagedPanel;
import utils.Color;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.function.Consumer;

public class PromotionPanel extends JPanel {
    private final Color color;
    private final Consumer<Character> onPieceChosen;

    private DynamicImagedPanel queenPanel;
    private DynamicImagedPanel rookPanel;
    private DynamicImagedPanel bishopPanel;
    private DynamicImagedPanel knightPanel;


    public PromotionPanel(Color color, int squareSize, Consumer<Character> onPieceChosen) {
        this.color = color;
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
        boolean capital = color == Color.WHITE;

        queenPanel = new DynamicImagedPanel();
        queenPanel.setImage(assetManager.getThemeImage(capital ? "q".toUpperCase() : "q"));
        add(queenPanel);

        rookPanel = new DynamicImagedPanel();
        rookPanel.setImage(assetManager.getThemeImage(capital ? "r".toUpperCase() : "r"));
        add(rookPanel);

        bishopPanel = new DynamicImagedPanel();
        bishopPanel.setImage(assetManager.getThemeImage(capital ? "b".toUpperCase() : "b"));
        add(bishopPanel);

        knightPanel = new DynamicImagedPanel();
        knightPanel.setImage(assetManager.getThemeImage(capital ? "n".toUpperCase() : "n"));
        add(knightPanel);
    }

    private void addListeners() {
        queenPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onPieceChosen.accept('Q');
            }
        });

        rookPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onPieceChosen.accept('R');
            }
        });

        bishopPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onPieceChosen.accept('B');
            }
        });

        knightPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onPieceChosen.accept('N');
            }
        });
    }
}
