package mario.rm.handler;

import Connessione.Connessione;
import Connessione.Profilo;
import Connessione.Query;
import Connessione.Relazione;
import java.awt.Graphics;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mario.MainComponent;
import mario.rm.SuperMario;
import mario.rm.input.Sound;
import mario.rm.sprite.Player;
import mario.rm.sprite.Sprite;
import mario.rm.input.Loader;
import mario.rm.input.MemoriaAC;
import mario.rm.input.Reader;
import mario.rm.input.SpriteLoad;
import mario.rm.sprite.enemy.Boo;
import mario.rm.sprite.enemy.Bullet;
import mario.rm.sprite.enemy.Cannone;
import mario.rm.sprite.enemy.Enemy;
import mario.rm.sprite.enemy.Plant;
import mario.rm.sprite.enemy.Tartosso;
import mario.rm.sprite.tiles.Checkpoint;
import mario.rm.sprite.tiles.CoinBlock;
import mario.rm.sprite.tiles.GravityTile;
import mario.rm.sprite.tiles.Solid;
import mario.rm.sprite.tiles.Tiles;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 *
 * gestisce la creazione di livelli ed il suo aggiornamento e rendering
 *
 */
public class Handler implements Reader {

    private LinkedList<Player> player;    //ELENCO PLAYER
    private static LinkedList<Tiles> tiles; //ELENCO TILES (DA MODIFICARE PER CREAZIONE DI SCENARI PIU COMPLESSI)
    private static LinkedList<Enemy> enemy; //ELENCO TILES (DA MODIFICARE PER CREAZIONE DI SCENARI PIU COMPLESSI)

    /**
     *
     * level - ELENCO DI LIVELLI DA CARICARE, IN ORDINE DAL PRIMO ALL'ULTIMO
     */
    public SelectLevel level;

    private final MemoriaAC memoria;

    private boolean next;

    private boolean sound;

    private SuperMario mario;

    private Sound nextL;

    private ArrayList<int[]> tilesCollision;

    private ArrayList<Tiles> position;

    public Handler(SuperMario mario) {
        player = new LinkedList<>();    //ELENCO PLAYER IN CAMPO
        tiles = new LinkedList<>(); //ELENCO TILES IN CAMPO
        enemy = new LinkedList<>(); //ELENCO NEMICI IN CAMPO

        tilesCollision = new ArrayList<>();
        position = new ArrayList<>();

        level = new SelectLevel(false);

        memoria = new MemoriaAC();    //CLASSE DOVE MEMORIZZO TUTTE LE IMMAGINI

        Bullet.bullet = memoria.getBullet();

        next = true;   //INDICA SE DEVE CAMBIARE LIVELLO

        nextL = new Sound("Sound/nsmb_stage_clear.mid");

        this.mario = mario;

        sound = false;

    }

