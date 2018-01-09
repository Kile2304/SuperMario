package mario.rm.sprite.enemy;

import mario.rm.handler.Handler;
import mario.rm.identifier.Direction;
import mario.rm.identifier.Move;
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
        velY = Type.BOO.getVelY();
        velX = Type.BOO.getVelX();
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
        direzioneY = direzioneY != 0 ? (direzioneY > 0 ? 1 : -1) : 0;   //CALCOLO DIREZIONE Y

        int direzioneX = p.getX() - x;  //CALCOLO VERSO DOVE DEVE MUOVERSI IL BOO
        direzioneX = direzioneX != 0 ? (direzioneX > 0 ? 1 : -1) : 0;   //CALCOLO DIREZIONE X

        actualDirection = direzioneX != 0 ? (direzioneX > 0 ? Direction.RIGHT : Direction.LEFT) : lastDirection;  //CALCOLO LA DIREZIONE ATTUALE DEL BOO

        if (p.getLastDirection() != actualDirection && direzioneX < 0 || p.getLastDirection() != actualDirection && direzioneX > 0) {   //SE IL PLAYER E' RIVOLTO VERSO IL BOO GLI DICO DI FERMARSI
            actualMove = Move.STAND;
        } else {    //ALTRIMENTI SI MUOVE
            actualMove = Move.WALK;

            x += velX * direzioneX;
            y += velY * direzioneY;
        }

    }

}
