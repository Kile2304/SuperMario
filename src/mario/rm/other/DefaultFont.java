package mario.rm.other;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author LENOVO
 */
public enum DefaultFont {
    

    STANDARD(new Font("Arial", Font.PLAIN, 10), Color.WHITE),
    ERROR(new Font("Arial", Font.BOLD, 15), Color.RED),
    DEBUG(new Font("Arial", Font.BOLD, 15), Color.BLUE),
    COMMAND(new Font("Arial", Font.ITALIC, 15), Color.GREEN),
    INFORMATION(new Font("Arial", Font.PLAIN, 15), Color.ORANGE);

    Font font;
    Color color;

    DefaultFont(Font font, Color color){
        this.font = font;
        this.color = color;
    }
    
    public Font getFont(){
        return font;
    }
    
    public Color getColor(){
        return color;
    }
    
}
