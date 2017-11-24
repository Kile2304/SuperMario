package mario.rm.input;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import mario.MainComponent;
import mario.rm.identifier.Type;
import mario.rm.utility.DefaultFont;
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
            Log.append(path, DefaultFont.DEBUG);
            //if (MainComponent.jar != null && MainComponent.jar.isFile()) {
                img = ImageIO.read(MainComponent.class.getClassLoader().getResourceAsStream(path));
            /*}else{
                File f = null;
                if(!path.substring(0, 4).equals("src/")){
                    f = new File("src/"+path);
                    img = ImageIO.read(f);
                }else{
                    f = new File(path);
                    img = ImageIO.read(f);
                }
                Log.append(f.getAbsolutePath(), DefaultFont.INFORMATION);
            }*/
        } catch (IOException ex) {
            //System.out.println("Immagine alla posizione: " + (path) + " non caricata correttamente!");
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }
        return img;
    }

    /**
     *
     * @param path: path completa ell'immagine da caricare in memoria
     * @return immagine caricata in memoria
     */
    public static final BufferedImage LoadImageCompletePath(String path) {
        BufferedImage img = null;
        try {
            //if (MainComponent.jar != null && MainComponent.jar.isFile()) {
                //path = path.substring(path.indexOf("mario/"));
                Log.append(path,DefaultFont.INFORMATION);
                img = ImageIO.read(MainComponent.class.getClassLoader().getResourceAsStream(path));
            //}else{
                //File f = new File(path);
                /*if(f.exists()){
                    System.out.println("porco dio");
                }*/
                //img = ImageIO.read(new File(path));
                //Log.append(f.getAbsolutePath(), DefaultFont.INFORMATION);
            //}
        } catch (IOException ex) {
            //System.out.println("Immagine alla posizione: " + new File(path).getAbsoluteFile() + " non caricata correttamente!");
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
        Log.append("4)CREO IL LIVELLO",DefaultFont.INFORMATION);


        if (path.equals("")) {
            return false;
        }
        try {
            Object fr = null;
            /*if(MainComponent.jar.isFile()){
                path = path.replace("src/", "");
                Log.append(path);*/
            System.out.println(""+path);
                fr = new InputStreamReader(MainComponent.class.getClassLoader().getResourceAsStream(path));
            /*}else{
                fr = new FileReader(path);
            }*/
            Log.append(path);
            BufferedReader br = new BufferedReader((java.io.Reader) fr);

            String line = "";
            ArrayList<Integer> punto = new ArrayList<>();
            Type type = null;
            Type unlockable = null;
            
            while ((line = br.readLine()) != null) {  //da modificare, in modo che rimanga solo int x, int y e Type, dopo diche fare for che scorre tutti gli anim...
                String word = "";
                type = null;
                unlockable = null;
                String tile = "";
                punto.clear();
                for (int i = 0; i < line.length(); i++) {
                    switch (line.charAt(i)) {
                        case '{':
                        case '[':
                        case '|':
                        case '<':
                            word = "";
                            break;
                        case '>':
                            //System.out.println(""+word);
                            punto.add(Integer.parseInt(word));
                            word = "";
                            break;
                        case ']':
                            //System.out.println(""+word);
                            if (type == null) {
                                Log.append("refactor: "+word, DefaultFont.INFORMATION);
                                type = Type.valueOf(word);
                            } else {
                                unlockable = type;
                                String t = type.name();
                                type = Type.valueOf(t.substring(0, t.lastIndexOf("_")));
                            }
                            word = "";
                            break;
                        case '!':
                            //System.out.println(""+word);
                            tile = word;
                            word = "";
                            break;
                        default:
                            word += line.charAt(i);
                            break;
                    }
                }
                int x0 = punto.get(0);
                int y0 = punto.get(1);
                read.creaLivello(x0, y0, type, unlockable, tile);
            }

        } catch (FileNotFoundException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        } catch (IOException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }

        return true;
    }

}
