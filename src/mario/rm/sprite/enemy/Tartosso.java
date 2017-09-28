package mario.rm.sprite.enemy;

import mario.rm.handler.Handler;
import mario.rm.identifier.Type;

/**
 *
 * @author LENOVO
 */
/**
 *
 * @return NEMICO CHE NON PUO' MORIRE DEFINITIVAMENTE, MA PUO' TEMPORANEAMENTE
 * ESSERE NEUTRALIZZATO COLPENDOLO ALLA TESTA
 */
public class Tartosso extends Enemy {

    private boolean test;

    public Tartosso(int x, int y, int width, int height, Handler handler, Type type, boolean canDie) {
        super(x, y, width, height, handler, type, canDie);
        test = true;
    }

    /**
     *
     * @return SE VIENE COLPITO NON MUORE, MA FINCHE NON FINISCE L'ANIMAZIONE DI
     * MORTE E DI RESSUREZZIONE NON RITORNA FALSO
     */
    @Override
    public boolean isEndDie() {//NECCESSARIO RIFARE LA CLASSE ANIM COME LA CLASSE TILE (HASHMAP<>)
        /*if (isDie) {    //CONTINUA L'ANIMAZIONE DI MORTE
            if (animazione.isEndDie() && !test) {
                direzione = (direzione / Math.abs(direzione)) * 100;
                test = true;
                animazione.getImage(lastMove, lastDirection);
            } else if (up.getEnd() && test) {   //CONTINUA L'ANIMAZIONE DI RESSURREZZIONE
                velX = type.getVelX();
                direzione = (direzione / Math.abs(direzione)) * 1;
                isDie = false;
                up.nextNormal();
            }
        }*/
        return false;
    }

    /**
     *
     * @return QUANDO IL NEMICO VIENE COLPITO
     */
    @Override
    public void die() {
        if (!isDie) {
            test = false;
            direzione = (direzione / Math.abs(direzione)) * 10;
            isDie = true;
            velX = 0;
        }
    }

}
