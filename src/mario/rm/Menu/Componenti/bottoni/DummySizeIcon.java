package mario.rm.Menu.Componenti.bottoni;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Shape;
import javax.swing.Icon;

/**
 *
 * @author LENOVO
 */
public class DummySizeIcon implements Icon {
    private final Shape shape;
    protected DummySizeIcon(Shape s) {
        shape = s;
    }
    @Override public void paintIcon(Component c, Graphics g, int x, int y) { /* Empty icon */ }
    @Override public int getIconWidth() {
        return shape.getBounds().width;
    }
    @Override public int getIconHeight() {
        return shape.getBounds().height;
    }
}   