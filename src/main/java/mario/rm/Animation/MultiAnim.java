package mario.rm.Animation;

import java.awt.image.BufferedImage;

/**
 *
 * @author LENOVO
 */
public class MultiAnim {
    
    private BufferedImage img;
    private int index;
    private long delay;
 
    public MultiAnim(BufferedImage img, int index, long delay){
        this.img = img;
        this.index = index;
        this.delay = delay;
    }

    public MultiAnim() {}

    public BufferedImage getImg() {
        return img;
    }

    public int getIndex() {
        return index;
    }
    
    public long getDelay(){
        return delay;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public void initialize(MultiAnim ma){
        img = ma.getImg();
        index = ma.getIndex();
        delay = ma.getDelay();
    }
    
}
