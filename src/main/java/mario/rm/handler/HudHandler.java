package mario.rm.handler;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static mario.rm.SuperMario.standardHeight;
import static mario.rm.SuperMario.standardWidth;
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
            return new BufferedImage[]{hud.getSubimage(82, 50, 8, 8)};
        }
        while (numero != 0) {
            int found = numero % 10;
            numero /= 10;
            found--;
            found = found < 0 ? 9 : found;
            BufferedImage img = hud.getSubimage(9 * found + 2, 50, 8, 8);
            temp.add(img);
        }
        BufferedImage[] converted = new BufferedImage[temp.size()];
        for (int i = 0; i < converted.length; i++) {
            converted[i] = temp.get(i);
        }
        return converted;
    }
    
    public static void drawHud(Graphics g, BufferedImage[] img, int x, int  y, int width, int height, boolean horizontal, boolean vertical){
        for (int i = 0; i < img.length; i++) {
            g.drawImage(img[i],
                            x,
                            y,
                            width,
                            height,
                            null);
            if(horizontal)  x += width;
            if(vertical)  y += height;
        }
    }
    
    public static void inverseDrawHud(Graphics g, BufferedImage[] img, int x, int  y, int width, int height, boolean horizontal, boolean vertical){
        for (int i = img.length - 1; i >= 0; i--) {
            g.drawImage(img[i],
                            x,
                            y,
                            width,
                            height,
                            null);
            if(horizontal)  x += width;
            if(vertical)  y += height;
        }
    }
    
    @Deprecated
    public static final BufferedImage[] toHud(String text){
        ArrayList<BufferedImage> temp = new ArrayList<>();
        text = text.toLowerCase();
        //System.out.println(""+text);
        int i = 0;
        while (text.length() > i) {
            int xx = (text.charAt(i) - 'a') % 13;
            int yy = (text.charAt(i) - 'a') / 12;
            //if(xx > 0) xx--;
            //System.out.println("yy: "+yy+" xx:"+xx);
            BufferedImage img = hud.getSubimage(9 * xx + 1, 9 * yy + 1 , 8, 8);
            temp.add(img);
            i++;
        }
        //System.out.println("legnth: "+temp.size());
        BufferedImage[] converted = new BufferedImage[temp.size()];
        for (i = 0; i < converted.length; i++) {
            converted[i] = temp.get(i);
        }
        return converted;
    }

}
