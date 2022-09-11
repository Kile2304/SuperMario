package mario.rm.input;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import mario.MainComponent;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class Loader {

    /**
     *
     * @param path: path parziale dell'immagine da caricare in memoria
     * @return l'immagine caricata in memoria
     */
    public static final BufferedImage LoadImage(String path) {    //MEMORIZZA NEL BUFFERIMAGE L'IMMAGINE
        BufferedImage img = null;
        try {
            Log.append(path, DefaultFont.INFORMATION);
            img = ImageIO.read(MainComponent.class.getClassLoader().getResourceAsStream(path));
        } catch (IOException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }
        return img;
    }
    
    public static BufferedImage LoadImageChange(String path, boolean jarNeeded){
        BufferedImage img = null;
        try {
            Log.append(path, DefaultFont.INFORMATION);
            img = ImageIO.read(jarNeeded
                    ? MainComponent.class.getClassLoader().getResourceAsStream(path.replace('\\', '/'))
                    : new FileInputStream(new File(path)));
        } catch (IOException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }
        return img;
    }
    
    public static final BufferedImage LoadImageNormal(String path) {
        BufferedImage img = null;
        try {
            Log.append(path, DefaultFont.INFORMATION);
            img = ImageIO.read(new FileInputStream(new File(path)));
        } catch (IOException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }
        return img;
    }

    /**
     *
     * @param path: path completa dell'immagine da caricare in memoria
     * @return immagine caricata in memoria
     */
    public static final BufferedImage LoadImageCompletePath(String path) {
        BufferedImage img = null;
        try {
            Log.append(path, DefaultFont.INFORMATION);
            img = ImageIO.read(MainComponent.class.getResourceAsStream(path));
        } catch (IOException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }
        return img;
    }

    /**
     *
     * @param path: percorso del file contenente il livello
     * @param read: metodo creaLivello
     * @return se ritorna falso, vuol dire che il percorso non e' valido
     */
    public static final boolean convertTextInMap(String path, Reader read) {
        Log.append("4) CREO IL LIVELLO", DefaultFont.INFORMATION);

        if (path.equals(""))
            return false;
        
        System.out.println("coversione testo in mappa di: " + path);
        String levelPath = null;
        if (path.startsWith("Luigi"))
        	levelPath = MainComponent.filePath + "\\" + path;
        else
        	levelPath = MainComponent.filePath + "\\Luigi\\" + path;

        Log.append(path);
        try (BufferedReader br = Files.newBufferedReader(Path.of(levelPath))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] complete = line.split("/");
                String[] first = complete[0].split(" ");
                int x0 = Integer.parseInt(first[0]);
                int y0 = Integer.parseInt(first[1]);
                String type = first[2];
                String partTile = first.length == 4
                        ? first[3]
                        : "";
                String unlockType = "";
                int unlockQuantity = 0;

                String movement = "";
                for (int i = 1; i < complete.length; i++) {
                    String[] second = complete[i].split(" ");
                    if (second[0].equals("U")) {
                        unlockType = second[1];
                        unlockQuantity = Integer.parseInt(second[2]);
                    } else
                        movement = complete[i];
                }
                SpriteLoad sl = new SpriteLoad(x0, y0, type, partTile, unlockType, unlockQuantity, movement);
                read.creaLivello(sl);
            }

        } catch (FileNotFoundException ex) {
        	ex.printStackTrace();
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        } catch (IOException e) {
        	e.printStackTrace();
            Log.append(Log.stackTraceToString(e), DefaultFont.ERROR);
		}

        return true;
    }

}
