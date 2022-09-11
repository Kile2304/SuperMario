package mario.rm.sprite.enemy;

import mario.rm.handler.Handler;
import mario.rm.identifier.Direction;

/**
 *
 * @author LENOVO
 */
public class Cannone extends Enemy {

    private String bullet;

    private long before;

    private int delay;

    public Cannone(int x, int y, int width, int height, Handler handler, String type, boolean canDie, String type2) {
        super(x, y, width, height, handler, type, canDie);
        this.bullet = type2;
        delay = 4000;
        hurt = false;
    }

    @Override
    public void tick() {
        if (System.currentTimeMillis() - delay >= before) {
            handler.addEnemy(new Bullet("MISSILE", x, y + height / 2 - height / 4, width / 2, height / 2, handler, true, Direction.LEFT));
            handler.addEnemy(new Bullet("MISSILE", x+width, y + height / 2 - height / 4, width / 2, height / 2, handler, true, Direction.RIGHT));
            before = System.currentTimeMillis();
        }
    }
    
    @Override
    public void die(){}

}
