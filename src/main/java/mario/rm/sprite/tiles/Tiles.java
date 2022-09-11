package mario.rm.sprite.tiles;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mario.rm.Animation.Tile;
import mario.rm.utility.Size;
import mario.rm.SuperMario;
import mario.rm.handler.Handler;
import mario.rm.identifier.Direction;
import mario.rm.identifier.TilePart;
import mario.rm.identifier.Tipologia;
import mario.rm.sprite.tiles.movement.Azione;
import mario.rm.sprite.tiles.movement.Linear;
import mario.rm.sprite.tiles.movement.Disappear;
import mario.rm.utility.MoveAttrib;
import mario.rm.utility.Punto;

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
public abstract class Tiles implements Size, Cloneable {    //sarebbe meglio astraatta per estensione ad altre classi

    protected int x;  //COORDINATA IN CUI SI TROVA(X)
    protected int y;  //COORDINATA IN CUI SI TROVA(Y)

    protected int mX; //NUMERO DI IMMAGINE DA MOSTRARE NELL'ASSE Y

    protected int width;    //LARGHEZZA
    protected int height;   //ALTEZZA

    protected int velX;   //VELOCITA CON CUI SI MUOVE IL PLAYER NELL'ASSE X
    protected int velY; //VELOCITA CON CUI SI MUOVE IL PLAYER NELL'ASSE Y

    protected Handler handler;  //INSIEME DI PLAYER E TILES

    protected double gravity;   //GRAVITA

    protected String type;    //TIPO DI SPRITE

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

    protected MoveAttrib att;
    protected int moveType;
    private Azione azione;
    
    private String script;

    public Tiles(int x, int y, int width, int height, Handler handler, String type, ArrayList<Tile> anim, boolean collide, String part, boolean damage, String script) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.handler = handler;
        this.type = type;
        stop = Tipologia.getValue(type, "time");
        isDelay = Tipologia.getValue(type, "delay");
        this.partTile = part;

        this.damage = damage;
        //System.out.println(""+type.name());
        if (!type.equals("VOID")) {
            anim.stream().filter((tile) -> (tile.getType().equals(type))).forEach((tile) -> {
                this.temp = tile.getImage(TilePart.valueOf(part));
            });
        }
        /*if(type.equals("CHECKPOINT"))System.out.println("x: "+x+" y: "+y/SuperMario.standardHeight);
        if(type.equals("ROD")){
            System.out.println("width: "+width+" height: "+height+" x: "+x+" y: "+y/SuperMario.standardHeight);
        }*/
        numImma = temp != null ? temp.length : 0;
        
        this.numSerieX = 1;
        this.collide = collide;
        
        this.script = script;

