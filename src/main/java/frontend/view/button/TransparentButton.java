package frontend.view.button;

import java.awt.*;

public class TransparentButton extends CustomButton {
    public TransparentButton(String text) {
        super(
                text,
                new Color(0, 0, 0, 0),
                new Color(0, 0, 0, 0)
        );
        setOpaque(false);
    }
    public TransparentButton(int glyph) {
        this();
        setGlyph(glyph);
    }
    public TransparentButton() {
        this("");
    }
}
