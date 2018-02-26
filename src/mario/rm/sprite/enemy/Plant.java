package mario.rm.sprite.enemy;

import java.util.ArrayList;
import mario.rm.Animation.Cut;
import mario.rm.handler.Handler;

/**
 *
 * @author LENOVO
 */
/**
 *
 * @return NEICO CHE NON PUO' MORIRE, E CHE VA SU E GIU DAL TERRENO
 */
public class Plant extends Enemy {

    long before;
    private static final int stop = 2000;

    public Plant(int x, int y, int width, int height, Handler handler, String type, boolean canDie) {
        super(x, y, width, height, handler, type, canDie);
        direzioneY = y;
        this.y += height;
        velY = -2;
    }

    /**
     *
     * @return VA SU E GIU DAL TERRENO
     */
    public void tick() {
        if (velY < 0 && y > direzioneY || velY > 0 && y < direzioneY) {
            y += velY;
            before = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - before >= stop && velY < 0 || System.currentTimeMillis() - before >= stop - 1000 && velY > 0) {
            velY *= -1;
            y = direzioneY;
            direzioneY += (height * (velY / Math.abs(velY)));
        }
    }
}