        script();

    }

    public Tiles(int x, int y, int width, int height, Handler handler, String type, boolean collide, String part, boolean damage, BufferedImage[] temp) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.handler = handler;
        this.type = type;
        stop = Tipologia.getValue(type, "time");
        isDelay = Tipologia.getValue(type, "delay");
        this.partTile = part;

        this.damage = damage;
        //System.out.println(""+type.name());

        this.temp = temp;

        numImma = temp != null ? temp.length : 0;

        this.numSerieX = 1;
        this.collide = collide;
        this.handler = handler;
    }

    public Tiles(int x, int y, int width, int height, Handler handler, String type, boolean collide, String part, String script) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.handler = handler;
        this.type = type;
        stop = Tipologia.getValue(type, "time");
        isDelay = Tipologia.getValue(type, "delay");
        //numImma = type.getNumeroImma();
        numImma = temp != null ? temp.length : 0;
        this.numSerieX = 1;
        this.collide = collide;
        this.partTile = part;

        this.script = script;
        
        script();
        
    }

    private void script() {
        att = new MoveAttrib(new Punto(x, y));
        att.setLast(new Punto(x, y));
        att.setDir(new Direction[]{Direction.LEFT, Direction.LEFT});
        if (script != null && !script.equals("")) {
            switch (script.split(" ")[1]) {
                case "normal":
                    moveType = 0;
                    azione = new Linear(script);
                    break;
                case "onCollide":
                    //azione = new OnCollide(script, this);
                    break;
                case "disappear":
                    azione = new Disappear(script, this);
                    break;
                case "GravityTile":
                    azione = new mario.rm.sprite.tiles.movement.GravityTile(handler, this);
            }
        } else {
            moveType = -1;
        }
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
            if (temp.length > 1 && System.currentTimeMillis() - before >= stop && !type.equals("VOID")) {
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
            try {
                for (int i = 0; i < numSerieX; i++) {
                    g.drawImage(temp[mX], x + (width * i), y, width, height, null);   //DISEGNO L'IMMAGINE
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        /*g.setColor(Color.WHITE);
        g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
        g.setColor(Color.red);
        g.drawRect(getBounds().x + 1, getBounds().y + 1, getBounds().width - 1, getBounds().height - 1);
        g.setColor(Color.BLACK);
        g.drawRect(getBounds().x + 2, getBounds().y + 2, getBounds().width - 2, getBounds().height - 2);
        g.setColor(Color.GREEN);
        g.drawRect(getBounds().x + 3, getBounds().y + 3, getBounds().width - 3, getBounds().height - 3);*/
 /*if(numSerieX > 1)
            System.out.println("Width: "+width+" numero: "+numSerieX+" = "+((width * numSerieX) - SuperMario.adaptWidth(20)) +" dichiarata: "+getBounds().width);*/
    }

    public Rectangle getBounds() {   //RITORNA L'AREA OCCUPANTE LO SPRITE SUL PIANO
        return new Rectangle(x, y, (width * numSerieX) - SuperMario.adaptWidth(20), height);  //POSIZIONE X, Y, LARGHEZZA, ALTEZZA. DA UTILIZZARE PER COLLIDER
    }

    /**
     *
     * @return RITORNA L'AREA OCCUPANTE DALLA PARTE ALTA DELLO SPRITE
     */
    public Rectangle getBoundsTop() {   //RITORNA L'AREA OCCUPANTE DALLA PARTE ALTA DELLO SPRITE
        return new Rectangle(x + SuperMario.adaptWidth(10), y, width * numSerieX - SuperMario.adaptWidth(20), SuperMario.adaptHeight(5));  //POSIZIONE X, Y, LARGHEZZA, ALTEZZA. DA UTILIZZARE PER COLLIDER
    }

    /**
     *
     * @return RITORNA L'AREA OCCUPANTE DALLA PARTE BASSA DELLO SPRITE
     */
    public Rectangle getBoundsBottom() {   //RITORNA L'AREA OCCUPANTE DALLA PARTE BASSA DELLO SPRITE
        return new Rectangle(x + SuperMario.adaptWidth(10), y + height - SuperMario.adaptHeight(5), width * numSerieX - SuperMario.adaptWidth(20), SuperMario.adaptHeight(5));  //POSIZIONE X, Y, LARGHEZZA, ALTEZZA. DA UTILIZZARE PER COLLIDER
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
        return new Rectangle((x + width) * numSerieX - SuperMario.adaptWidth(10), y + SuperMario.adaptHeight(10), SuperMario.adaptWidth(5), height - SuperMario.adaptHeight(20));  //POSIZIONE X, Y, LARGHEZZA, ALTEZZA. DA UTILIZZARE PER COLLIDER
    }

    public void tick() {
        if (azione != null) {
            att = azione.tick(att);
            x = att.getLast().getX();
            y = att.getLast().getY();
        }
    }

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

    public String getType() {
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

    @Override
    public Tiles clone() {
        /*Tiles s = null;
        try {
            s = getClass().getDeclaredConstructor(Integer.class, Integer.class, Integer.class, Integer.class,
                    Handler.class, Type.class, Boolean.class, String.class, Boolean.class, BufferedImage[].class).newInstance(
                    x, y, width, height, handler, type, collide, partTile, damage, temp);
            //return new Tiles(x, y, width, height, handler, type, collide, partTile, damage, temp)};
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }*/
        Tiles s = null;
        try {
            s = (Tiles) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Tiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }
    
    public void setCollide(boolean collide){
        this.collide = collide;
    }

    public Azione getAzione() {
        return azione;
    }

    public MoveAttrib getAtt() {
        return att;
    }
    
    public String getScript(){
         return script;
    }

}
