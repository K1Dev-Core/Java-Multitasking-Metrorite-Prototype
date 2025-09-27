import java.io.File;
import javax.sound.sampled.*;

public class SoundManager {
    private static final int MAX_CONCURRENT_SOUNDS = 10;
    private static int currentSounds = 0;
    private static boolean soundEnabled = true;
    private static Clip backgroundClip;

    public static void playExplosion() {
        if (soundEnabled) {
            playSound("assets/sound/explosion.wav");
        }
    }

    public static void playDrip() {
        if (soundEnabled) {
            playSound("assets/sound/drip.wav");
        }
    }

    private static synchronized void playSound(String path) {
        if (currentSounds >= MAX_CONCURRENT_SOUNDS) {
            return;
        }
        
        new Thread(() -> {
            try {
                currentSounds++;
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(new File(path)));
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControl.setValue(-10.0f);
                
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        currentSounds--;
                    }
                });
                
                clip.start();
            } catch (Exception e) {
                currentSounds--;
            }
        }).start();
    }

    public static void playBackground() {
        if (soundEnabled) {
            new Thread(() -> {
                try {
                    backgroundClip = AudioSystem.getClip();
                    backgroundClip.open(AudioSystem.getAudioInputStream(new File("assets/sound/bg.wav")));
                    
                    FloatControl volumeControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
                    volumeControl.setValue(-20.0f);
                    
                    backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
                } catch (Exception e) {
                }
            }).start();
        }
    }

    public static void stopBackground() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }

    public static void toggleSound() {
        soundEnabled = !soundEnabled;
        if (!soundEnabled) {
            stopBackground();
        } else {
            playBackground();
        }
    }

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }
}