package mario.rm.sprite.enemy;

import java.util.ArrayList;
import mario.rm.Animation.Anim;
import mario.rm.handler.Handler;
import mario.rm.identifier.Direction;

/**
 *
 * @author LENOVO
 */
public class Bullet extends Enemy{
    
    public static ArrayList<Anim> bullet;
    
    public Bullet(String type, int x, int y, int width, int height, Handler handler, boolean canDie, Direction dir){
        super(x, y, width, height, handler, type, canDie, null);
        if(dir == Direction.LEFT){
            x -= width;
        }
        bullet.stream().filter((anim) -> (anim.getType().equals(type))).forEach((anim) -> {
            this.animazione = anim;
        });
        this.lastDirection = dir;
        actualDirection = dir;
        
        velX = lastDirection == Direction.RIGHT ? velX : -velX;
    }
    
    @Override
    public void die(){
        handler.removeEnemy(this);
    }
    
    @Override
    public void tick(){
        x += velX;
    }
    
}
