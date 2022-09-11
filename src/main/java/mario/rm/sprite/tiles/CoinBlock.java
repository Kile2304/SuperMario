package mario.rm.sprite.tiles;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import mario.rm.SuperMario;
import mario.rm.Animation.Tile;
import mario.rm.handler.Handler;
import mario.rm.identifier.TilePart;
import mario.rm.input.MemoriaAC;

/**
 *
 * @author LENOVO
 */
/**
 *
 * CLASSE CHE INDICA UN QUALCHE OGGETTO CHE COLPENDOLO TIRERA' FUORI UN ITEM
 */
public class CoinBlock extends Tiles {

    /**
     *
     * @return TIPO DI ITEM CHE VERRA' RILASCIATO
     */
    private final String unlock;

    private int numero;

    private long timeForReUnlock;

    public CoinBlock(int x, int y, int width, int height, Handler handler, String type, ArrayList<Tile> anim, String unlock, boolean collide, String part, int numero, String script) {
        super(x, y, width, height, handler, type, anim, collide, part, false, script);
        this.unlock = unlock;
        this.numero = numero;
    }

    /**
     * Sblocco item nascosto nel tile
     */
    @Override
    public void unlockable() {
        if (timeForReUnlock + 500 < System.currentTimeMillis()) {
            System.out.println("" + unlock);
            timeForReUnlock = System.currentTimeMillis();
            if (unlock.equals("MUSHROOM") || unlock.equals("UP1")) {
                new Thread() {
                    @Override
                    public void run() {
                        int i = 0;
                        while (i < SuperMario.playerNumber) {
                            //handler.addTiles(new GravityTile(x, y - SuperMario.standardHeight, SuperMario.standardWidth, SuperMario.standardHeight, handler, unlock, handler.getMemoria().getUnlockable(), true, TilePart.UPLEFT.name(), false)); //AGGIUNGE UNA MONETA
                            //handler.addTiles(new Solid(x, y - SuperMario.standardHeight, SuperMario.standardWidth, SuperMario.standardHeight, handler, unlock, handler.getMemoria().getUnlockable(), true, TilePart.UPLEFT.name(), false)); //AGGIUNGE UNA MONETA
                            handler.addTiles(new Solid(x, y - SuperMario.standardHeight, SuperMario.standardWidth, SuperMario.standardHeight, handler,
                                unlock, MemoriaAC.getUnlockable(), false, TilePart.UPLEFT.name(), false, "M GravityTile")); //SE E GIALLO E' UNA MONETA
                            handler.addPosition();
                            i++;
                            try {
                                sleep(500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(CoinBlock.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        numero = 1;
                    }
                }.start();
            } else {
                handler.addTiles(new Solid(x, y - SuperMario.standardHeight, SuperMario.standardWidth, SuperMario.standardHeight, handler, unlock, MemoriaAC.getUnlockable(), true, TilePart.UPLEFT.name(), false, "")); //AGGIUNGE UNA MONETA
            }
            if (numero == 1) {
                type = "UNLOCKED";

                ArrayList<Tile> tempo = MemoriaAC.getUnlockable();
                tempo.stream().filter((tile1) -> (tile1.getType().equals(type))).forEach((tile1) -> {
                    temp = tile1.getImage(TilePart.UPLEFT);
                });
            }
            numero--;
        }
    }

}
