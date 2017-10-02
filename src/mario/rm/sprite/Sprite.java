package mario.rm.sprite;

import mario.rm.identifier.Type;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import mario.rm.Size;
import mario.rm.SuperMario;
import mario.rm.handler.Handler;
import mario.rm.Animation.Anim;
import mario.rm.Animation.Tile;
import mario.rm.identifier.Direction;
import mario.rm.identifier.Move;
import mario.rm.identifier.TilePart;

/**
 *
 * @author LENOVO
 */
/**
 *
 * @return CLASSE BASE PER LA GESTIONE DI SPRITE COMPLESSI PER IL SUO MOVIMENTO
 * E IL DISEGNO SULLO SCHERMO
 */
public abstract class Sprite implements Size {  //DA FARE ASSOLUTAMENTE COLLIDER

    protected int x;  //COORDINATA IN CUI SI TROVA(X)
    protected int y;  //COORDINATA IN CUI SI TROVA(Y)

    protected BufferedImage temp;  //IMMAGINE RITAGLIATA

    protected int width;    //LARGHEZZA
    protected int height;   //ALTEZZA

    protected int velX;   //VELOCITA CON CUI SI MUOVE IL PLAYER NELL'ASSE X
    protected int velY; //VELOCITA CON CUI SI MUOVE IL PLAYER NELL'ASSE Y

    protected Handler handler;  //CLASSE CHE GESTISCE PLAYER E TILES

    protected double gravity;   //GRAVITA
    protected boolean falling;  //CADUERE
    protected boolean jumping;  //SALTARE
    protected boolean walking;  //CAMMINARE

    protected Type type;    //TIPO DI SPRIT

    protected int direzione;    //USATO PER INDICARE L'IMMAGINE DA DISEGNARE E LA DIREZONE IN CUI SI MUOVE

    protected Anim animazione;

    protected Move lastMove;
    protected Direction lastDirection;

    /*protected BufferedImage[] ti;
    int ind;
    int delay2;*/
    
    public Sprite(int x, int y, int width, int height, Handler handler, Type type, ArrayList<Anim> elenco) {  //NORMALE INIZIALIZZAZIONE CON IL COSTRUTTORE
        this.x = x; //INIZIALIZZA LA POSIZIONE NELLE COORDINATE X
        this.y = y; //INIZIALIZZA LA POSIZIONE NELLE COORDINATE Y
        this.width = width; //LARGHEZZA
        this.height = height;   //ALTEZZA
        this.handler = handler; //INSIEME DI TILE E PLAYER
        falling = true;    //CADUTA
        jumping = false;    //SALTO
        gravity = 0.0;  //GRAVITA
        walking = false;    //MOVIMENTO
        this.type = type;   //CHE TIPO DI SPRITE E'
        lastMove = Move.STAND;
        lastDirection = Direction.RIGHT;

        for (Iterator<Anim> it = elenco.iterator(); it.hasNext();) {
            Anim animazione = it.next();
            if (animazione.getType() == type) {
                this.animazione = animazione;   //DA CAMBIARE (PROBABILMENTE COSTRUTTORE CHE CREA COPIA)
            }
        }

        /*handler.getMemoria().getTiles().stream().filter((t) -> (t.getType() == Type.KI)).forEach((t) -> {
            ti = t.getImage(TilePart.UPLEFT);
        });*/
    }

    /**
     *
     * @param direzioneX
     * @return IMPOSTA LA DIREZIONE CHE IN QUESTO CASO INDICA ANCHE IL TIPO DI
     * SPRITE DA DISEGNARE
     */
    public void setDirezione(int direzioneX) {
        direzione = direzioneX;
    }

    /**
     *
     * @return RITORNA L'ATTUALE DIREZIONE
     */
    public int getDirezione() {
        return direzione;
    }

    /**
     *
     * @return NEL CASO LO SPRITE AVESSE UN ANIMAZIONE DI MORTE O RISALITA,
     * QUESTA FUNZIONE RITORNA VERO QUANDO L'ANIMAZIONE E' FINITA
     */
    public boolean isEndDie() {
        return animazione.isEndDie();
    }

    /**
     *
     * @param g
     * @return DISEGNA LO SPRITE CORRENTE IN BASE AL MOVIMENTO CHE STA FACENDO,
     * ES: STAZIONAMENTO, CAMMINA, CORRE...
     */
    public void render(Graphics g) {//DA MODIFICARE, SOPRATTUTTO IL VALORE COSTANTE
        temp = animazione.getImage(lastMove, lastDirection);
        
        //g.drawImage(ti[ind], x - width / 2, y - height, width * 2, height * 2, null);   //DISEGNO L'IMMAGINE
        g.drawImage(temp, x, y, width, height, null);   //DISEGNO L'IMMAGINE
        /*if (delay2 > ti.length * 2) {
            if (ind < ti.length - 1) {
                ind++;
            } else {
                ind = 0;
            }
            delay2 = 0;
        } else {
            delay2++;
        }*/
    }

    public void setLastMovement(Direction dir, Move move) {
        lastDirection = dir;
        lastMove = move;
    }

    public Direction getLastDirection() {
        return lastDirection;
    }

    /**
     *
     * @return ELIMINAZIONE DELLO SPRITE
     */
    public abstract void die(); //ELIMINAZIONE DALL'ELENCO DELLO SPRITE

