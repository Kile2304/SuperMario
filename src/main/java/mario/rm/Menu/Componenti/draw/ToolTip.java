package mario.rm.Menu.Componenti.draw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import mario.rm.utility.Punto;

/**
 *
 * @author LENOVO
 */
public class ToolTip {
    
    private static final Font f = new Font("arial", Font.BOLD, 15);
    
    public static final void draw(Graphics g, Punto p, int pixel, String script){
        int x = p.getX() * pixel;
        int y = p.getY() * pixel;
        
        
        g.setColor(new Color(189, 189, 189, 50));
        g.fillRoundRect(x - pixel, y - pixel / 2, pixel * 3, pixel * 2, pixel, pixel);
        
        g.setColor(Color.BLACK);
        g.drawRoundRect(x - pixel, y - pixel / 2, pixel * 3, pixel * 2, pixel, pixel);

        g.setColor(Color.red);
        g.drawString(script, x - pixel / 2, y + pixel / 2);
    }
    
}
