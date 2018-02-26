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
import mario.rm.utility.DefaultFont;
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
        }
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
        
        //System.out.println(""+path + "\\" + cart);
        System.out.println(""+p1);
        p1 = "Immagini\\extract"+path + "\\" + p1.substring(p1.lastIndexOf("\\") + 1);
        Cut cut = null;
        if (tile == null) {
            cut = new Cut(p1, coord, move, type, direction, path + "\\" + cart, transformation);
        } else if (unlock == null) {
            cut = new Cut(p1, coord, null, type, null, path + "\\" + cart, tile);
        } else {
            cut = new Cut(p1, coord, null, type, null, path + "\\" + cart, tile, unlock);
        }

        if (cut.getErrore()) {
            return;
        }

        //File f = new File("src\\mario\\res\\Animazioni" + path + "\\" + cart);
        File f = new File("res\\Animazioni" + path + "\\" + cart);
        if (!f.exists()) {
            f.mkdir();
        }

        //System.out.println(""+new File("src\\dragon\\ball\\res\\animazioni"+path+"\\"+cart+"\\"+nomeFile).getAbsolutePath());
        //System.out.println(""+path);
        try {
            //fos = new FileOutputStream(new File("src\\mario\\res\\Animazioni" + path + "\\" + cart + "\\" + nomeFile).getAbsolutePath());
            fos = new FileOutputStream(new File("res\\Animazioni" + path + "\\" + cart + "\\" + nomeFile).getAbsolutePath());
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
