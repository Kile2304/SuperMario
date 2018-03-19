package mario.rm.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import mario.MainComponent;
import mario.rm.utility.Size;
import mario.rm.SuperMario;
import mario.rm.handler.Handler;
import mario.rm.Animation.Anim;
import mario.rm.Animation.MultiAnim;
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

    protected String type;    //TIPO DI SPRIT

    protected int direzione;    //USATO PER INDICARE L'IMMAGINE DA DISEGNARE E LA DIREZONE IN CUI SI MUOVE

    protected Anim animazione;

    protected Move lastMove;
    protected Direction lastDirection;

    protected Move actualMove;
    protected Direction actualDirection;

    protected MultiAnim ma;

    private BufferedImage[] aura;

    int tempIndex;

    public Sprite(int x, int y, int width, int height, Handler handler, String type, ArrayList<Anim> elenco) {  //NORMALE INIZIALIZZAZIONE CON IL COSTRUTTORE
        System.gc();
        System.out.println("after: "+MainComponent.memoryUsed());
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
        actualMove = Move.STAND;
        actualDirection = Direction.RIGHT;

        if (handler != null) {
            for (Iterator<Anim> it = elenco.iterator(); it.hasNext();) {
                Anim animazione = it.next();
                if (animazione.getType().equals(type)) {
                    this.animazione = animazione;   //DA CAMBIARE (PROBABILMENTE COSTRUTTORE CHE CREA COPIA)
                }
            }
        }
        if (handler != null) {
            for (Iterator<Tile> it = handler.getMemoria().getUnlockable().iterator(); it.hasNext();) {
                Tile animazione = it.next();
                if (animazione.getType().equals("AURA")) {
                    aura = animazione.getImage(TilePart.UPLEFT);
                }
            }
        }
        tempIndex = -1;

        ma = new MultiAnim(null, 0, 0);

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
        if (actualMove == Move.DIE || actualMove == Move.RUN) {
            return animazione.isEndDie(actualMove, ma.getIndex());
        }
        return false;
    }

    long time;

    /**
     *
     * @param g
     */
    public void render(Graphics g) {//DA MODIFICARE
        /*try {
            sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(SuperMario.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        if (animazione != null) {
            MultiAnim t = animazione.getImage(actualMove, actualDirection, lastMove, lastDirection, ma);
            if (t != null) {
                ma.initialize(t);
            }
            //System.out.println("Get: "+type+" "+ma.getIndex()+" delay: "+ma.getDelay());
            temp = ma.getImg();
            lastMove = actualMove;
            lastDirection = actualDirection;

            if (temp != null) {
                g.drawImage(temp, x, y, width, height, null);   //DISEGNO L'IMMAGINE
            }
            
            t = null;
        }
        /*g.setColor(Color.WHITE);
        g.drawRect(getBoundsTop().x, getBoundsTop().y, getBoundsTop().width, getBoundsTop().height);
        g.setColor(Color.red);
        g.drawRect(getBoundsBottom().x, getBoundsBottom().y, getBoundsBottom().width, getBoundsBottom().height);
        g.setColor(Color.BLACK);
        g.drawRect(getBoundsLeft().x, getBoundsLeft().y, getBoundsLeft().width, getBoundsLeft().height);
        g.setColor(Color.GREEN);
        g.drawRect(getBoundsRight().x, getBoundsRight().y, getBoundsRight().width, getBoundsRight().height);*/
    }

    public void setLastMovement(Direction dir, Move move) {
        actualDirection = dir;
        actualMove = move;
    }

    public Direction getLastDirection() {
        return actualDirection;
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

    public Rectangle getBounds(int x, int y) {   //RITORNA L'AREA OCCUPANTE TOTALE DALLO SPRITE
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
     * @return RITORNA LA LARGHEZZA IN PIXEL DELLO SPRITE NELLO SCHERMO
     */
    public int getWidth() {
        return width;
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
    public String getType() {
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
