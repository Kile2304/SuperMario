package mario.rm.Menu.sprite_estractor.input;

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
    
    public void increaseXBy(int increase){
        x += increase;
    }
    public void increaseYBy(int increase){
        y += increase;
    }
    
    public boolean compare(Punto p){
        return x == p.x && y == p.y;
    }
    
    @Override
    public String toString(){
        return "X: "+x+" Y: "+y;
    }
    
    
}
