package frontend.model.assets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

public class AvatarManager {
    private final ImageManager avatars = new ImageManager();
    private final int size = 100;

    public AvatarManager() {
        loadAvatars();
    }

    private void loadAvatars() {
        String path = "avatars/";

        // Access the directory
        File folder = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource(path)).getPath());

        if (!folder.isDirectory()) {
            System.err.println("The path provided is not a directory: " + path);
        }

        // Loop through files
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (!file.isFile()) {
                continue;
            }

            // Check file extension for supported formats
            String fileName = file.getName();
            String extension = getFileExtension(fileName).toLowerCase();

            // Only load supported formats
            if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("svg")) {
                continue;
            }

            // Try to load file
            try {
                String key = fileName.substring(0, fileName.lastIndexOf('.'));
                String filePath = path + "/" + fileName;

                if (extension.equals("svg")) {
                    avatars.loadSVG(key, filePath, size, size);
                } else {
                    avatars.loadImage(key, filePath);
                }

                System.out.println("Loaded: " + fileName);
            } catch (Exception e) {
                System.err.println("Failed to load image: " + fileName);
                e.printStackTrace();
            }
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    public BufferedImage getAvatar(String key) {
        return avatars.getImage(key);
    }
}
