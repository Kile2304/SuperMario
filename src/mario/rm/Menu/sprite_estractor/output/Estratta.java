package mario.rm.Menu.sprite_estractor.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mario.rm.Animation.Cut;
import mario.rm.identifier.Direction;
import mario.rm.identifier.Move;
import mario.rm.identifier.Type;
import mario.rm.utility.Punto;

/**
 *
 * @author LENOVO
 */
public class Estratta {

    private String path;

    private ArrayList<Punto> coord;

    private Type type;
    private Move move;
    private Direction direction;
    private String nomeFile;
    private String transformation;
    private String tile;
    Type unlock;

    public Estratta() {
        coord = new ArrayList<>();
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setType(Type type) {
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

    public void setUnlock(Type unlock) {
        this.unlock = unlock;
    }

    public Type getType() {
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
                path = this.path.substring(this.path.lastIndexOf("\\terrain\\", this.path.lastIndexOf("\\")));
                path = path.substring(0, path.lastIndexOf("\\"));
            }
        }

        //System.out.println("estratta path: "+path);
        ObjectOutputStream out = null;
        FileOutputStream fos = null;

        String cart = "";
        try {
            cart = nomeFile.substring(0, nomeFile.lastIndexOf("-"));
        } catch (StringIndexOutOfBoundsException e) {
            cart = nomeFile.substring(0, nomeFile.lastIndexOf("."));
        }
        Cut cut = null;

        if (tile == null) {
            cut = new Cut(p1, coord, move, type, direction, path + "\\" + cart, transformation);
        } else if (unlock == null) {
            cut = new Cut(p1, coord, null, type, null, path + "\\" + cart, tile);
        } else {
            cut = new Cut(p1, coord, null, type, null, path + "\\" + cart, tile, unlock);
        }

        if(cut.getErrore()){
            return;
        }
        
        File f = new File("src\\mario\\res\\Animazioni" + path + "\\" + cart);
        if (!f.exists()) {
            f.mkdir();
        }

        //System.out.println(""+new File("src\\dragon\\ball\\res\\animazioni"+path+"\\"+cart+"\\"+nomeFile).getAbsolutePath());
        //System.out.println(""+path);
        try {
            fos = new FileOutputStream(new File("src\\mario\\res\\Animazioni" + path + "\\" + cart + "\\" + nomeFile).getAbsolutePath());
            out = new ObjectOutputStream(fos);

            out.writeObject(cut);
            out.close();
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Estratta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Estratta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
