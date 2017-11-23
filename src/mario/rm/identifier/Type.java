package mario.rm.identifier;

import java.io.Serializable;

/**
 *
 * @author LENOVO
 */
public enum Type implements Serializable {

    SOLID (2000, 6, 16, 0, 0), //TILE NON ATTRAVERSABILE
    TARTOSSO (0, 0, 0, 3, 0), //NEMICO TARTOSSO
    SOLIDFIRE (2000, 6, 16, 0, 0), //TILE NON ATTRAVERSABILE
    SOLIDLIGHT (2000, 6, 16, 0, 0), //TILE NON ATTRAVERSABILE
    CROSSABLE (0, 0, 0, 0, 0), //TILE ATTRAVERSABILE
    VOID (0, 0, 0, 0, 0), //TILE ATTRAVERSABILE
    PLAYER (0, 0, 0, 0, 0), //PLAYER
    KOOPA (2, 4, 0, 3, 0),
    COIN (0, 6, 32, 0, 0), //MONETA
    MUSHROOM (4, 0, 0, 0, 0), //FUNGO
    UNLOCKABLE (2000, 4, 16, 0, 0), //QUANDO SALTANDOCI SOPRA ESCONO MONETE O POTENZIAMENTI
    UNLOCKABLE2 (2000, 4, 16, 0, 0), //QUANDO SALTANDOCI SOPRA ESCONO MONETE O POTENZIAMENTI
    UNLOCKABLE3 (2000, 4, 16, 0, 0), //QUANDO SALTANDOCI SOPRA ESCONO MONETE O POTENZIAMENTI
    CHECKPOINT (2, 0, 0, 0, 0), //CHECKPOINT
    MELODIE (2, 0, 0, 0, 0), //MELODIE
    TUBO_GREEN (0, 0, 0, 0, 0), //MELODIE
    TUBO_RED (0, 0, 0, 0, -16), //MELODIE
    TUBO_RED_DOWN (0, 0, 0, 0, 18), //MELODIE
    PIRANHAPLANT (0, 0, 0, 0, 0), //MELODIE
    BLOCK (2, 0, 0, 0, 0),
    
    
    GROUND (0, 0, 0, 0, 0), //TERRA
    
    COLUMNICE (0, 0, 0, 0, 0), //COLONNA DI GHIACCIO
    ICE (0, 0, 0, 0, 0), //COLONNA DI GHIACCIO
    
    DESERT (0, 0, 0, 0, 0), //COLONNA DI GHIACCIO
    COLUMNDESERT (0, 0, 0, 0, 0), //COLONNA DI GHIACCIO
    
    COLUMNSNOW (0, 0, 0, 0, 0), //COLONNA DI GHIACCIO
    SNOW (0, 0, 0, 0, 0), //COLONNA DI GHIACCIO
    
    DARK (0, 0, 0, 0, 0), //COLONNA DI GHIACCIO
    
    CHECKPOINTSAFE (0, 0, 0, 0, 0), //QUANDO SI COLPISCE IL CHECKPOINT, QUESTO CAMBIA BANDIERA
    CHAINCHOMP (0, 0, 0, 3, 0), //IL CATENACCIO ( NEMICO )
    BOO (0, 0, 0, 1, 1), //FANTASMINO CHE SEGUE IL PLAYER
    FLAG (0, 0, 0, 0, 0), //BANDIERA DI FINE LIVELLO
    FLAGCOMPLETE (0, 0, 0, 0, 0), //BANDIERA DI FINE LIVELLO CHE CAMBIA FORMA
    ROD (0, 0, 0, 0, 0), //ASTA DELLA BANDIERA DI FINE LIVELLO

    UP1(0, 0, 0, 0, 0),
    MUSHROOMPLATFORM(0, 0, 0, 0, 0),
    UNLOCKABLE_COIN(0, 0, 0, 0, 0),
    SPINE(0, 0, 0, 0, 0),
    BULLET (0, 0, 0, 0, 0),
    BRIDGE (0, 0, 0, 0, 0),
    PLAYER_LUIGI (0, 0, 0, 0, 0),
    KI (0, 0, 0, 0, 0);
    
     private int tempo;
    private int delay;
    private int numeroImma; //NUMERO DI IMMAGINI CONTENUTE NELL'ARRAY ANIMAZIONI
    private int velX;   //VELOCITA' X CHE DEVE AVERE
    private int velY;   //VELOCITA' Y CHE DEVE AVERE

    Type(int tempo, int delay, int numeroImma, int velX, int velY) {
        this.tempo = tempo;
        this.delay = delay;
        this.numeroImma = numeroImma;
        this.velX = velX;
        this.velY = velY;
    }

    public int getVelX() {
        return velX;
    }

    public int getVelY() {
        return velY;
    }

    public int getTempo() {
        return tempo;
    }

    public int getDelay() {
        return delay;
    }

    public int getNumeroImma() {
        return numeroImma;
    }
}
