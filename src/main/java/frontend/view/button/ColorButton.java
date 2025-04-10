package frontend.view.button;

import frontend.model.assets.AssetManager;

public class ColorButton extends CustomButton {
    public ColorButton(String text) {
        super(
                text,
                AssetManager.getInstance().getThemeColor("color"),
                AssetManager.getInstance().getThemeColor("opaque")
        );
    }
    public ColorButton(int glyph) {
        this();
        setGlyph(glyph);
    }
    public ColorButton() {
        this("");
    }
}
