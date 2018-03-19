package mario.rm.sprite.tiles.movement;

import java.util.LinkedList;
import static mario.rm.SuperMario.handler;
import mario.rm.sprite.Player;
import mario.rm.sprite.tiles.Tiles;
import mario.rm.utility.MoveAttrib;

/**
 *
 * @author LENOVO
 */
public class Disappear implements Azione {

    private int delay;
    private int delay2;
    private Tiles t;
    private long time;

    public Disappear(String script, Tiles t) {
        this.t = t;
        String[] parameter = script.split(" ");
        try {
            delay = Integer.parseInt(parameter[2]);
            delay2 = Integer.parseInt(parameter[3]);
            //System.out.println("");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        time = 0;
    }

    @Override
    public MoveAttrib tick(MoveAttrib att) {
        if (time == 0) {
            LinkedList<Player> player = handler.getPlayer();
            for (int i = 0; i < player.size(); i++) {
                if (t.getBounds().intersects(player.get(i).getBounds())) {
                    time = System.currentTimeMillis();
                }
            }
        } else {
            if (time + (delay * 1000) < System.currentTimeMillis() && t.getCollide()) {
                System.out.println("sdjkasnd");
                t.setCollide(false);
                att.getLast().increaseYBy(500);
                time = System.currentTimeMillis();
            }else if (time + (delay2 * 1000) < System.currentTimeMillis() && !t.getCollide()) {
                System.out.println("asdasd");
                t.setCollide(true);
                time = 0;
                att.getLast().increaseYBy(-500);
            }
        }
        return att;
    }

    @Override
    public int getVelX() {
        return 0;
    }

    @Override
    public int getVelY() {
        return 0;
    }

}
