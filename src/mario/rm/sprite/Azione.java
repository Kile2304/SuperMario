package mario.rm.sprite;

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
