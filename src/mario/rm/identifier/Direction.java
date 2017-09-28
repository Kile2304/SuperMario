package mario.rm.identifier;

import java.io.Serializable;

/**
 *
 * @author LENOVO
 */
public enum Direction implements Serializable{
 
    UP(0, "UP"),
    DOWN(1, "DOWN"),
    LEFT(2, "LEFT"),
    RIGHT(3, "RIGHT");
    
    private int moltiplier;
    private String name;
    
    Direction(int moltiplier, String name){
        this.moltiplier = moltiplier;
        this.name = name;
    }
    
    public int getMoltiplier(){
        return moltiplier;
    }
    
    public String getName(){
        return name;
    }
    
}
