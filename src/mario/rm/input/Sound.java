package mario.rm.input;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import mario.MainComponent;

/**
 *
 * @author LENOVO
 */
public class Sound {

    private Clip clip;

    public static boolean soundON = true;

    public Sound(String path) {
        try {
            if (!MainComponent.jar.isFile()) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(MainComponent.class.getClassLoader().getResourceAsStream(path));
                clip = AudioSystem.getClip();
                clip.open(ais);
            }else{
                //add buffer for mark/reset support
                InputStream bufferedIn = new BufferedInputStream(MainComponent.class.getClassLoader().getResourceAsStream(path));
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            }
        } catch (IOException | LineUnavailableException e) {
            System.out.println("" + e);
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("" + ex);
        }
    }

    public void start() {
        if (soundON) {
            clip.start();
        }
    }

    public void stop() {
        if (soundON) {
            clip.setFramePosition(0);
            clip.stop();
        }
    }

    public void loop() {
        if (soundON) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public int getCurrentFrame() {
        return (int) clip.getLongFramePosition();
    }

    public void isRunning() {
        if (soundON) {
            long time = 0;
            while (time < clip.getMicrosecondLength() / 1000) {
                long temp = System.currentTimeMillis();
                try {
                    sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
                }
                time += (System.currentTimeMillis() - temp);
            }
        }
    }

}