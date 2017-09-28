package mario.rm.sprite.tiles;

import java.util.ArrayList;
import mario.rm.Animation.Cut;
import mario.rm.Animation.Tile;
import mario.rm.handler.Handler;
import mario.rm.identifier.Type;

/**
 *
 * @author LENOVO
 */
/**
 * 
 * @return NORMALE BLOCCO CHE NON FA NIENTE DI SPECIALE
 */
public class Solid extends Tiles {   //NORMALE TILES

    public Solid(int x, int y, int width, int height, Handler handler, Type type, ArrayList<Tile> anim, boolean collide, String part) {
        super(x, y, width, height, handler, type, anim, collide, part);
    }
    
    public Solid(int x, int y, int width, int height, Handler handler, Type type, boolean collide, String part) {
        super(x, y, width, height, handler, type, collide, part);
    }

    @Override
    public void tick() {
    }

    @Override
    public void unlockable() {}

}
