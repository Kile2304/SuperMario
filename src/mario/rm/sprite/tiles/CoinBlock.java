package mario.rm.sprite.tiles;

import java.util.ArrayList;
import mario.rm.Animation.Tile;
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
 * @return CLASSE CHE INDICA UN QUALCHE OGGETTO CHE COLPENDOLO TIRERA' FUORI UN
 * ITEM
 */
public class CoinBlock extends Tiles {

    /**
     *
     * @return TIPO DI ITEM CHE VERRA' RILASCIATO
     */
    private final Type unlock;

    public CoinBlock(int x, int y, int width, int height, Handler handler, Type type, ArrayList<Tile> anim, Type unlock, boolean collide, String part) {
        super(x, y, width, height, handler, type, anim, collide, part, false);
        this.unlock = unlock;
    }

    @Override
    public void tick() {

    }

    /**
     *
     * @return RILASCIA L'ITEM E TRASFORMA IL BLOCCO ATTUALE IN UN BLOCCO
     * DIVERSO
     */
    @Override
    public void unlockable() {
        handler.addTiles(new Solid(x, y - 64, SuperMario.standardWidth, SuperMario.standardHeight, handler, unlock, handler.getMemoria().getUnlockable(), true, TilePart.UPLEFT.name(), false)); //AGGIUNGE UNA MONETA
        type = Type.SOLIDFIRE;

        /*handler.getMemoria().getUnlockable().stream().filter((animazione) -> (animazione.getType() == type && animazione.getMove() == Move.WALK)).forEach((animazione) -> {
            this.anim = new Animazione(animazione);
        });*/
        ArrayList<Tile> tempo = handler.getMemoria().getUnlockable();
        tempo.stream().filter((tile1) -> (tile1.getType() == unlock)).forEach((tile1) -> {
            temp = tile1.getImage(TilePart.valueOf(type.name()));
        });
    }

}