    /**
     *
     * @return AGGIORNA LA POSIZIONE DEL PLAYER, COMPRESE LE COLLISIONI.
     */
    public abstract void tick();    //AGGIORNAMENTO DELLO SPRITE

    /**
     *
     * @return TELETRASPORTA LO SPRITE (USATO PER SIMULARE I TUBI)
     */
    public abstract void setTeleport();    //AGGIORNAMENTO DELLO SPRITE

    /**
     *
     * @return RITORNA L'AREA OCCUPANTE TOTALE DALLO SPRITE
     */
    public Rectangle getBounds() {   //RITORNA L'AREA OCCUPANTE TOTALE DALLO SPRITE
        return new Rectangle(x, y, width, height);  //POSIZIONE X, Y, LARGHEZZA, ALTEZZA. DA UTILIZZARE PER COLLIDER
    }

    /**
     *
     * @return RITORNA L'AREA OCCUPANTE DALLA PARTE ALTA DELLO SPRITE
     */
    public Rectangle getBoundsTop() {   //RITORNA L'AREA OCCUPANTE DALLA PARTE ALTA DELLO SPRITE
        return new Rectangle(x + SuperMario.adaptWidth(10), y, width - SuperMario.adaptWidth(20), SuperMario.adaptHeight(5));  //POSIZIONE X, Y, LARGHEZZA, ALTEZZA. DA UTILIZZARE PER COLLIDER
    }

    /**
     *
     * @return RITORNA L'AREA OCCUPANTE DALLA PARTE BASSA DELLO SPRITE
     */
    public Rectangle getBoundsBottom() {   //RITORNA L'AREA OCCUPANTE DALLA PARTE BASSA DELLO SPRITE
        return new Rectangle(x + SuperMario.adaptWidth(10), y + height - SuperMario.adaptHeight(5), width - SuperMario.adaptWidth(20), SuperMario.adaptHeight(5));  //POSIZIONE X, Y, LARGHEZZA, ALTEZZA. DA UTILIZZARE PER COLLIDER
    }

    /**
     *
     * @return RITORNA L'AREA OCCUPANTE DALLA PARTE SINISTRA DELLO SPRITE
     */
    public Rectangle getBoundsLeft() {   //RITORNA L'AREA OCCUPANTE DALLA PARTE SINISTRA DELLO SPRITE
        return new Rectangle(x, y + SuperMario.adaptHeight(10), SuperMario.adaptWidth(5), height - SuperMario.adaptHeight(20));  //POSIZIONE X, Y, LARGHEZZA, ALTEZZA. DA UTILIZZARE PER COLLIDER (DA MODIFICARE)
    }

    /**
     *
     * @return RITORNA L'AREA OCCUPANTE DALLA PARTE DESTRA DELLO SPRITE
     */
    public Rectangle getBoundsRight() {   //RITORNA L'AREA OCCUPANTE DALLA PARTE DESTRA DELLO SPRITE
        return new Rectangle(x + width - SuperMario.adaptWidth(10), y + SuperMario.adaptHeight(10), SuperMario.adaptWidth(5), height - SuperMario.adaptHeight(20));  //POSIZIONE X, Y, LARGHEZZA, ALTEZZA. DA UTILIZZARE PER COLLIDER
    }

    /**
     *
     * @return FINE RITORNA LA POSIZIONE X DELLO SPRITE
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return FINE RITORNA LA POSIZIONE Y DELLO SPRITE
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @return RITORNA L'ALTEZZA IN PIXEL DELLO SPRITE NELLO SCHERMO
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @return INDICA SE LO SPRITE STA CADENDO
     */
    public boolean isFalling() {
        return falling;
    }

    /**
     *
     * @return INDICA SE LO SPRITE STA SALTANDO
     */
    public boolean isJumping() {
        return jumping;
    }

    /**
     *
     * @return INDICA SE LO SPRITA STA CAMMINANDO
     */
    public boolean isWalking() {
        return walking;
    }

    /**
     *
     * @return RITORNA DI CHE TIPO E' LO SPRITE, ES: PLAYER, SOLID, TARTOSSO...
     */
    public Type getType() {
        return type;
    }

    /**
     *
     * @param velX
     * @return IMPOSTA LA VELOCITA' SULL'ASSE X
     */
    public void setVelX(int velX) {
        this.velX = velX;
    }

    /**
     *
     * @param velY
     * @return IMPOSTA LA VELOCITA' SULL'ASSE Y
     */
    public void setVelY(int velY) {
        this.velY = velY;
    }

    /**
     *
     * @param gravity
     * @return IMPOSTA LA GRAVITA' (USATO PER IL SALTO E LA CADUTA)
     */
    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    /**
     *
     * @return RITORNA IL VALORE DELLA GRAVITA'
     */
    public double getGravity() {
        return gravity;
    }

    /**
     *
     * @param falling
     * @return IMPOSTA SE STA CADENDO O NO
     */
    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    /**
     *
     * @param jumping
     * @return IMPOSTA SE STA SALTANDO O NO
     */
    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    /**
     *
     * @param walking
     * @return IMPOSTA SE STA CAMMINANDO O NO
     */
    public void setWalking(boolean walking) {
        this.walking = walking;
    }

}
