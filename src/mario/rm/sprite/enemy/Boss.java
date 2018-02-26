package mario.rm.sprite.enemy;

import java.util.ArrayList;
import java.util.LinkedList;
import mario.rm.Animation.Cut;
import mario.rm.handler.Handler;
import mario.rm.sprite.Player;
import mario.rm.sprite.tiles.Tiles;

/**
 *
 * @author LENOVO
 */
public class Boss extends Enemy {

    private static Player p;
    private long time;
    private long time2;

    private int delayWalk = 4000;

    private boolean force;

    private int life;

    public Boss(int x, int y, int width, int height, Handler handler, String type, boolean canDie) {
        super(x, y, width, height, handler, type, canDie);
        time = System.currentTimeMillis();
        force = false;
        life = 3;
        canDie = false;
    }

    public static void setPlayer(Player p) {
        Boss.p = p;
    }

    @Override
    public void die() {
        if (life == 0) {
            handler.removeEnemy(this);   //RIMUOVE TILE
        } else if (!isDie) {
            life--;
            isDie = true;
            canDie = false;
            long t = System.currentTimeMillis();
            new Thread() {
                @Override
                public void run() {
                    boolean immortale = true;
                    while (immortale) {
                        if (System.currentTimeMillis() - t > 1500) {
                            immortale = false;
                            isDie = false;
                        }
                    }
                }
            }.start();
        }
    }

    @Override
    public void tick() {
        y += velY * direzioneY;

        falling = true;

        LinkedList<Tiles> tile = handler.getTiles();
        for (int i = 0; i < handler.getTiles().size(); i++) {
            if (getBounds().intersects(tile.get(i).getBounds())) {
                if (tile.get(i).getType().equals("MUSHROOM") || tile.get(i).getType().equals("COIN")) {
                    if (getBoundsBottom().intersects(tile.get(i).getBounds())) { //INTERSEZIONE PARTE BASSA
                        y = tile.get(i).getY() - height + 1;//LA SUA POSIZIONE DIVIENE POSIZIONE TILE (Y) MENO L'ALTEZZA

                        if (!jumping) {
                            falling = false;
                            gravity = 0;
                            velY = 0;
                        }
                    }
                }
            }
        }
        if (falling) {    //SE STA CADENDO
            direzioneY = -1;
            gravity -= STACCO; //AUMENTA LA GRAVITA
            velY = ((int) gravity); //LA VELOCITA E PARI ALLA GRAVITA
        } else {
            direzioneY = 1;
        }

        if (System.currentTimeMillis() - time < delayWalk) {
            if (p.getX() >= x) {
                direzione = 1;
            } else {
                direzione = -1;
            }
            x += (velX * direzione);
            time2 = System.currentTimeMillis();
        } else if (!force) {
            if (!isDie) {
                canDie = true;
            }
            if (System.currentTimeMillis() - time2 > 3000 && !force) {
                velX *= 3;
                delayWalk /= 2;
                force = true;
                time = System.currentTimeMillis();
            }
        } else {
            force = false;
            delayWalk *= 2;
            velX /= 3;
            time = System.currentTimeMillis();
            canDie = false;
        }
    }

}
