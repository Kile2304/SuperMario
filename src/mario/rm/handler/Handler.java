package mario.rm.handler;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mario.rm.SuperMario;
import mario.rm.input.Sound;
import mario.rm.sprite.Player;
import mario.rm.sprite.Sprite;
import mario.rm.identifier.Type;
import mario.rm.input.Loader;
import mario.rm.input.MemoriaAC;
import mario.rm.input.Reader;
import mario.rm.sprite.enemy.Boo;
import mario.rm.sprite.enemy.Enemy;
import mario.rm.sprite.enemy.Plant;
import mario.rm.sprite.enemy.Tartosso;
import mario.rm.sprite.tiles.Checkpoint;
import mario.rm.sprite.tiles.CoinBlock;
import mario.rm.sprite.tiles.Solid;
import mario.rm.sprite.tiles.Tiles;

/**
 *
 * @author LENOVO
 *
 * gestisce la creazione di livelli ed il suo aggiornamento e rendering
 *
 */
public class Handler implements Reader {

    private final LinkedList<Player> player;    //ELENCO PLAYER
    private final LinkedList<Tiles> tiles; //ELENCO TILES (DA MODIFICARE PER CREAZIONE DI SCENARI PIU COMPLESSI)
    private final LinkedList<Enemy> enemy; //ELENCO TILES (DA MODIFICARE PER CREAZIONE DI SCENARI PIU COMPLESSI)

    /**
     *
     * @return ELENCO DI LIVELLI DA CARICARE, IN ORDINE DAL PRIMO ALL'ULTIMO
     */
    private final SelectLevel level;

    private final MemoriaAC memoria;

    private boolean next;

    private boolean sound;

    private final SuperMario mario;

    private Sound nextL;

    private final boolean checkpoint;

    public Handler(SuperMario mario) {
        player = new LinkedList<>();    //ELENCO PLAYER IN CAMPO
        tiles = new LinkedList<>(); //ELENCO TILES IN CAMPO
        enemy = new LinkedList<>(); //ELENCO NEMICI IN CAMPO

        level = new SelectLevel(false);

        memoria = new MemoriaAC();    //CLASSE DOVE MEMORIZZO TUTTE LE IMMAGINI

        next = false;   //INDICA SE DEVE CAMBIARE LIVELLO

        nextL = new Sound("mario/res/Sound/nsmb_stage_clear.mid");

        this.mario = mario;

        sound = false;

        checkpoint = false;
    }

    /**
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
                player.stream().forEach((player) -> {
                    player.render(g);
                });
            }
        };
        t[0].start();

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
                    }
                }
            }
        };
        t[2].start();

        t[1] = new Thread() {
            @Override
            public void run() {
                tiles.stream().forEach((tiles) -> {    //SEMPLICE FOR MA CON I SUGGERIMENTI DI NETBEANS PER IL RENDER DEGLI TILES
                    if (tiles.getX() <= d && tiles.getX() >= c && p >= tiles.getY() && z <= tiles.getY()) {
                        tiles.render(g);
                    }
                });
            }
        };
        t[1].start();

        for (int i = 0; i < t.length; i++) {
            try {
                t[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     *
     */
    public void tick() {
        //long time = System.currentTimeMillis();
        /*tiles.stream().forEach((tiles) -> {    //SEMPLICE FOR MA CON I SUGGERIMENTI DI NETBEANS PER IL TICK DEGLI TILES
            if (tiles.getX() <= player.get(0).getX() + SuperMario.WIDTH / 2 && tiles.getX() >= player.get(0).getX() - SuperMario.WIDTH / 2) {
                tiles.tick();
            }
        });*/
        player.stream().forEach((sprite) -> {   //SEMPLICE FOR MA CON I SUGGERIMENTI DI NETBEANS PER IL TICK DEI PLAYER
            sprite.tick();
        });
        int t = (int) (player.get(0).getX() + SuperMario.WIDTH / 1.5);
        int c = (int) (player.get(0).getX() - SuperMario.WIDTH / 1.5);
        if (enemy.size() > 0) { //SEMPLICE FOR MA CON I SUGGERIMENTI DI NETBEANS PER IL TICK DEGLI ENEMY
            enemy.stream().forEach((enemy) -> {
                if (enemy.getX() <= t && enemy.getX() >= c) {
                    enemy.tick();
                }
            });
        }
        if (next) {   //SE IL PLAYER HA FATTO DIVENTARE TRUE NEXT
            sound = true;
            new Thread() {
                @Override
                public void run() {
                    memoria.getSound().stop();
                    mario.createLV();   //CREA IL PROSSIMO LIVELLO (DA FIXARE PER IL CARICAMENTO)
                }
            }.start();
            nextL.start();
            nextL.isRunning();
            nextL.stop();
            sound = false;
        }
        //System.out.println("tick: "+(System.currentTimeMillis()-time));
    }

