package mario.rm.sprite.tiles;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import mario.rm.Animation.Tile;
import mario.rm.SuperMario;
import static mario.rm.SuperMario.adaptHeight;
import mario.rm.handler.Handler;
import mario.rm.sprite.enemy.Enemy;

/**
 *
 * @author LENOVO
 */
public class Energy extends Tiles {

    int direzione;
    int direzioneY;

    boolean falling;
    boolean jumping;

    private static final double STACCO = adaptHeight(0.40); //velocita di risalita

    public Energy(int x, int y, int width, int height, Handler handler, String type, ArrayList<Tile> anim, boolean collide, String part, boolean damage, String script, int direzione) {
        super(x, y, width, height, handler, type, anim, collide, part, damage, script);
        velX = 3;
        this.direzione = direzione;
    }

    @Override
    public void tick() {
        x += velX * direzione;
        y += velY * direzioneY;
        //System.out.println("" + x);
        falling = true;
        LinkedList<Tiles> tile = handler.getTiles();
        for (int i = 0; i < tile.size(); i++) {
            if (tile.get(i) != this && getBounds().intersects(tile.get(i).getBounds())) {

                if (tile.get(i).getBounds().intersects(getBoundsRight())) {
                    die();
                } else if (getBoundsLeft().intersects(tile.get(i).getBounds())) {  //INTERSEZIONE PARTE DESTRA
                    die();
                } else if (getBoundsBottom().intersects(tile.get(i).getBounds())) {  //INTERSEZIONE PARTE DESTRA
                    if (tile.get(i).getType().equals("VOID")) {
                        die();
                    }
                    y = tile.get(i).getY() - height - 1;//LA SUA POSIZIONE DIVIENE POSIZIONE TILE (Y) MENO L'ALTEZZA

                    jumping = true;
                    falling = false;
                    gravity = 5;    //aumenta quanto sale
                    break;
                } 
            }
        }
        LinkedList<Enemy> enemy = handler.getEnemy();
        for (int i = 0; i < enemy.size(); i++) {
            if (getBounds().intersects(enemy.get(i).getBounds())) {
                die();
                enemy.get(i).die();
            }
        }
        if (falling) {    //SE STA CADENDO
            direzioneY = -1;
            if (gravity > -5) {
                gravity -= STACCO; //AUMENTA LA GRAVITA
            }
            velY = ((int) gravity); //LA VELOCITA E PARI ALLA GRAVITA
        } else {
            direzioneY = 0;
        }

    }
    
    @Override
    public Rectangle getBounds(){
        int x0 = x  - velX * direzione;
        int y0 = y - velY * direzioneY;
        int xx = width + velX * direzione - SuperMario.adaptWidth(20);
        int yy = height + velY * direzioneY;
        return new Rectangle(x0, y0, xx, yy);
    }

    @Override
    public Rectangle getBoundsBottom() {
        int x0 = x + SuperMario.adaptWidth(10) - velX * direzione;
        int y0 = y + height - SuperMario.adaptHeight(5) - velY * direzioneY;
        int xx = width + velX * direzione - SuperMario.adaptWidth(20);
        int yy = SuperMario.adaptHeight(5) + velY * direzioneY;
        return new Rectangle(x0, y0, xx, yy);
    }

    @Override
    public Rectangle getBoundsLeft() {
        int x0 = x - velX * direzione;
        int y0 = y + SuperMario.adaptHeight(10) - velY * direzioneY;
        int xx = velX * direzione + SuperMario.adaptWidth(5);
        int yy = -SuperMario.adaptHeight(20) + velY * direzioneY + height;
        return new Rectangle(x0, y0, xx, yy);
    }

    @Override
    public Rectangle getBoundsRight() {
        int x0 = x + width - SuperMario.adaptWidth(10) - velX * direzione;
        int y0 = y + SuperMario.adaptHeight(10) - velY * direzioneY;
        int xx = velX * direzione + SuperMario.adaptWidth(5);
        int yy = -SuperMario.adaptHeight(20) + velY * direzioneY + height;
        return new Rectangle(x0, y0, xx, yy);
    }

    @Override
    public void unlockable() {
    }

}
