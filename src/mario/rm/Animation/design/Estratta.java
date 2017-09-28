package mario.rm.Animation.design;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mario.rm.Animation.Cut;
import mario.rm.identifier.Move;
import mario.rm.utility.Punto;
import mario.rm.identifier.Type;

/**
 *
 * @author LENOVO
 */
public class Estratta {

    private String path;

    private ArrayList<Punto> coord;

    private Type type;
    private Move move;

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

    public void output() {
        Cut cut = new Cut(path, coord, move, type);
        ObjectOutputStream out = null;
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(new File("bullet.anim").getAbsolutePath());
            out = new ObjectOutputStream(fos);

            out.writeObject(cut);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Estratta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Estratta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
