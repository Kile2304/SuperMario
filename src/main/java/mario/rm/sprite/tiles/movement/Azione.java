package mario.rm.sprite.tiles.movement;

import mario.rm.utility.MoveAttrib;
import mario.rm.utility.Punto;

/**
 *
 * @author mantini.christian
 */
public interface Azione {
    
    MoveAttrib tick(MoveAttrib att);
    
    int getVelX();
    
    int getVelY();
}
