package mario.rm.utility;

/**
 *
 * @author LENOVO
 */
public class RGB {

    private int red;
    private int green;
    private int blue;

    public RGB() {
        red = 0;
        blue = 0;
        green = 0;
    }

    public RGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void setColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    
    public void setColor(RGB r) {
        red = r.red;
        green = r.green;
        blue = r.blue;
    }

    public boolean compareTo(RGB find) {
        if (red != find.red) {
            return false;
        } else if (green != find.green) {
            return false;
        } else {
            return blue == find.blue;
        }
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
    
    @Override
    public String toString(){
        String s = "";
        s += "rosso: "+red;
        s +=" verde: "+green;
        s += " blue: "+blue;
        
        return s;
    }

}
