package mario.rm.sprite.enemy;

import java.util.LinkedList;
import static mario.rm.SuperMario.adaptHeight;
import mario.rm.handler.Handler;
import mario.rm.sprite.Sprite;
import mario.rm.identifier.Type;
import mario.rm.sprite.tiles.Tiles;

/**
 *
 * @author LENOVO
 */
/**
 *
 * @return INIZIALIZZAZIONE DI UN NEMICO NORMALE CHE VA AVANTI E INDIETRO
 */
public class Enemy extends Sprite {

    boolean canDie; //SE PUO' MORIRE O NO

    //protected Animazione up;    //ANIMAZIONE

    protected int direzioneY;   //DIREZIONEY

    protected static final double STACCO = adaptHeight(0.17);

    protected boolean isDie;

    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param img
     * @param handler
     * @param canDie
     * @param type
     * @return (POSIZIONE X, POSIZIONE Y, ALTEZZA, LARGHEZZA, ELENCO DI
     * ANIMAZIONI DA CUI ESTRAPOLARSI LA SUA, QUELLO CHE GESTISCE TUTTO IL
     * LIVELLO, IL TIPO DEL NEMICO, SE PUO MORIRE)
     */
    public Enemy(int x, int y, int width, int height, Handler handler, Type type, boolean canDie) {
        super(x, y, width, height, handler, type, handler.getMemoria().getEnemy());
        velX = type.getVelX();  //VEDO A CHE VELOCITA' E' CONSENTITO ANDARE A QUEL TIPO DI NEMICO
        velY = type.getVelY();  //VEDO A CHE VELOCITA' E' CONSENTITO ANDARE A QUEL TIPO DI NEMICO

        isDie = false;  //IMPOSTO A NON MORTO

        this.canDie = canDie;
        direzione = -1;

        direzioneY = 1;

    }

    /**
     *
     * @param g
     * @return DISEGNA IL NEMICO
     */
   /* @Override
    public void render(Graphics g) {
        delay++;   //VARIABILE PER RALLENTARE LO SCORRIMENTO DELL'IMMAGINE
        if (delay >= 8) { //SE STA CAMMINANDO E IL FRAME E' ALMENO A 8

            switch (direzione) {
                case 1:
                    temp = walk.nextMirror();
                    break;
                case -1:
                    temp = walk.nextNormal();
                    break;
                case -10:
                    temp = die.nextNormal();
                    break;
                case 10:
                    temp = die.nextMirror();
                    break;
                case -100:
                    temp = up.nextNormal();
                    break;
                case 100:
                    temp = up.nextMirror();
                    break;
                default:
                    break;
            }
            delay = 0; //RESETTA LA VARIABILE CONT
        }
        g.drawImage(temp, x, y, width, height, null);
    }*/

    /**
     *
     * @return SEMPLICE NEMICO CHE QUANDO VIENE COLPITO, FA LA SUA ANIMAZIONE E
     * MUORE
     */
    @Override
    public void die() {
        if (!isDie) {
            direzione = (direzione / Math.abs(direzione)) * 10;
            isDie = true;
            velX = 0;
        } else {
            handler.removeEnemy(this);   //RIMUOVE TILE
        }
    }

    /**
     *
     * @return AGGIORNA LA POSIZIONE DEL NEMICO SEMPLICEMENTE CHE QUANDO VA A
     * SBATTERE CAMBIA DIREZIONE, E SE SOTTO DI LUI NON C'E' PAVIMENTO CADE
     */
    @Override
    public void tick() {
        if (!isDie) {
            x += velX * direzione;
            y += velY * direzioneY;

            falling = true;

            LinkedList<Tiles> tile = handler.getTiles();
            for (int i = 0; i < handler.getTiles().size(); i++) {
                if (getBounds().intersects(tile.get(i).getBounds())) {
                    if (tile.get(i).getType() != Type.MUSHROOM || tile.get(i).getType() != Type.COIN) {
                        if (getBoundsBottom().intersects(tile.get(i).getBounds())) { //INTERSEZIONE PARTE BASSA
                            y = tile.get(i).getY() - height + 1;//LA SUA POSIZIONE DIVIENE POSIZIONE TILE (Y) MENO L'ALTEZZA

                            if (!jumping) {
                                falling = false;
                                gravity = 0;
                                velY = 0;
                            }
                        }
                        if (getBoundsRight().intersects(tile.get(i).getBounds())) {  //INTERSEZIONE PARTE DESTRA
                            x = tile.get(i).getX() - width; //LA POSIZIONE IN X DIVENTA LA X DEL TILE MENO LA LARGHEZZA
                            direzione *= -1;
                        }
                        if (getBoundsLeft().intersects(tile.get(i).getBounds())) {   //INTERSEZIONE PARTE SINISTRA (DOVREBBE ESSERE PERFETTO)
                            x = tile.get(i).getX() + tile.get(i).getWidth() - 20; //LA POSIZIONE IN X DIVENTA LA X DEL TILE MENO LA LARGHEZZA del tile
                            direzione *= -1;
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
        }
    }

    /**
     *
     * @return RITORNA SE IL NEMICO PUO' ESSERE COLPITO E QUINDI MORIRE
     */
    public boolean isCanDie() {
        return canDie;
    }

    /**
     *
     * @param direzioneX
     * @param direzioneY
     * @return IMPOSTA LE DIREZIONI X E Y
     */
    public void setDirezione(int direzioneX, int direzioneY) {
        this.direzione = direzioneX;
        this.direzioneY = direzioneY;
    }

    @Override
    public void setTeleport() {
        boolean teleport = true;
    }

    /**
     *
     * @return RITORNA SE E' MORTO O NO, SE E' MORTO NON POTRA' ESSERE COLPITO,
     * O COLPIRE
     */
    public boolean isDie() {
        return isDie;
    }
}