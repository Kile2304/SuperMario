package mario.rm.Menu.sprite_estractor.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mario.MainComponent;
import mario.rm.Animation.Cut;
import mario.rm.identifier.Direction;
import mario.rm.identifier.Move;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;
import mario.rm.utility.Punto;

/**
 *
 * @author LENOVO
 */
public class Estratta {

    private String path;

    private LinkedList<Punto> coord;

    private String type;
    private Move move;
    private Direction direction;
    private String nomeFile;
    private String transformation;
    private String tile;
    private String unlock;

    public Estratta() {
        coord = new LinkedList<>();
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public void setPunto(Punto coord, Punto coord2) {
        this.coord.add(coord);
        this.coord.add(coord2);
    }

    public void setDirection(Direction dir) {
        this.direction = dir;
    }

    public void setNomeFile(String nome) {
        nomeFile = nome;
    }

    public void setTransformation(String transformation) {
        this.transformation = transformation;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public void setUnlock(String unlock) {
        this.unlock = unlock;
    }

    public String getType() {
        return type;
    }

    public void output() {

        //System.out.println(""+path);
        String p1 = path;
        /*System.out.println(""+p1);
        try {
            path = this.path.substring(this.path.lastIndexOf("\\player\\"), this.path.lastIndexOf("\\"));
        } catch (StringIndexOutOfBoundsException e) {
            try {
                path = this.path.substring(this.path.lastIndexOf("\\tile\\", this.path.lastIndexOf("\\")));
                path = path.substring(0, path.lastIndexOf("\\"));
            } catch (StringIndexOutOfBoundsException ex) {
                try {
                    path = this.path.substring(this.path.lastIndexOf("\\terrain\\", this.path.lastIndexOf("\\")));
                    path = path.substring(0, path.lastIndexOf("\\"));
                } catch (StringIndexOutOfBoundsException exe) {
                    try {
                        path = this.path.substring(this.path.lastIndexOf("\\enemy\\", this.path.lastIndexOf("\\")));
                        path = path.substring(0, path.lastIndexOf("\\"));
                    } catch (StringIndexOutOfBoundsException exec) {
                        return;
                    }
                }
            }
        }*/
        boolean jarNeeded = true;
        try {
            path = this.path.substring(this.path.lastIndexOf("\\Immagini\\extract\\") + 1, path.length());
        } catch (StringIndexOutOfBoundsException e) {
            path = this.path.substring(this.path.lastIndexOf("\\Luigi\\resources\\") + 1, path.length());
            jarNeeded = false;
        }
        p1 = MainComponent.filePath + "/" + path;
        p1 = p1.replace('\\', '/');
        //System.out.println(""+p1);
        //path = path.replace("/src/mario/res", "");

        //System.out.println("estratta path: "+path);
        Log.append(p1, DefaultFont.INFORMATION);
        ObjectOutputStream out = null;
        FileOutputStream fos = null;
        String cart = "";
        try {
            cart = nomeFile.substring(0, nomeFile.lastIndexOf("-"));
        } catch (StringIndexOutOfBoundsException e) {
            cart = nomeFile.substring(0, nomeFile.lastIndexOf("."));
        }

        String category = "";
        String[] split = p1.split("/");
        for (int i = split.length - 1; i > 0; i--) {
            if (split[i].equals("player") || split[i].equals("tile") || split[i].equals("enemy")) {
                for (int j = i; j < split.length - 1; j++) {
                    category += split[j] + "/";
                }
                break;
            }
        }
        //System.out.println("category: "+category);

        //System.out.println(""+path + "\\" + cart);
        //System.out.println(""+p1);
        //p1 = "Immagini\\extract"+path + "\\" + p1.substring(p1.lastIndexOf("\\") + 1);
        //System.out.println(""+p1);
        String percorso = jarNeeded ? path : p1;
        System.out.println(""+percorso + " "+jarNeeded);
        Cut cut = null;
        if (tile == null) {
            cut = new Cut(percorso, coord, move, type, direction, category + "/" + cart, transformation, jarNeeded);
        } else if (unlock == null) {
            cut = new Cut(percorso, coord, null, type, null, category + "/" + cart, tile, jarNeeded);
        } else {
            cut = new Cut(percorso, coord, null, type, null, category + "/" + cart, tile, unlock, jarNeeded);
        }

        if (cut.getErrore()) {
            return;
        }

        //File f = new File("src\\mario\\res\\Animazioni" + path + "\\" + cart);
        path = p1.substring(0, p1.lastIndexOf("/"));
        File f = new File(MainComponent.filePath + "/Luigi/Animation/" + "/" + category + cart);
        //System.out.println(""+f.getAbsolutePath());
        //System.out.println("cartella: "+f.getAbsolutePath());
        if (!f.exists()) {
            f.mkdirs();
        }

        //System.out.println(""+new File("src\\dragon\\ball\\res\\animazioni"+path+"\\"+cart+"\\"+nomeFile).getAbsolutePath());
        //System.out.println(""+path);
        try {
            //fos = new FileOutputStream(new File("src\\mario\\res\\Animazioni" + path + "\\" + cart + "\\" + nomeFile).getAbsolutePath());
            fos = new FileOutputStream(new File(f.getAbsolutePath() + "\\" + nomeFile).getAbsolutePath());
            out = new ObjectOutputStream(fos);
            out.writeObject(cut);

            out.close();
            fos.close();
        } catch (FileNotFoundException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        } catch (IOException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }
    }

}
