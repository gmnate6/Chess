package com.nathanholmberg.chess.client.model.assets;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Theme {
    private final ImageManager imageManager;
    private final ColorManager colorManager;
    private final String themeName;
    private final String path;

    public Theme(String themeName) {
        imageManager = new ImageManager();
        colorManager = new ColorManager();

        this.themeName = themeName;
        this.path = "themes/" + themeName;
        loadImages();
        loadColors();
    }

    private void loadImages() {
        imageManager.loadImage("P", path + "/images/wp.png");
        imageManager.loadImage("R", path + "/images/wr.png");
        imageManager.loadImage("N", path + "/images/wn.png");
        imageManager.loadImage("B", path + "/images/wb.png");
        imageManager.loadImage("Q", path + "/images/wq.png");
        imageManager.loadImage("K", path + "/images/wk.png");
        imageManager.loadImage("p", path + "/images/bp.png");
        imageManager.loadImage("r", path + "/images/br.png");
        imageManager.loadImage("n", path + "/images/bn.png");
        imageManager.loadImage("b", path + "/images/bb.png");
        imageManager.loadImage("q", path + "/images/bq.png");
        imageManager.loadImage("k", path + "/images/bk.png");
        imageManager.loadImage("board", path + "/images/board.png");
        imageManager.loadImage("background", path + "/images/background.png");
        imageManager.loadImage("hint", path + "/images/hint.png");
        imageManager.loadImage("capture_hint", path + "/images/capture_hint.png");
    }

    private void loadColors() {
        colorManager.loadColors( path + "/colors.json");
    }

    public String getThemeName() {
        return themeName;
    }

    public String getPath() {
        return path;
    }

    public BufferedImage getImage(String key) {
        return imageManager.getImage(key);
    }

    public Color getColor(String key) {
        return colorManager.getColor(key);
    }

}
