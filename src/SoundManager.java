import java.io.File;
import javax.sound.sampled.*;

public class SoundManager {
    private static final int MAX_CONCURRENT_SOUNDS = 10;
    private static int currentSounds = 0;

    public static void playExplosion() {
        playSound("assets/sound/explosion.wav");
    }

    public static void playDrip() {
        playSound("assets/sound/drip.wav");
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
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(new File("assets/sound/bg.wav")));
                
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControl.setValue(-20.0f);
                
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (Exception e) {
            }
        }).start();
    }

    public static void stopBackground() {
    }
}