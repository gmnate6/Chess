package frontend.view.button;

import frontend.model.assets.AssetManager;

public class ColorButton extends Button {
    public ColorButton(String text) {
        super(
                text,
                AssetManager.getInstance().getThemeColor("colorButton"),
                AssetManager.getInstance().getThemeColor("colorButtonHovered")
        );
    }
}
