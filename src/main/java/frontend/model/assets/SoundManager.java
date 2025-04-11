package frontend.model.assets;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private final Map<String, Clip> sounds = new HashMap<>();

    public SoundManager() {
        loadSounds();
    }

    public void loadSound(String key, String path) {
        try {
            // Get the resource as a stream
            var resource = SoundManager.class.getResource(path);

            if (resource != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(resource);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                sounds.put(key, clip); // Store the clip in the map
            } else {
                System.err.println("Sound file not found in resources: " + path);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading sound: " + e.getMessage());
        }
    }

    public void loadSounds() {
        loadSound("capture", "/sounds/capture.wav");
        loadSound("castle", "/sounds/castle.wav");
        loadSound("decline", "/sounds/decline.wav");
        loadSound("draw-offer", "/sounds/draw-offer.wav");
        loadSound("game-draw", "/sounds/game-draw.wav");
        loadSound("game-end", "/sounds/game-end.wav");
        loadSound("game-start", "/sounds/game-start.wav");
        loadSound("game-win", "/sounds/game-win.wav");
        loadSound("illegal", "/sounds/illegal.wav");
        loadSound("move-check", "/sounds/move-check.wav");
        loadSound("move-opponent", "/sounds/move-opponent.wav");
        loadSound("move-self", "/sounds/move-self.wav");
        loadSound("notify", "/sounds/notify.wav");
        loadSound("premove", "/sounds/premove.wav");
        loadSound("promote", "/sounds/promote.wav");
        loadSound("ten-seconds", "/sounds/ten-seconds.wav");
    }

    public Clip getSound(String key) {
        return sounds.get(key);
    }

    public void playSound(String key) {
        Clip clip = getSound(key);
        if (clip == null) {
            System.err.println("Sound not found for key: " + key);
            return;
        }

        // Set volume
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(0.0f);

        // Stop current clip
        if (clip.isRunning()) {
            clip.stop();
        }

        // Play
        clip.setFramePosition(0);
        clip.start();
    }
}
