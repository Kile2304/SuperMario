package mario.rm.sprite.tiles;

import java.util.ArrayList;
import mario.rm.Animation.Cut;
import mario.rm.Animation.Tile;
import mario.rm.handler.Handler;

/**
 *
 * @author LENOVO
 */
/**
 * 
 * @return NORMALE BLOCCO CHE NON FA NIENTE DI SPECIALE
 */
public class Solid extends Tiles {   //NORMALE TILES

    public Solid(int x, int y, int width, int height, Handler handler, String type, ArrayList<Tile> anim, boolean collide, String part, boolean damage, String script) {
        super(x, y, width, height, handler, type, anim, collide, part, damage, script);
    }
    
    public Solid(int x, int y, int width, int height, Handler handler, String type, boolean collide, String part, String script) {
        super(x, y, width, height, handler, type, collide, part, script);
    }

    @Override
    public void unlockable() {}

}
