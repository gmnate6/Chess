package frontend.view;

import frontend.view.button.TransparentButton;

import javax.swing.*;
import java.awt.*;

public class TestFrame extends JFrame {
    public TestFrame() {
        super("Test Frame");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setBackground(Color.RED);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLUE);
        panel.setPreferredSize(new Dimension(200, 500));
        add(panel, BorderLayout.EAST);

        panel.add(new TransparentButton("Test"));
        panel.add(new TransparentButton("Test"));
        panel.add(new TransparentButton("Test"));
        panel.add(new TransparentButton("Test"));
        panel.add(new TransparentButton("Test"));

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        new TestFrame();
    }
}

