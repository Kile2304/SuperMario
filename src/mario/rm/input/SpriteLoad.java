package mario.rm.input;

/**
 *
 * @author LENOVO
 */
public class SpriteLoad {
    
    private int x;
    private int y;
    private String type;
    private String partTile;
    
    private String unlockableType;
    private int unlockableQuantity;
    
    private String movement;

    public SpriteLoad(int x, int y, String type, String partTile, String unlockableType, int unlockableQuantity, String movement) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.partTile = partTile;
        this.unlockableType = unlockableType;
        this.unlockableQuantity = unlockableQuantity;
        this.movement = movement;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType() {
        return type;
    }

    public String getPartTile() {
        return partTile;
    }

    public String getUnlockableType() {
        return unlockableType;
    }

    public int getUnlockableQuantity() {
        return unlockableQuantity;
    }

    public String getMovement() {
        return movement;
    }
    
    
    
}
