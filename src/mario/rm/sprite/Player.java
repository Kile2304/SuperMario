package mario.rm.sprite;

import mario.rm.identifier.Type;
import java.awt.Rectangle;
import static java.lang.Thread.sleep;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mario.rm.SuperMario;
import static mario.rm.SuperMario.HEIGHT;
import static mario.rm.SuperMario.adaptHeight;
import mario.rm.camera.Camera;
import mario.rm.handler.Handler;
import mario.rm.identifier.Move;
import mario.rm.input.Movement;
import mario.rm.input.Sound;
import mario.rm.sprite.enemy.Boo;
import mario.rm.sprite.enemy.Boss;
import mario.rm.sprite.enemy.Enemy;
import mario.rm.sprite.tiles.Tiles;
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class Player extends Sprite {    //PLAYER(DA ESTENDERE SU UN'ALTRA FUTURA CLASSE)

    public static int MONETE = 0;
    private boolean grow;

    private boolean immortal;   //VARIABILE CHE GESTISCE L'IMMORTALITA' TEMPORANEA DEL PLAYER
    private long time;  //IMPOSTA UN TIMER IN MODO CHE DOPO UN TOT DI TEMPO CHE IL PLAYER E' STATO COLPITO, POSSA ANCORA ESSERE COLPITO

    private int life;   //NUMERO DI VITE

    private int respawnX;   //SE MUORE LO RIPORTA A QUESTE COORDINATE X
    private int respawnY;   //SE MUORE LO RIPORTA A QUESTE COORDINATE Y

    public static int PUNTEGGIO = 0;    //PUNTEGGIO A CUI E' ARRIVATO

    private boolean teleport;   //USATO PER I TUBI

    private static final double STACCO = adaptHeight(0.17); //COSTANTE VARIABILE IN BASE ALLA RISOLUZIONE DELLO SCHERMO, PER IL SALTO 

    private static final Sound[] sound = new Sound[]{new Sound("Sound/nsmb_death.wav"), new Sound("Sound/nsmb_power-up.wav"), new Sound("Sound/nsmb_coin.wav")};   //SUONE DELLA MORTEM DEL POWER UP E DEL COIN

    //cheat
    private boolean godMode = false;
    private int movXIncrease;
    private double jumpIncrease;
    private boolean infiniteJump = false;

    public Player(int x, int y, int width, int height, Handler handler, Type type) {  //NORMALE INIZIALIZZAZIONE CON IL COSTRUTTORE
        super(x, y, width, height, handler, type, handler.getMemoria().getPlayer());
        Log.append("6)INIZIALIZZO IL PLAYER"+ "(number of player) "+handler.getMemoria().getPlayer().size(), DefaultFont.INFORMATION);
        grow = false;   //INDICA SE E' GRANDE O PICCOLO

        immortal = false;   //QUANDO VIENE COLPITO HA UN TOT DI TEMPO PER SCAPPARE DAL NEMICO

        life = 999999;

        respawnX = x;   //COORDINATE PER IL RESPAWN
        respawnY = y;

        Boo.setPlayer(this);
        Boss.setPlayer(this);

        direzione = 100;
    }

    /**
     *
     * @return IMOSTA A POSSO USARE IL TUBO
     */
    @Override
    public void setTeleport() {
        teleport = true;
    }

    /**
     *
     * @param direzione
     * @return IMPOSTO LA DIREZIONE
     */
    @Override
    public void setDirezione(int direzione) {
        this.direzione = direzione;
    }

    /**
     *
     * @return AGGIORNA LA SUA POSIZIONE, INOLTRE GESTISCE: GRAVITA, SALTO,
     * COLLISIONI CON NEMICI/TILES, MORTE, RIPRODUZIONI SUONI, RICHIAMO CAMBIO
     * DI LIVELLO...
     */
    @Override
    public void tick() {    //SE FACCIO PARTIRE THREAD PARTE ANCHE QUESTO (FIXARE, SPOSTARE E MODIFICARE UN PO DI ROBE
        falling = true;  //NON STA' TOCCANDO IL PAVIMENTO

        Thread[] t = new Thread[2];

        t[0] = new Thread() {
            @Override
            public void run() {
                LinkedList<Tiles> tile = handler.getTiles();
                for (int i = 0; i < tile.size(); i++) {

                    int tileX = tile.get(i).getX();

                    if (tileX <= x + width * 3 && tileX >= x - width * 2 && tile.get(i).getY() >= y - height * 2 && tile.get(i).getY() <= y + height * 3) {
                        Type tileType = tile.get(i).getType();
                        Rectangle bounds = tile.get(i).getBounds();

                        if (getBounds().intersects(bounds)) {
                            if (tileType == Type.ROD || tile.get(i).getType() == Type.FLAGCOMPLETE) {
                                handler.next(); //SE COLPISCE LA BANDIERA DI FINE LIVELLO LO CAMBIA
                            } else if (tileType == Type.COIN) {
                                tile.get(i).die(); //LA MONETA VIENE RIMOSSA
                                sound[2].stop();
                                sound[2].start();
                                if (MONETE < 100) //SE HO MENO DI 100 MONETE
                                {
                                    MONETE++;   //IL NUMERO DELLE MONETE AUMENTA
                                } else {   //ALTRIMENTI
                                    life++; //AUMENTO LE VITE DI 1
                                    MONETE = 0; //AZZERO IL NUMERO DELLE MONETE
                                }
                                PUNTEGGIO += 100;   //AUMENTO IL PUNTEGGIO
                            } else if (tileType == Type.MUSHROOM) {
                                if (!grow) {    //
                                    if (sound[1].getCurrentFrame() != 0) {
                                        sound[1].stop();
                                    }
                                    sound[1].start();
                                    width += crescita; //RADDOPPIA LA LARGHEZZA DEL PLAYER
                                    height += crescita;    //RADDOPPIA L'ALTEZZA DEL PLAYER
                                    y -= crescita;  //MODIFICO LE  COORDINATE PERCHE' E' DIVENTATO GRANDE
                                    x -= crescita;  //MODIFICO LE COORDINATE PERCHE' E' DIVENTATO GRANDE
                                    grow = true;    //IL PAYER E' GRANDE
                                }
                                tile.get(i).die(); //IL FUNGO VIENE RIMOSSO
                            } else if (tileType == Type.CHECKPOINT) {
                                respawnX = tile.get(i).getX() - standardWidth;
                                respawnY = tile.get(i).getY() + standardHeight;
                                Camera.setUpY(-respawnY + HEIGHT - SuperMario.standardHeight * 4);
                                tile.get(i).unlockable();   //SBLOCCO IL CHECKPINT
                                if (!grow) {
                                    if (sound[1].getCurrentFrame() != 0) {
                                        sound[1].stop();
                                    }
                                    sound[1].start();
                                    width += crescita; //RADDOPPIA LA LARGHEZZA DEL PLAYER
                                    height += crescita;    //RADDOPPIA L'ALTEZZA DEL PLAYER
                                    y -= crescita;  //MODIFICO LE  COORDINATE PERCHE' E' DIVENTATO GRANDE
                                    x -= crescita;  //MODIFICO LE COORDINATE PERCHE' E' DIVENTATO GRANDE
                                    grow = true;    //IL PAYER E' GRANDE
                                }
                            } else if (tileType == Type.UP1) {
                                life++;
                                tile.get(i).die();
                            } else if (tile.get(i).getCollide()) {
                                if (tile.get(i).canDamage() && !godMode) {
                                    if (grow) {
                                        grow = false;
                                        width -= crescita;
                                        height -= crescita;
                                        x -= crescita;
                                        y -= crescita;
                                    } else {
                                        die();
                                    }
                                    continue;
                                }
                                if (getBoundsTop().intersects(bounds)) {    //INTERSEZIONE PARTE ALTA
                                    gravity = 0.0;    //AZZERO LA GRAVITA'
                                    y = tile.get(i).getY() + tile.get(i).getHeight();   //LA SUA COORDINATA Y DIVIENE PARI AD LA POSIZIONE DEL TILE (Y) PIU HEIGHT del tile
                                    falling = true; //CADENDO E' VERO
                                    if (grow && tileType == Type.SOLID) {  //SE SONO GRANDE E LO COLPISCO CON LA TESTA LO DSTRUGGO
                                        tile.get(i).die();  //ELIMINO IL TILE
                                    }
                                    if (tileType == Type.UNLOCKABLE) { //E' UN CUBO CHE SBLOCCA QUALCOSA CHE SE LO COLPISCO CON LA TESTA SBLOCCO QUALCOSA
                                        tile.get(i).unlockable();   //SBLOCCO
                                    }
                                    if (tileType == Type.TUBO_RED) {
                                        y -= 16 * SuperMario.standardHeight;
                                        Camera.setUpY(-y + HEIGHT - SuperMario.standardHeight * 4 - height);
                                    }
                                }
                                if (getBoundsBottom().intersects(bounds)) { //INTERSEZIONE PARTE BASSA
                                    falling = false;   //IMPOSTO A STA' TOCCANDO IL PAVIMENTO
                                    if (tileType == Type.VOID) {   //SE TOCCA UNA ZONA DA NON TOCCARE
                                        if (grow) { //SE ERA GRANDE
                                            grow = false;   //TORNA PICCOLO
                                            width -= crescita;
                                            height -= crescita;
                                        }
                                        die();
                                        return;
                                    }

                                    if (tileType == Type.TUBO_RED && teleport || tileType == Type.TUBO_RED_DOWN && teleport) {
                                        y += tileType.getVelY() * SuperMario.standardHeight;
                                        Camera.setUpY(-y + HEIGHT - SuperMario.standardHeight * 4 - height); //REIMPOSTO LA TELECAMERA PER IL RESPAWN
                                        continue;
                                    }

                                    if (!jumping) { //SE NON STA SALTANDO
                                        gravity = 0.0;    //AZZERO LA GRAVITA'
                                        velY = 0;   // AZZERO LA VELOCITA' ULL'ASSE Y
                                    }

                                    y = tile.get(i).getY() - height + 1;//LA SUA POSIZIONE DIVIENE POSIZIONE TILE (Y) MENO L'ALTEZZA

                                    if (!jumping) { //SE NON STA' SALTANDO
                                        if (velX != 0) {    //SE LA VELOCITA' E' DIVERSA DA 0
                                            lastMove = Move.WALK;
                                        } else {
                                            lastMove = Move.STAND;
                                        }
                                    }

                                }
                                if (getBoundsRight().intersects(bounds)) {  //INTERSEZIONE PARTE DESTRA
                                    x = tileX - width; //LA POSIZIONE IN X DIVENTA LA X DEL TILE MENO LA LARGHEZZA
                                    if (walking) {  //SE PRIMA STAVA CAMMINANDO
                                        walking = false;    //NON STA PIU CAMMINANDO
                                    }
                                }
                                if (getBoundsLeft().intersects(bounds)) {   //INTERSEZIONE PARTE SINISTRA (DOVREBBE ESSERE PERFETTO)
                                    x = tileX + tile.get(i).getWidth() - SuperMario.adaptWidth(20); //LA POSIZIONE IN X DIVENTA LA X DEL TILE MENO LA LARGHEZZA del tile
                                    if (walking) {  //SE PRIMA STAVA CAMMINANDO
                                        walking = false;    //NON STA PIU CAMMINANDO
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
        t[0].start();
        t[1] = new Thread() {
            @Override
            public void run() {
                LinkedList<Enemy> enemy = handler.getEnemy();
                for (int i = 0; i < enemy.size(); i++) {
                    if (!enemy.get(i).isDie()) {
                        if (getBoundsBottom().intersects(enemy.get(i).getBoundsTop())) { //INTERSEZIONE PARTE BASSA
                            if (enemy.get(i).isCanDie()) {
                                enemy.get(i).die(); //IL NEMICO VIENE SCONFITTO
                                y = enemy.get(i).getY() - height - 1;
                                gravity = -7.2;   //SALTA IN ALTO
                                jumping = true; //DICE CHE SALTANDO
                                falling = false;    //DICE CHE NON STA CADENDO
                                PUNTEGGIO += 1000;  //AGGIORNO IL PUNTEGGIO
                            }
                        } else if (getBounds().intersects(enemy.get(i).getBounds())) {  //SE COLPISCE IL NEMICO CON QUALSIASI ALTRA PARTE
                            if (!immortal && !godMode) {    //NEL CASO NON SIA IMMORTALE
                                if (!grow) {    //NEL CASO NON SIA GRANDE
                                    die();
                                } else {    //SE ERA GRANDE
                                    grow = false;   //LO FA DIVENTARE PICCOLO
                                    width -= crescita;  //DECREMENTA LA LARGHEZZA
                                    height -= crescita; //DECREMENTA LA ALTEZZA
                                    y += crescita;  //RIPOSIZIONA SULL'ASSE Y
                                }
                                time = System.currentTimeMillis();  //MEMORIZZA IN CHE MOMENTO E' STATO COLPITO
                                immortal = true;    //LO RENDE TEMPORANEAMENTE IMMORTALE
                            }
                        }
                    }
                }
            }
        };
        t[1].start();

        for (int i = 0; i < t.length; i++) {
            try {
                t[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        if (jumping) {    //SE STA SALTANDO
            gravity -= STACCO; //VIENE DECREMENTATA LA GRAVITA
            velY = ((int) -gravity);    //LA VELOCITA E PARI AL NEGATIVO DELLA GRAVITA
            if (gravity <= 0.0) {   //SE LA GRAVITA E MINORE DI ZERO
                jumping = false;    // NON STA PIU SALTANDO
                falling = true; //STA CADENDO
            }
        } else if (falling) {    //SE STA CADENDO
            gravity += STACCO; //AUMENTA LA GRAVITA
            velY = ((int) gravity); //LA VELOCITA E PARI ALLA GRAVITA
        }

        if (immortal) { //SE E' STATO COLPITO DA PIù DI 2 SECONDI GLI TOLGO L'IMMORTALITA'
            if (System.currentTimeMillis() - time >= timer) {
                immortal = false;   //IMPOSTA IL PLAYER A PUO' ESSERE COLPITO
            }
        }

        teleport = false;

        x += velX;  //MI SPOSTO SULL'ASSE X
        y += velY;  //MI SPOSTO SULL'ASSE Y

        if (!walking) {
            velX = 0;
        }
    }

    /**
     *
     * @return UCCIDE IL PLAYER
     */
    @Override
    public void die() {

        handler.getSound().stop();

        life--; //GLI VIENE TOLTA UNA VITA
        //System.out.println("" + life);
        if (life == 0) {    //SE HA 0 VITE
            System.out.println("Perso");    //GLI DICE DI AVERE PERSO
            System.exit(0); //TERMINA BRUSCAMENTE IL GIOCO
        } else {  //SE HA ANCORA VITE LO FA SPAWNARE AL CHECKPOINT PRECEDENTE
            x = respawnX;   //IMPOSTA LE COORDINATE X A QUELLE DEL CHECKPOINT PRECEDENTE
            y = respawnY;   //IMPOSTA LE COORDINATE Y A QUELLE DEL CHECKPOINT PRECEDENTE
            Camera.setUpY(-y + HEIGHT - SuperMario.standardHeight * 4);
            sound[0].start();
            sound[0].isRunning();
            //handler.restoreStatus();
        }
        handler.getSound().loop();

    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLife() {
        return life;
    }

    public void changeGodMode() {
        godMode = !godMode;
    }

    public boolean getGodMode() {
        return godMode;
    }

    public int getMoveXIncrease() {
        return movXIncrease + Movement.velX;
    }

    public void setMoveXIncrease(int movXIncrease) {
        this.movXIncrease = movXIncrease - Movement.velX;
    }

    public double getJumpIncrease() {
        return jumpIncrease + Movement.jump;
    }

    public void setJumpIncrease(double jumpIncrease) {

        this.jumpIncrease = jumpIncrease - Movement.jump;
    }

    public boolean getInfiniteJump() {
        return infiniteJump;
    }

    public void changeInfiniteJump() {
        infiniteJump = !infiniteJump;
    }

}
