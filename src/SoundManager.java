import java.io.File;
import javax.sound.sampled.*;

public class SoundManager {
    private static Clip explosionSound;
    private static Clip dripSound;
    private static Clip backgroundSound;

    static {
        try {
            explosionSound = AudioSystem.getClip();
            explosionSound.open(AudioSystem.getAudioInputStream(new File("assets/sound/explosion.wav")));

            dripSound = AudioSystem.getClip();
            dripSound.open(AudioSystem.getAudioInputStream(new File("assets/sound/drip.wav")));

            backgroundSound = AudioSystem.getClip();
            backgroundSound.open(AudioSystem.getAudioInputStream(new File("assets/sound/bg.wav")));
        } catch (Exception e) {
            System.out.println("Cannot load sounds: " + e.getMessage());
        }
    }

    public static void playExplosion() {
        if (explosionSound != null) {
            explosionSound.setFramePosition(0);
            explosionSound.start();
        }
    }

    public static void playDrip() {
        if (dripSound != null) {
            dripSound.setFramePosition(0);
            dripSound.start();
        }
    }

    public static void playBackground() {
        if (backgroundSound != null) {
            backgroundSound.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public static void stopBackground() {
        if (backgroundSound != null) {
            backgroundSound.stop();
        }
    }
}