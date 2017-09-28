package mario.rm.Menu;

import java.awt.image.BufferedImage;
import mario.rm.identifier.Type;
import mario.rm.utility.RGB;

/**
 *
 * @author LENOVO
 */
public class Cell {

    protected Type type;
    protected BufferedImage img;
    
    protected boolean collider;
    protected boolean terrain;
    
    protected String partTile;
    protected Type unlockable;

    public Cell(Type id, BufferedImage img, String title) {
        this.type = id;
        this.img = img;
        collider = false;
        terrain = false;
        partTile = title;
    }

    public Cell() {
    }

    public BufferedImage getImg() {
        return img;
    }

    public Type getType() {
        return type;
    }

    public boolean getCollider() {
        return collider;
    }

    public void changeCollider() {
        collider = Boolean.logicalXor(collider, true);
    }

    public void setTerrain(boolean ter) {
        this.terrain = ter;
    }

    public boolean getTerrain() {
        return terrain;
    }
    
    public void setUnlockable(Type type){
        unlockable = type;
    }
    
    public Type getUnlockable(){
        return unlockable;
    }

    public String getPartTil() {
        return partTile;
    }
    
}
