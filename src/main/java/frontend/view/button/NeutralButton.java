package frontend.view.button;

import frontend.model.assets.AssetManager;

public class NeutralButton extends Button {
    public NeutralButton(String text) {
        super(
                text,
                AssetManager.getInstance().getThemeColor("opaque"),
                AssetManager.getInstance().getThemeColor("color")
        );
    }
}
