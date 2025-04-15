package com.nathanholmberg.chess.client.view.game;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.components.panels.DynamicImagedPanel;
import com.nathanholmberg.chess.engine.enums.Color;

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

    public final int rowCount = 4;
    public final int columnCount = 1;

    private DynamicImagedPanel queenPanel;
    private DynamicImagedPanel rookPanel;
    private DynamicImagedPanel bishopPanel;
    private DynamicImagedPanel knightPanel;

    public PromotionPanel(Color color, boolean rightSideUp, Consumer<Character> onPieceChosen) {
        this.color = color;
        this.rightSideUp = rightSideUp;
        this.onPieceChosen = onPieceChosen;

        setLayout(new GridLayout(rowCount, columnCount));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setBackground(AssetManager.getThemeColor("boardWhite"));
        setBorder(new LineBorder(AssetManager.getThemeColor("boardBlack"), 2));

        build();
        addListeners();
    }

    private void build() {
        boolean isWhite = color == Color.WHITE;

        queenPanel = new DynamicImagedPanel();
        queenPanel.setImage(AssetManager.getThemeImage(getPieceKey('q', isWhite)));

        rookPanel = new DynamicImagedPanel();
        rookPanel.setImage(AssetManager.getThemeImage(getPieceKey('r', isWhite)));

        bishopPanel = new DynamicImagedPanel();
        bishopPanel.setImage(AssetManager.getThemeImage(getPieceKey('b', isWhite)));

        knightPanel = new DynamicImagedPanel();
        knightPanel.setImage(AssetManager.getThemeImage(getPieceKey('n', isWhite)));

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

    public void setSquareSize(int squareSize) {
        setPreferredSize(new Dimension(squareSize * columnCount, squareSize * rowCount));
    }
}
