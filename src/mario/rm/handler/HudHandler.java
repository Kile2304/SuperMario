package mario.rm.handler;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import mario.rm.input.Loader;

/**
 *
 * @author LENOVO
 */
public final class HudHandler {
    
    public static final BufferedImage hud = (Loader.LoadImage("Immagini/hud.png")); //FOTO DELLE SCRITTE
    
    public static final BufferedImage[] toHud(int numero) {
        ArrayList<BufferedImage> temp = new ArrayList<>();
        if (numero == 0) {    //nel caso fosse 0 ritorna solo questa immagine
            return new BufferedImage[]{hud.getSubimage(82, 50, 7, 7)};
        }
        while (numero != 0) {
            int found = numero % 10;
            numero /= 10;
            found--;
            found = found < 0 ? 9 : found;
            BufferedImage img = hud.getSubimage(9 * found + 2, 50, 7, 7);
            temp.add(img);
        }
        BufferedImage[] converted = new BufferedImage[temp.size()];
        for (int i = 0; i < converted.length; i++) {
            converted[i] = temp.get(i);
        }
        return converted;
    }
    @Deprecated
    public static final BufferedImage[] toHud(String text){
        return null;
    }

}
