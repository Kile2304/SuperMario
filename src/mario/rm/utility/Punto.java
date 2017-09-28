package mario.rm.utility;

/**
 *
 * @author LENOVO
 */
public class Punto {
    
    private int x;
    private int y;
    
    public Punto(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public boolean compare(Punto p){
        if(x == p.x && y == p.y){
            return true;
        }
        return false;
    }
    
}
