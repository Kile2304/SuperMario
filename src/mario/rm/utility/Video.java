package mario.rm.utility;

import java.awt.Canvas;
import javax.swing.JFrame;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;

/**
 *
 * @author LENOVO
 */
public class Video extends Canvas {

    private EmbeddedMediaPlayer emp;

    public Video(JFrame frame) {
        MediaPlayerFactory mpf = new MediaPlayerFactory();
        emp = mpf.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(frame));
        emp.setVideoSurface(mpf.newVideoSurface(this));
    }

    public void play(String path) {
        emp.prepareMedia(path);
        emp.play();
    }

    public EmbeddedMediaPlayer getPlayer() {
        return emp;
    }

}
