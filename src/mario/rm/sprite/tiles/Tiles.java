package mario.rm.sprite.tiles;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import mario.rm.Animation.Tile;
import mario.rm.utility.Size;
import mario.rm.SuperMario;
import mario.rm.handler.Handler;
import mario.rm.identifier.TilePart;
import mario.rm.identifier.Type;

/**
 *
 * @author LENOVO
 */
/**
 *
 * @return CLASSE BASE PER LA GESTIONE DEGLI TILES ES: PAVIMENTO, BLOCCHI,
 * ITEM... NE PERMETTE IL DISEGNO SULLO SCHERMO, E L'EVENTUALE MOVIMENTO O
 * AZIONE SPECIALE
 */
public abstract class Tiles implements Size {    //sarebbe meglio astraatta per estensione ad altre classi

    protected int x;  //COORDINATA IN CUI SI TROVA(X)
    protected int y;  //COORDINATA IN CUI SI TROVA(Y)

    protected int mX; //NUMERO DI IMMAGINE DA MOSTRARE NELL'ASSE Y

    protected int width;    //LARGHEZZA
    protected int height;   //ALTEZZA

    protected int velX;   //VELOCITA CON CUI SI MUOVE IL PLAYER NELL'ASSE X
    protected int velY; //VELOCITA CON CUI SI MUOVE IL PLAYER NELL'ASSE Y

    protected Handler handler;  //INSIEME DI PLAYER E TILES

    protected double gravity;   //GRAVITA

    protected Type type;    //TIPO DI SPRITE

    protected BufferedImage[] temp;

    protected int delay;

    private long before;

    private final int stop;

    private final int isDelay;

    private final int numImma;

    protected int numSerieX;

    protected boolean breakable;

    protected boolean collide;

    protected String partTile;

    protected boolean damage;

    public Tiles(int x, int y, int width, int height, Handler handler, Type type, ArrayList<Tile> anim, boolean collide, String part, boolean damage) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.handler = handler;
        this.type = type;
        stop = type.getTempo();
        isDelay = type.getDelay();
        this.partTile = part;

        this.damage = damage;
        //System.out.println(""+type.name());
        if (type != Type.VOID) {
            anim.stream().filter((tile) -> (tile.getType() == type)).forEach((tile) -> {
                this.temp = tile.getImage(TilePart.valueOf(part));
            });
        }

        numImma = temp != null ? temp.length : 0;

        this.numSerieX = 1;
        this.collide = collide;
    }

    public Tiles(int x, int y, int width, int height, Handler handler, Type type, boolean collide, String part) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.handler = handler;
        this.type = type;
        stop = type.getTempo();
        isDelay = type.getDelay();
        //numImma = type.getNumeroImma();
        numImma = temp != null ? temp.length : 0;
        this.numSerieX = 1;
        this.collide = collide;
        this.partTile = part;
    }

    public void die() {
        handler.removeTile(this);   //RIMUOVE TILE
    }

    public void moreSerie() {
        numSerieX++;
    }

    public boolean canDamage() {
        return damage;
    }

    public abstract void unlockable();

    public void render(Graphics g) {
        if (temp != null) {
            if (System.currentTimeMillis() - before >= stop && type != Type.VOID) {
                delay++;
                if (delay >= isDelay) { //SE STA CAMMINANDO E IL FRAME E' ALMENO A 3
                    mX++;   //CAMBIA L'IMMAGINE DA VISUALIZZARE
                    if (mX >= numImma) //SE ARRIVA ALL' ULTIMA IMMAGINE 
                    {
                        mX = 0; //RESETTA
                        before = System.currentTimeMillis();
                    }
                    delay = 0; //RESETTA LA VARIABILE CONT
                }
            }
            for (int i = 0; i < numSerieX; i++) {
                g.drawImage(temp[mX], x + (width * i), y, width, height, null);   //DISEGNO L'IMMAGINE
            }
        }

    }

    public Rectangle getBounds() {   //RITORNA L'AREA OCCUPANTE LO SPRITE SUL PIANO
        return new Rectangle(x, y, (width * numSerieX) - SuperMario.adaptWidth(20), height);  //POSIZIONE X, Y, LARGHEZZA, ALTEZZA. DA UTILIZZARE PER COLLIDER
    }

    public abstract void tick();

    public void setWidth(int width) {
        this.width = width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Type getType() {
        return type;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public int getmX() {
        return mX;
    }

    public void setmX(int mX) {
        this.mX = mX;
    }

    public int getSerie() {
        return numSerieX;
    }

    public boolean getCollide() {
        return collide;
    }

    public String getTile() {
        return partTile;
    }

}
