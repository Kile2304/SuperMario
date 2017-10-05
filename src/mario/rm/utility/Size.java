package mario.rm.utility;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import mario.rm.SuperMario;

/**
 *
 * @author LENOVO
 */
public interface Size {

    static final int standardWidth = SuperMario.adaptWidth(50);
    static final int standardHeight = SuperMario.adaptHeight(50);
    
    static final int crescita = SuperMario.adaptHeight(15);

    static final int timer = 2000;
    
}
