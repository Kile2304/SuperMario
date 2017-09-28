package mario.rm.sprite.enemy;

import java.util.ArrayList;
import mario.rm.Animation.Cut;
import mario.rm.handler.Handler;
import mario.rm.sprite.Player;
import mario.rm.identifier.Type;

/**
 *
 * @author LENOVO
 */
/**
 *
 * @return NEMICO CHE VOLE ED SEGUE IL PLAYER, SE IL PLAYER E' RIVOLTO VERSO IL
 * NEMICO, QUESTO SI IMMOBILIZZERA' FINCHE IL PLAYER NON SI GIRA
 */
public class Boo extends Enemy {

    /**
     *
     * @return SERVE PER SAPERE SE IL FANTASMINO E' RIVOLTO VERSO IL PLAYER
     */
    private static Player p;

    public Boo(int x, int y, int width, int height, Handler handler, Type type, boolean canDie) {
        super(x, y, width, height, handler, type, canDie);
    }

    public static void setPlayer(Player p) {
        Boo.p = p;
    }

    /**
     *
     * @return AGGIORNA LA POSIZIONE DEL FANTASMINO
     */
    @Override
    public void tick() {

        direzioneY = p.getY() - y; //IMPOSTO LA DIREZIONE IN CUI MUOVERSI (Y)
        if (direzioneY != 0) {
            direzioneY = direzioneY / Math.abs(direzioneY);   //MI CALCOLO LA DIREZIONE
        } else {
            direzioneY = 20;
        }   //FINE DIREZIONE (Y)

        if (p.getX() > x && direzione < 0 || p.getX() < x && direzione > 0) { //SE LO SALTO E LUI E' FERMO GLI DICO DI RINCORRERMI
            direzione = -direzione;
        }

        int f = -p.getDirezione() / Math.abs(p.getDirezione());
        if (f == direzione / Math.abs(direzione)) {
            direzione = 100 * f;
            velX = 0;
            velY = 0;
        } else {
            int dirX = p.getX() - x;

            if (dirX != 0) {
                dirX = dirX / Math.abs(dirX);
            } else {
                dirX = 20;
            }

            direzione = dirX;

            velX = Type.BOO.getVelX();
            velY = Type.BOO.getVelY();
        }

        if (direzione != 20) {
            x += velX * (direzione / Math.abs(direzione));
        }
        if (direzioneY != 20) {
            y += velY * (direzioneY / Math.abs(direzioneY));
        }

    }

}
