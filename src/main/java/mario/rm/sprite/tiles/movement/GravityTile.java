package mario.rm.sprite.tiles.movement;

import static mario.rm.SuperMario.adaptHeight;

import java.util.List;

import mario.rm.SuperMario;
import mario.rm.handler.Handler;
import mario.rm.identifier.Direction;
import mario.rm.identifier.Tipologia;
import mario.rm.sprite.tiles.Tiles;
import mario.rm.utility.MoveAttrib;
import mario.rm.utility.Punto;

/**
 *
 * @author LENOVO
 */
public class GravityTile implements Azione {

    private Handler handler;
    private Tiles tileC;

    private boolean falling;
    private boolean jumping;

    private double gravity;

    private int velX;
    private int velY;

    private static final double STACCO = adaptHeight(0.17);

    public GravityTile(Handler handler, Tiles tile) {
        this.handler = handler;
        this.tileC = tile;

        velX = Tipologia.getValue(tileC.getType(), "velY");  //VEDO A CHE VELOCITA' E' CONSENTITO ANDARE A QUEL TIPO DI NEMICO
        if (velX == 0) {
            velX = 2;
        }
    }

    @Override
    public MoveAttrib tick(MoveAttrib att) {
        int direzione = att.getDir()[0] == Direction.LEFT ? -1 : 1;
        int direzioneY = att.getDir()[1] == Direction.DOWN ? -1 : 0;

        Punto p = att.getLast();
        
        p.x += velX * direzione;
        p.y += velY * direzioneY;

        //System.out.println("" + x);
        falling = true;

        List<Tiles> tile = handler.getTiles();
        for (int i = 0; i < handler.getTiles().size(); i++) {
            if (tile.get(i) != tileC && tileC.getBounds().intersects(tile.get(i).getBounds())) {
                if (tile.get(i).getType().equals("VOID")) {
                    tileC.die();
                } else if (!tile.get(i).getType().equals("COIN")) {
                    if (tileC.getBoundsBottom().intersects(tile.get(i).getBounds())) { //INTERSEZIONE PARTE BASSA
                        p.y = tile.get(i).getY() - tileC.getHeight() + 1;//LA SUA POSIZIONE DIVIENE POSIZIONE TILE (Y) MENO L'ALTEZZA

                        if (!jumping) {
                            falling = false;
                            gravity = 0;
                            velY = 0;
                        }
                    }
                    if (tileC.getBoundsRight().intersects(tile.get(i).getBounds())) {  //INTERSEZIONE PARTE DESTRA
                        p.x = tile.get(i).getX() - tileC.getWidth(); //LA POSIZIONE IN X DIVENTA LA X DEL TILE MENO LA LARGHEZZA
                        direzione *= -1;
                    }
                    if (tileC.getBoundsLeft().intersects(tile.get(i).getBounds())) {   //INTERSEZIONE PARTE SINISTRA (DOVREBBE ESSERE PERFETTO)
                        p.x = tile.get(i).getX() + tile.get(i).getWidth() - SuperMario.adaptWidth(20); //LA POSIZIONE IN X DIVENTA LA X DEL TILE MENO LA LARGHEZZA del tile
                        direzione *= -1;
                    }
                }
            }
        }
        if (falling) {    //SE STA CADENDO
            direzioneY = -1;
            gravity -= STACCO; //AUMENTA LA GRAVITA
            velY = ((int) gravity); //LA VELOCITA E PARI ALLA GRAVITA
        } else {
            direzioneY = 0;
        }
        
        att.setLast(p);
        Direction horizontal = direzione == 1 ? Direction.RIGHT : Direction.LEFT;
        Direction vertical = direzione == -1 ? Direction.DOWN : Direction.UP;
        //System.out.println("or: "+horizontal+" ver: "+vertical+" velY: "+velY);
        att.setDir(new Direction[]{horizontal, vertical});
        return att;

    }

    @Override
    public int getVelX() {
        return velX;
    }

    @Override
    public int getVelY() {
        return velY;
    }

}
