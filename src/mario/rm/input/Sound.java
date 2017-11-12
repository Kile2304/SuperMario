package mario.rm.input;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import mario.MainComponent;
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class Sound {

    private static ArrayList<FloatControl> control = new ArrayList<>();
    
    private Clip clip;

    public static boolean soundON = false;

    public Sound(String path) {
        try {
            if (!MainComponent.jar.isFile()) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(MainComponent.class.getClassLoader().getResourceAsStream(path));
                clip = AudioSystem.getClip();
                clip.open(ais);
            } else {
                //add buffer for mark/reset support
                InputStream bufferedIn = new BufferedInputStream(MainComponent.class.getClassLoader().getResourceAsStream(path));
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            }
            FloatControl gainControl
                    = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            Log.append("gainControl: "+gainControl, DefaultFont.INFORMATION);
            float value = 50.0f;
            float range = Math.abs(gainControl.getMinimum()) + Math.abs(gainControl.getMaximum());
            float temp = (float) (((value / 100.0) * range) - Math.abs(gainControl.getMinimum()));
            gainControl.setValue(temp);
            control.add(gainControl);
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            Log.append(Log.stackTraceToString(e), DefaultFont.ERROR);
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
                    Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
                }
                time += (System.currentTimeMillis() - temp);
            }
        }
    }

    public static FloatControl getVolume(){
        return control.get(0);
    }
    
    public static void setVolume(float volume) {
        for (int i = 0; i < control.size(); i++) {
            control.get(i).setValue(volume);
        }
    }

}
