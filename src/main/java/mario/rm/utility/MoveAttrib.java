package mario.rm.utility;

import mario.rm.identifier.Direction;

/**
 *
 * @author LENOVO
 */
public class MoveAttrib {
    
    private Punto first;
    private Punto last;

    private Direction[] dir;
    
    public MoveAttrib(Punto first){
        this.first = first;
    }

    public Punto getFirst() {
        return first;
    }

    public void setFirst(Punto first) {
        this.first = first;
    }

    public Punto getLast() {
        return last;
    }

    public void setLast(Punto last) {
        this.last = last;
    }

    public Direction[] getDir() {
        return dir;
    }

    public void setDir(Direction[] dir) {
        this.dir = dir;
    }
    public void setDir(Direction dir, int index) {
        this.dir[index] = dir;
    }
    
    
    
}
