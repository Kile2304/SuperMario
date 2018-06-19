package mario.rm.utility;

/**
 *
 * @author LENOVO
 */
public class Punto {
    
    public int x;
    public int y;
    
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
    
    public void increaseXBy(int increase){
        x += increase;
    }
    public void increaseYBy(int increase){
        y += increase;
    }
    
    public boolean compare(Punto p){
        return x == p.x && y == p.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
    
    @Override
    public String toString(){
        return "X: "+x+" Y: "+y;
    }
    
    
}