    /**
     * Aggiorno la parte grafica
     *
     * @param g DISEGNA TUTTI GLI OGGETTI DELLA MAPPA
     */
    public void render(Graphics g) {
        Thread[] t = new Thread[3];

        int d = (int) (player.get(0).getX() + SuperMario.WIDTH / 1.5);
        int c = (int) (player.get(0).getX() - SuperMario.WIDTH / 1.5);

        int p = (int) (player.get(0).getY() + SuperMario.HEIGHT / 1.5);
        int z = (int) (player.get(0).getY() - SuperMario.HEIGHT / 1.5);

        t[0] = new Thread() {
            @Override
            public void run() {
                /*for (int i = player.size()-1; i < 0; i--) {
                    System.out.println("kdjshbfsdhfb");
                    player.get(i).render(g);
                }*/
                for (int i = 0; i < player.size(); i++) {
                    player.get(player.size() - 1 - i).render(g);
                }
            }
        };
        t[0].start();

        t[1] = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < tiles.size(); i++) {
                    if (tiles.get(i).getX() <= d && tiles.get(i).getX() >= c && p >= tiles.get(i).getY() && z <= tiles.get(i).getY()) {
                        tiles.get(i).render(g);
                    }
                }
            }
        };
        t[1].start();

        t[2] = new Thread() {
            @Override
            public void run() {

                for (int i = 0; i < enemy.size(); i++) {
                    if (enemy.get(i).getX() <= d && enemy.get(i).getX() >= c && p >= enemy.get(i).getY() && z <= enemy.get(i).getY()) {
                        if (!enemy.get(i).isCanDie() || !enemy.get(i).isEndDie()) { //SE IL NEMICO PUO' MORIRE E LA SUA ANIMAZIONE DELLA MORTE NON E' FINITA
                            enemy.get(i).render(g); //DISEGNA IL NEMICO
                        } else {
                            enemy.get(i).die(); //ALTRIMENTI LO "UCCIDE"
                        }
                    } else if (enemy.get(i) instanceof Bullet) {  //SE E' UN PROIETTILE LO ELIMINA COME SE FOSSE ANDATO FUORI MAPPA
                        enemy.get(i).die();
                    }
                }
            }
        };
        t[2].start();

        for (int i = 0; i < t.length; i++) {
            try {
                t[i].join();
            } catch (InterruptedException ex) {
                Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
            }

        }
        t = null;
    }

    /**
     * Aggiorno la parte logica del gioco
     */
    public void tick() {
        //long time = System.currentTimeMillis();
        /*tiles.stream().forEach((tiles) -> {    //SEMPLICE FOR MA CON I SUGGERIMENTI DI NETBEANS PER IL TICK DEGLI TILES
            if (tiles.getX() <= player.get(0).getX() + SuperMario.WIDTH / 2 && tiles.getX() >= player.get(0).getX() - SuperMario.WIDTH / 2) {
                tiles.tick();
            }
        });*/

        Thread[] thread = new Thread[3];

        thread[1] = new Thread() {
            @Override
            public void run() {
                player.stream().forEach((sprite) -> {   //SEMPLICE FOR MA CON I SUGGERIMENTI DI NETBEANS PER IL TICK DEI PLAYER
                    sprite.tick();
                });
            }
        };
        thread[1].start();
        int xMax = (int) (player.get(0).getX() + SuperMario.WIDTH / 1.5);
        int xMin = (int) (player.get(0).getX() - SuperMario.WIDTH / 1.5);
        thread[2] = new Thread() {
            @Override
            public void run() {
                if (enemy.size() > 0) { //SEMPLICE FOR MA CON I SUGGERIMENTI DI NETBEANS PER IL TICK DEGLI ENEMY
                    for (int i = 0; i < enemy.size(); i++) {
                        if (enemy.get(i).getX() <= xMax && enemy.get(i).getX() >= xMin) {
                            enemy.get(i).tick();
                        }
                    }
                }
            }
        };
        thread[2].start();
        int yMax = (int) (player.get(0).getY() + SuperMario.HEIGHT / 1.5);
        int yMin = (int) (player.get(0).getY() - SuperMario.HEIGHT);
        thread[0] = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < position.size(); i++) {
                    if (position.get(i).getX() <= xMax
                            && position.get(i).getX() >= xMin
                            && position.get(i).getY() >= yMin
                            && position.get(i).getY() <= yMax) {
                        position.get(i).tick();
                    }
                }
            }
        };
        thread[0].start();
        for (int i = 0; i < thread.length; i++) {
            try {
                thread[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (next) {   //SE IL PLAYER HA FATTO DIVENTARE TRUE NEXT
            sound = true;
            new Thread() {
                @Override
                public void run() {
                    memoria.getSound().stop();
                    if (Connessione.isConnected() && Profilo.looged) {
                        String currentLevelTemp = level.getCurrent().substring(level.getCurrent().lastIndexOf("/") + 1, level.getCurrent().lastIndexOf("."));
                        Relazione s = Query.sendSelect("SELECT score FROM score WHERE username=\"" + Profilo.username + "\" AND name=\"" + currentLevelTemp + "\"");
                        if (s.getValue().length == 0) {
                            Query.insert("INSERT INTO score (name, username, score) VALUES (\""
                                    + currentLevelTemp + "\",\""
                                    + Profilo.username + "\","
                                    + (Player.PUNTEGGIO + SuperMario.timeShow * 100) + ")");
                        } else if (Integer.parseInt(s.getValue()[0][0]) < (Player.PUNTEGGIO + SuperMario.timeShow * 100)) {
                            Query.insert("DELETE FROM score WHERE username=\"" + Profilo.username
                                    + "\" AND name=\"" + currentLevelTemp + "\"");
                            Query.insert("INSERT INTO score (name, username, score) VALUES (\""
                                    + currentLevelTemp + "\",\""
                                    + Profilo.username + "\","
                                    + (Player.PUNTEGGIO + SuperMario.timeShow * 100) + ")");
                        }
                        Query.score = Query.sendSelect("SELECT username, score FROM Score WHERE name = \"" + currentLevelTemp + "\" ORDER BY SCORE DESC LIMIT 10");
                    }
                    mario.createLV(false);   //CREA IL PROSSIMO LIVELLO (DA FIXARE PER IL CARICAMENTO)
                }
            }.start();
            nextL.start();
            nextL.isRunning();
            nextL.stop();
            mario.setIsWait();
            if (!Sound.soundON) {
                try {
                    sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            sound = false;
        }
        //System.out.println("tick: "+(System.currentTimeMillis()-time));
    }

    /**
     *
     * @return se ritorna falso, c'è stato un errore nel caricamento del livello
     */
    public boolean newLevel(boolean current) {

        player.clear(); //PULISCE IL BUFFER DEI PLAYER
        enemy.clear();  //PULISCE IL BUFFER DEI NEMICI
        tiles.clear();  //PULISCE IL BUFFER DEI TILE

        memoria.clean();
        memoria.carica();
        //memoria.adaptImage(SuperMario.standardWidth, SuperMario.standardHeight);
        System.gc();
        if (!new Loader().convertTextInMap(current ? level.getCurrent() : level.getNext(), this)) {
            return false;
        } //CREA IL LIVELLO

        int temp = tiles.size();    //SEMPLICE VARIABILE PER OTTENERE INFORMAZIONI SUL RENDERING DEL LIVELLO
        Log.append("" + temp, DefaultFont.INFORMATION);
        miglioraLivello();  //CERCO DI RIDURRE IL NUMERO DI SPRITE, IN MODO DA VELOCIZZARE AGGIORNAMENTO E RENDERING

        Log.append("" + tiles.size(), DefaultFont.INFORMATION);
        Log.append("" + (temp - tiles.size()), DefaultFont.INFORMATION);
        Log.append("Migliorato del: " + ((double) 100 / temp * (temp - tiles.size())), DefaultFont.INFORMATION); //CALCOLO DI QUANTO E' MIGLIORATO IL LIVELLO (%)

        //cloneCurrentStatus();
        if (!current) {
            memoria.nextSound();
        }
        while (sound) {
            try {
                sleep(10);
            } catch (InterruptedException ex) {
            }
        }
        memoria.getSound().loop();
        Player.initSound();

        System.gc();
        System.out.println("level created: " + MainComponent.memoryUsed());

        next = false;   //INDICA CHE DOPO QUESTO NON DOVRA' PIU' CARICARE UN NUOVO LIVELLO

        return true;
    }

    /**
     *
     * @param sp: il player da rimuovere
     */
    public void removePlayer(Player sp) {    //RIMUOVE UN PLAYER DALL'ELENCO
        player.remove(sp);
    }

    /**
     *
     * @param sp: il tile da rimuovere
     */
    public void removeTile(Tiles sp) {   //RIMUOVE UN TILES DALL'ELENCO
        tiles.remove(sp);
    }

    /**
     *
     * @param sp: il player da aggiungere
     */
    public void addPlayer(Player sp) {   //AGGIUNGE UN PLAYER ALL'ELENCO
        player.push(sp);
    }

    /**
     *
     * @param til: il tile da aggiungere
     */
    public void addTiles(Tiles til) {    //AGGIUNGE UN TILE ALL'ELENCO
        tiles.push(til);
    }

    public void addEnemy(Enemy en) {
        enemy.add(en);
    }

    /**
     *
     * @param x0: coordinate x dove deve essere disegnato
     * @param y0: coordinate y dove deve essere disegnato
     * @param type: che tipo e' lo sprite da disegnare (player, koopa, boo,
     * tartosso, solid...)
     * @param unlockable: nel caso il type sia unlockable questo specifica, che
     * cosa sblocca il contatto col tile
     * @param tile: nel caso dei tile questo specifica che parte del tile
     * bisogna disegnare
     */
    @Override
    public void creaLivello(SpriteLoad loaded) {
        int x0 = loaded.getX() * SuperMario.standardWidth;
        int y0 = loaded.getY() * SuperMario.standardHeight;

        String type = loaded.getType();
        String tile = loaded.getPartTile();

        String unlockable = loaded.getUnlockableType();
        int numero = loaded.getUnlockableQuantity();

        String movement = loaded.getMovement();
        //System.out.println(""+type.name());
        if (type != null) //IN BASE AD UNA IMMAGINE, SCANSIONA PIXEL PER PIXEL IL SUO CONTENUTO, E NE CREA UNA, CON LE INDICAZIONI DATOGLI DALL'IMMAGINE
        {
            if (unlockable.equals("")) {
                switch (type) {
                    case "UNLOCKED":
                        tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this,
                                type, memoria.getUnlockable(), true, tile, false, movement));   //SE E NERO E' NORMALE SOLIDO
                        break;
                    case "UNLOCKABLE":
                    case "SOLID":
                        tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this,
                                type, memoria.getTiles(), true, tile, false, movement));   //SE E NERO E' NORMALE SOLIDO
                        break;
                    //case "UNLOCKED":
                    //tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, memoria.getUnlockable(), true, tile, false, unlockable));
                    //break;
                    case "PLAYER_LUIGI":
                        long memoryUsed = MainComponent.memoryUsed();
                        System.out.println("pre: " + memoryUsed);
                        for (int i = 0; i < SuperMario.playerNumber; i++) {
                            if (i % 2 == 0) {
                                player.add(new Player(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, "CRASH", i));   //SE PIXEL BLU è UN PLAYER
                            } else {
                                player.add(new Player(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, "PLAYER_LUIGI", i));   //SE PIXEL BLU è UN PLAYER
                            }
                        }
                        break;
                    case "COIN":
                    case "MUSHROOM":
                    case "UP1":
                        tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this,
                                type, memoria.getUnlockable(), false, tile, false, movement)); //SE E GIALLO E' UNA MONETA
                        break;
                    /*case MUSHROOMPLATFORM:
                    tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, true, tile, tile)); //SE E ROSSO E' UN FUNGO
                    break;*/
                    case "SPINE":
                        tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this,
                                type, memoria.getTiles(), true, tile, true, movement));
                        break;
                    case "TARTOSSO":
                        enemy.add(new Tartosso(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this,
                                type, true));
                        break;
                    case "KOOPA":
                        enemy.add(new Enemy(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this,
                                type, true));
                        break;
                    case "TUBO_RED":
                    case "TUBO_RED_DOWN":
                        tiles.add(new Solid(x0, y0 - SuperMario.standardHeight, SuperMario.standardWidth * 2, SuperMario.standardHeight * 2,
                                this, type, memoria.getTiles(), true, tile, false, movement));
                        break;
                    case "PIRANHAPLANT":
                        enemy.add(new Plant(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this,
                                type, false));
                        break;
                    case "GROUND":
                        tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this,
                                type, true, tile, movement));   //SE E NERO E' NORMALE SOLIDO
                        break;
                    case "CHECKPOINT":
                        tiles.add(new Checkpoint(x0, y0 - SuperMario.standardHeight, SuperMario.standardWidth * 2, SuperMario.standardHeight * 2,
                                this, type, memoria.getUnlockable(), false, tile, movement));   //SE E NERO E' NORMALE SOLIDO
                        break;
                    case "VOID":
                        tiles.add(new Checkpoint(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this,
                                type, memoria.getTiles(), true, tile, movement));   //SE E NERO E' NORMALE SOLIDO
                        break;
                    case "CHAINCHOMP":
                        enemy.add(new Enemy(x0, y0 - SuperMario.standardHeight, SuperMario.standardWidth * 2, SuperMario.standardHeight * 2,
                                this, type, false));
                        break;
                    case "BOO":
                        enemy.add(new Boo(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this,
                                type, false));
                        break;
                    case "FLAG":
                        tiles.add(new Checkpoint(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this,
                                type, memoria.getUnlockable(), false, tile, movement));   //SE E NERO E' NORMALE SOLIDO
                        break;
                    case "ROD":
                        int x1 = (int) (SuperMario.standardWidth - SuperMario.standardWidth / SuperMario.adaptWidth(10)) + SuperMario.adaptWidth(2);
                        tiles.add(new Checkpoint(x0 + x1, y0, SuperMario.standardWidth / SuperMario.adaptWidth(10), SuperMario.standardHeight,
                                this, type, memoria.getUnlockable(), false, tile, movement));   //SE E NERO E' NORMALE SOLIDO
                        break;
                    case "DARK":
                    case "SNOW":
                    case "COLUMNSNOW":
                    case "DESERT":
                    case "COLUMNDESERT":
                    case "ICE":
                    case "COLUMNICE":
                    case "MUSHROOMPLATFORM":
                        /*tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this,
                                type, true, tile, movement));*/   //SE E NERO E' NORMALE SOLIDO
                        tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this,
                                type, memoria.getTerreni(), true, tile, false, movement));
                        break;
                    case "CANNONE":
                        x0 -= SuperMario.standardWidth;
                        y0 -= SuperMario.standardHeight;
                        enemy.add(new Cannone(x0, y0, SuperMario.standardWidth * 2, SuperMario.standardHeight * 2, this,
                                type, false, "MISSILE"));
                        break;
                    default:
                        break;
                }
                if (!movement.equals("")) {
                    position.add(tiles.getLast());
                }
            } else {
                //tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, true, tile, tile)); //SE E ROSSO E' UN FUNGO
                //System.out.println("Move: "+movement);
                tiles.add(new CoinBlock(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this,
                        type, memoria.getTiles(), unlockable, true, tile, numero, movement));
            }
        } else {
            Log.append(Handler.class.getName() + " " + "Questa animazione non e' valida: manca il Type di appartenenza", DefaultFont.ERROR);
        }

    }

    /**
     *
     * @return RITORNA L'ELENCO DELLE IMMAGINI IN MEMORIA
     */
    public MemoriaAC getMemoria() {
        return memoria;
    }

    /**
     *
     * @return RITORNA L'ELENCO DEI PLAYER
     */
    public LinkedList<Player> getPlayer() {
        return player;
    }

    /**
     *
     * @return RITORNA L'ELENCO DEI TILES
     */
    public LinkedList<Tiles> getTiles() {
        return tiles;
    }

    /**
     *
     * @return RITORNA L'ELENCO DEI NEMICI
     */
    public LinkedList<Enemy> getEnemy() {
        return enemy;
    }

    /**
     *
     * @param enemy: nemico da rimuovere dall'elenco
     */
    public void removeEnemy(Sprite enemy) {
        this.enemy.remove(enemy);
    }

    /**
     *
     */
    private void miglioraLivello() {    //MIGLIORI PRETAZIONALMENTE IL LIVELLO TOGLIENDO GLI SPRITE "INUTILI"
        for (int i = 0; i < tiles.size(); i++) {
            /*if (tiles.get(i).getType().equals("SOLID") || tiles.get(i).getType().equals("COINS") || tiles.get(i).getType().equals("MUSHROOM") || tiles.get(i).getType().equals("UNLOCKABLE")) {
                continue;
            }*/
            if (tiles.get(i) instanceof CoinBlock || tiles.get(i) instanceof GravityTile || tiles.get(i).getType().equals("SOLID")) {
                continue;
            }
            for (int j = i + 1; j < tiles.size(); j++) {
                /*if (tiles.get(j).getType().equals("SOLID") || tiles.get(j).getType().equals("COIN") || tiles.get(j).getType().equals("MUSHROOM") || tiles.get(j).getType().equals("UNLOCKABLE")) {
                    continue;
                }*/
                if (tiles.get(i) instanceof CoinBlock || tiles.get(i) instanceof GravityTile || tiles.get(i).getType().equals("SOLID")) {
                    continue;
                }
                if (tiles.get(i).getType().equals(tiles.get(j).getType())
                        && tiles.get(i).getX() + (tiles.get(i).getWidth() * tiles.get(i).getSerie()) == tiles.get(j).getX()
                        && tiles.get(i).getY() == tiles.get(j).getY()
                        && tiles.get(i).getTile().equals(tiles.get(j).getTile())
                        && tiles.get(i).getScript().equals(tiles.get(j).getScript())) {
                    tiles.get(i).moreSerie();
                    tiles.remove(j);
                }
            }
        }

    }

    public void addScript() {
        position.add(tiles.getFirst());
    }

    /**
     *
     */
    public void next() {    //GLI DICO DI CAMBIARE LIVELLO
        next = true;
    }

    /**
     *
     * @return RITORNA LA CANZONE DI SOTTOFONDO DA IMPOSTARE
     */
    public Sound getSound() {
        return memoria.getSound();
    }

    /**
     *
     * @return ritorna il livello corrente
     */
    public String getLevel() {
        return level.getCurrent();
    }

    public void cloneCurrentStatus() {
    }

    public void restoreStatus() {
    }

    public boolean getNext() {
        return next;
    }

    public void clean() {
        memoria.clean();
        memoria.delete();
        position = null;
        tilesCollision = null;
        nextL = null;
        player = null;
        enemy = null;
        tiles = null;
        level = null;
        mario = null;
    }
}
