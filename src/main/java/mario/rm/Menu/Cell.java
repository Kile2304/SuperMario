package mario.rm.Menu;

import java.awt.image.BufferedImage;
import mario.rm.utility.RGB;

/**
 *
 * @author LENOVO
 */
public class Cell {

    protected String type;
    protected BufferedImage img;
    
    protected boolean collider;
    protected boolean terrain;
    
    protected String partTile;
    //protected Type unlockable;
    protected String script;

    public Cell(String id, BufferedImage img, String title) {
        this.type = id;
        this.img = img;
        collider = false;
        terrain = false;
        partTile = title;
        script = "";
    }

    public Cell() {
    }

    public BufferedImage getImg() {
        return img;
    }

    public String getType() {
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
    
    /*public void setUnlockable(Type type){
        unlockable = type;
    }
    
    public Type getUnlockable(){
        return unlockable;
    }*/
    
    public String getScript(){
        return script;
    }
    
    public void setScript(String script){
        this.script = script;
    }

    public String getPartTil() {
        return partTile;
    }
    
}
