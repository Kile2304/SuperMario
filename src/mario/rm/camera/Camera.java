package mario.rm.camera;

import mario.rm.SuperMario;
import mario.rm.sprite.Sprite;

/**
 *
 * @author LENOVO
 */
public class Camera {   //QUESTA CLASSE SERVE PER L'INQUADRATURA

    private int x;  //COORDINATE X FINESTRA
    private int y;  //COORDINATE Y FINESTRA
    private static int upY;

    public Camera(Sprite p) {
        System.out.println("7)INIZIALIZZO LA TELECAMERA");
        x = -p.getX() + SuperMario.WIDTH / 2;  //MI CENTRA LE COORDINATE X RISPETTO AL PLAYER
        y = -p.getY() + SuperMario.HEIGHT - SuperMario.standardHeight * 3 - p.getHeight(); //MI CENTRA LE COORDINATE Y RISPETTO AL PLAYER
        upY = y;
    }

    public void tick(Sprite p) {
        x = -p.getX() + SuperMario.WIDTH / 2;  //MI CENTRA LE COORDINATE X RISPETTO AL PLAYER
        //y = -p.getY() + SuperMario.HEIGHT - SuperMario.standardHeight * 3 - p.getHeight(); //MI CENTRA LE COORDINATE Y RISPETTO AL PLAYER
        /*if ((upY - y >= -SuperMario.HEIGHT / 2) && (upY - y) <= SuperMario.standardHeight) {
            y = upY;
        } else {
            y -= SuperMario.HEIGHT / 2 - SuperMario.standardHeight;
        }*/
        y = -p.getY() + SuperMario.HEIGHT / 2 - p.getHeight() / 2;
    }

    public static void setUpY(int y) {
        upY = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