    /**
     *
     * @return se ritorna falso, c'è stato un errore nel caricamento del livello
     */
    public boolean newLevel() {
        next = false;   //INDICA CHE DOPO QUESTO NON DOVRA' PIU' CARICARE UN NUOVO LIVELLO

        player.clear(); //PULISCE IL BUFFER DEI PLAYER
        enemy.clear();  //PULISCE IL BUFFER DEI NEMICI
        tiles.clear();  //PULISCE IL BUFFER DEI TILE

        memoria.carica();

        if (!new Loader().convertTextInMap(level.getNext(), this)) {
            return false;
        } //CREA IL LIVELLO

        int temp = tiles.size();    //SEMPLICE VARIABILE PER OTTENERE INFORMAZIONI SUL RENDERING DEL LIVELLO
        System.out.println("" + temp);
        miglioraLivello();  //CERCO DI RIDURRE IL NUMERO DI SPRITE, IN MODO DA VELOCIZZARE AGGIORNAMENTO E RENDERING

        System.out.println("" + tiles.size());
        System.out.println("" + (temp - tiles.size()));
        System.out.println("Migliorato del: " + ((double) 100 / temp * (temp - tiles.size()))); //CALCOLO DI QUANTO E' MIGLIORATO IL LIVELLO (%)

        memoria.nextSound();
        while (sound) {
            /*try {
                sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
        memoria.getSound().loop();
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
    public void creaLivello(int x0, int y0, Type type, Type unlockable, String tile) {
        x0 *= SuperMario.standardWidth;
        y0 *= SuperMario.standardHeight;
        if (null != type) //IN BASE AD UNA IMMAGINE, SCANSIONA PIXEL PER PIXEL IL SUO CONTENUTO, E NE CREA UNA, CON LE INDICAZIONI DATOGLI DALL'IMMAGINE
        //int x0 = x * SuperMario.standardWidth;
        //int y0 = y * SuperMario.standardHeight;
        {
            switch (type) {
                case SOLID:
                    tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, memoria.getTiles(), true, tile, false));   //SE E NERO E' NORMALE SOLIDO
                    break;
                case PLAYER:
                    player.add(new Player(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, unlockable));   //SE PIXEL BLU è UN PLAYER
                    break;
                case COIN:
                    tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, memoria.getUnlockable(), false, tile, false)); //SE E GIALLO E' UNA MONETA
                    break;
                case MUSHROOM:
                    tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, memoria.getUnlockable(), false, tile, false)); //SE E ROSSO E' UN FUNGO
                    break;
                case MUSHROOMPLATFORM:
                    tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, true, tile)); //SE E ROSSO E' UN FUNGO
                    break;
                case UNLOCKABLE:
                    String unlock = unlockable.name();

                    Type getUnlock = Type.valueOf(unlock.substring(unlock.lastIndexOf("£") + 1, unlock.length()));
                    tiles.add(new CoinBlock(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, memoria.getTiles(), getUnlock, true, tile));
                    break;
                case SPINE:
                    tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, memoria.getTiles(), true, tile, true));
                    break;
                case TARTOSSO:
                    enemy.add(new Tartosso(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, true));
                    break;
                case KOOPA:
                    enemy.add(new Enemy(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, true));
                    break;
                case TUBO_RED:
                    tiles.add(new Solid(x0, y0 - SuperMario.standardHeight, SuperMario.standardWidth * 2, SuperMario.standardHeight * 2, this, type, memoria.getTiles(), true, tile, false));
                    break;
                case TUBO_RED_DOWN:
                    tiles.add(new Solid(x0, y0 - SuperMario.standardHeight, SuperMario.standardWidth * 2, SuperMario.standardHeight * 2, this, type, memoria.getTiles(), true, tile, false));
                    break;
                case PIRANHAPLANT:
                    enemy.add(new Plant(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, false));
                    break;
                case GROUND:
                    tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, true, tile));   //SE E NERO E' NORMALE SOLIDO
                    break;
                case CHECKPOINT:
                    tiles.add(new Checkpoint(x0, y0 - SuperMario.standardHeight, SuperMario.standardWidth * 2, SuperMario.standardHeight * 2, this, type, memoria.getTiles(), false, tile));   //SE E NERO E' NORMALE SOLIDO
                    break;
                case VOID:
                    tiles.add(new Checkpoint(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, memoria.getTiles(), true, tile));   //SE E NERO E' NORMALE SOLIDO
                    break;
                case CHAINCHOMP:
                    enemy.add(new Enemy(x0, y0 - SuperMario.standardHeight, SuperMario.standardWidth * 2, SuperMario.standardHeight * 2, this, type, false));
                    break;
                case BOO:
                    enemy.add(new Boo(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, false));
                    break;
                case FLAG:
                    tiles.add(new Checkpoint(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, memoria.getTiles(), false, tile));   //SE E NERO E' NORMALE SOLIDO
                    break;
                case ROD:
                    int x1 = (int) (SuperMario.standardWidth - (SuperMario.standardWidth / SuperMario.adaptWidth(2.1)));
                    tiles.add(new Checkpoint(x0 + x1, y0 * SuperMario.standardHeight, SuperMario.standardWidth / 2, SuperMario.standardHeight, this, type, memoria.getTiles(), false, tile));   //SE E NERO E' NORMALE SOLIDO
                    break;
                case DARK:
                    tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, true, tile));   //SE E NERO E' NORMALE SOLIDO
                    break;
                case SNOW:
                    tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, true, tile));   //SE E NERO E' NORMALE SOLIDO
                    break;
                case COLUMNSNOW:
                    tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, true, tile));   //SE E NERO E' NORMALE SOLIDO
                    break;
                case DESERT:
                    tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, true, tile));   //SE E NERO E' NORMALE SOLIDO
                    break;
                case COLUMNDESERT:
                    tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, true, tile));   //SE E NERO E' NORMALE SOLIDO
                    break;
                case ICE:
                    tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, true, tile));   //SE E NERO E' NORMALE SOLIDO
                    break;
                case COLUMNICE:
                    tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, true, tile));   //SE E NERO E' NORMALE SOLIDO
                    break;
                case UP1:
                    tiles.add(new Solid(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, type, memoria.getTiles(), false, tile, false));   //SE E NERO E' NORMALE SOLIDO
                    break;
                default:
                    break;
            }
            /*else if (find.compareTo(BOSS)) {
                    enemy.add(new Boss(x0, y0, SuperMario.standardWidth, SuperMario.standardHeight, this, Type.KOOPA, true));
                }*/
        } else {
            System.err.println("Questa animazione non e' valida: manca il Type di appartenenza");
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
            if (tiles.get(i).getType() == Type.SOLID || tiles.get(i).getType() == Type.COIN || tiles.get(i).getType() == Type.MUSHROOM || tiles.get(i).getType() == Type.UNLOCKABLE || tiles.get(i).getType() == Type.UNLOCKABLE2 || tiles.get(i).getType() == Type.UNLOCKABLE3) {
                continue;
            }
            for (int j = i + 1; j < tiles.size(); j++) {
                if (tiles.get(j).getType() == Type.SOLID || tiles.get(j).getType() == Type.COIN || tiles.get(j).getType() == Type.MUSHROOM || tiles.get(j).getType() == Type.UNLOCKABLE || tiles.get(j).getType() == Type.UNLOCKABLE2 || tiles.get(j).getType() == Type.UNLOCKABLE3) {
                    continue;
                }
                if (tiles.get(i).getType() == tiles.get(j).getType() && tiles.get(i).getX() + (tiles.get(i).getWidth() * tiles.get(i).getSerie()) == tiles.get(j).getX() && tiles.get(i).getY() == tiles.get(j).getY()) {
                    tiles.get(i).moreSerie();
                    tiles.remove(j);
                }
            }
        }

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
}
