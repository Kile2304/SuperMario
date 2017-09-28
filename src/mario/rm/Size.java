package mario.rm;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;

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
