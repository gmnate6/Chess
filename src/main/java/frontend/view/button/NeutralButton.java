package frontend.view.button;

import frontend.model.assets.AssetManager;

public class NeutralButton extends CustomButton {
    public NeutralButton(String text) {
        super(
                text,
                AssetManager.getInstance().getThemeColor("opaque"),
                AssetManager.getInstance().getThemeColor("color")
        );
    }
    public NeutralButton(int glyph) {
        this();
        setGlyph(glyph);
    }
    public NeutralButton() {
        this("");
    }
}
