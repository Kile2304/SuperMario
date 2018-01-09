package mario.rm.sprite.enemy;

import mario.rm.handler.Handler;
import mario.rm.identifier.Direction;
import mario.rm.identifier.Type;

/**
 *
 * @author LENOVO
 */
public class Cannone extends Enemy {

    private Type bullet;

    private long before;

    private int delay;

    public Cannone(int x, int y, int width, int height, Handler handler, Type type, boolean canDie, Type type2) {
        super(x, y, width, height, handler, type, canDie);
        this.bullet = type2;
        delay = 2000;
        hurt = false;
    }

    @Override
    public void tick() {
        if (System.currentTimeMillis() - delay >= before) {
            handler.addEnemy(new Bullet(Type.MISSILE, x, y + height / 2 - height / 4, width / 2, height / 2, handler, true, Direction.LEFT));
            handler.addEnemy(new Bullet(Type.MISSILE, x+width, y + height / 2 - height / 4, width / 2, height / 2, handler, true, Direction.RIGHT));
            before = System.currentTimeMillis();
        }
    }

}
