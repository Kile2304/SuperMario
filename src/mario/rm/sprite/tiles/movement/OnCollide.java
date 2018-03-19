package mario.rm.sprite.tiles.movement;

import java.util.LinkedList;
import static mario.rm.SuperMario.handler;
import mario.rm.identifier.Direction;
import mario.rm.sprite.tiles.Tiles;
import mario.rm.utility.MoveAttrib;

/**
 *
 * @author LENOVO
 */
public class OnCollide implements Azione {

    private int velX;
    private int velY;
    
    private Tiles t;
    
    public OnCollide(String script, Tiles t) {
        this.t = t;
        String[] parameter = script.split(" ");
        try {
            velX = Integer.parseInt(parameter[2]);
            velY = Integer.parseInt(parameter[3]);
            //System.out.println("");
            //velY = 1;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }
    
    @Override
    public MoveAttrib tick(MoveAttrib att) {
        //System.out.println("adsadas");
        LinkedList<Tiles> tile = handler.getTiles();
        Direction orizontal = att.getDir()[0];
        Direction vertical = att.getDir()[1];
        for (int i = 0; i < tile.size(); i++) {
            if(t != tile.get(i)){
                if(t.getBoundsLeft().intersects(tile.get(i).getBounds())){
                    orizontal = Direction.RIGHT;
                } else if(t.getBoundsRight().intersects(tile.get(i).getBounds())){
                    orizontal = Direction.LEFT;
                } else if(t.getBoundsTop().intersects(tile.get(i).getBounds())){
                    vertical = Direction.RIGHT;
                } else if(t.getBoundsBottom().intersects(tile.get(i).getBounds())){
                    vertical = Direction.LEFT;
                }
            }
        }
        att.getLast().increaseXBy(orizontal == Direction.LEFT ? -velX : velX);
        att.getLast().increaseYBy(vertical == Direction.LEFT ? -velY : velY);
        att.setDir(new Direction[]{orizontal, vertical});
        
        return att;
    }

    @Override
    public int getVelX() {
        return velX;
    }

    @Override
    public int getVelY() {
        return velY;
    }
    
}
