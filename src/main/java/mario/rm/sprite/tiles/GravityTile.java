package mario.rm.sprite.tiles;

import static mario.rm.SuperMario.adaptHeight;

import java.util.ArrayList;
import java.util.List;

import mario.rm.SuperMario;
import mario.rm.Animation.Tile;
import mario.rm.handler.Handler;
import mario.rm.identifier.Tipologia;

/**
 *
 * @author LENOVO
 */
public class GravityTile extends Solid {

    private int direzioneY;
    private int direzione;

    private boolean falling;
    private boolean jumping;

    private static final double STACCO = adaptHeight(0.17);

    public GravityTile(int x, int y, int width, int height, Handler handler, String type, ArrayList<Tile> anim, boolean collide, String part, boolean damage) {
        super(x, y, width, height, handler, type, anim, collide, part, damage, "");

        velX = 2;  //VEDO A CHE VELOCITA' E' CONSENTITO ANDARE A QUEL TIPO DI NEMICO
        velY = Tipologia.getValue(type, "velY");  //VEDO A CHE VELOCITA' E' CONSENTITO ANDARE A QUEL TIPO DI NEMICO

        direzione = -1;

    }

    public GravityTile(int x, int y, int width, int height, Handler handler, String type, boolean collide, String part) {
        super(x, y, width, height, handler, type, collide, part, "");

        velX = 2;  //VEDO A CHE VELOCITA' E' CONSENTITO ANDARE A QUEL TIPO DI NEMICO
        velY = Tipologia.getValue(type, "velY");  //VEDO A CHE VELOCITA' E' CONSENTITO ANDARE A QUEL TIPO DI NEMICO

        direzione = -1;

    }

    @Override
    public void tick() {
        x += velX * direzione;
        y += velY * direzioneY;

        //System.out.println("" + x);
        falling = true;

        List<Tiles> tile = handler.getTiles();
        for (int i = 0; i < handler.getTiles().size(); i++) {
            if (tile.get(i) != this && getBounds().intersects(tile.get(i).getBounds())) {
                if (tile.get(i).getType().equals("VOID")) {
                    die();
                } else if (!tile.get(i).getType().equals("COIN")) {
                    if (getBoundsBottom().intersects(tile.get(i).getBounds())) { //INTERSEZIONE PARTE BASSA
                        y = tile.get(i).getY() - height + 1;//LA SUA POSIZIONE DIVIENE POSIZIONE TILE (Y) MENO L'ALTEZZA

                        if (!jumping) {
                            falling = false;
                            gravity = 0;
                            velY = 0;
                        }
                    }
                    if (getBoundsRight().intersects(tile.get(i).getBounds())) {  //INTERSEZIONE PARTE DESTRA
                        x = tile.get(i).getX() - width; //LA POSIZIONE IN X DIVENTA LA X DEL TILE MENO LA LARGHEZZA
                        direzione *= -1;
                    }
                    if (getBoundsLeft().intersects(tile.get(i).getBounds())) {   //INTERSEZIONE PARTE SINISTRA (DOVREBBE ESSERE PERFETTO)
                        x = tile.get(i).getX() + tile.get(i).getWidth() - SuperMario.adaptWidth(20); //LA POSIZIONE IN X DIVENTA LA X DEL TILE MENO LA LARGHEZZA del tile
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

    }

}
