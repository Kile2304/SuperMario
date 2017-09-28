package mario.rm.Animation;

import mario.rm.identifier.Move;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import mario.rm.identifier.Direction;
import mario.rm.identifier.Type;

/**
 *
 * @author LENOVO
 */
public class Anim implements Serializable, Animated {

    transient private BufferedImage[] run;
    transient private BufferedImage[] die;
    transient private BufferedImage[] jump;
    transient private BufferedImage[] walk;
    transient private BufferedImage[] up;
    transient private BufferedImage[] stand;
    private Anim transformation;

    private Move lastMove;
    private Direction lastDirection;

    private Type type;

    private String path;

    transient private int index;

    public Anim(Type type, String path) {
        this.type = type;
        lastMove = Move.STAND;
        lastDirection = Direction.DOWN;
        this.path = path;
    }

    public void addAnimation(BufferedImage[] anim, Move move, Direction direction) {
        BufferedImage[] find = null;    //non funziona, da RIFARE
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(move.name().toLowerCase())) {
                try {
                    field.set(this, animation(anim,direction, (BufferedImage[]) field.get(this)));
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Anim.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //Piu avanti da provare il metodo fatto in getImage();
    }

    private BufferedImage[] animation(BufferedImage[] anim, Direction direction, BufferedImage[] dest) {
        if (dest == null) {
            dest = new BufferedImage[anim.length * 4];
            System.out.println("nuovo");
        }

        System.arraycopy(anim, 0, dest, dest.length / 4 * direction.getMoltiplier(), anim.length);

        return dest;
    }

    public BufferedImage getImage(Move move, Direction dir) {
        BufferedImage img = null;

        BufferedImage[] find = null;    //non funziona, da RIFARE
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(move.name().toLowerCase())) {
                try {
                    find = (BufferedImage[]) field.get(this);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Anim.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (find != null) {
            img = getImage(move, dir, find);
        }
        return img;
    }

    private BufferedImage getImage(Move move, Direction dir, BufferedImage[] anim) {
        if (lastMove != move || dir != lastDirection) {
            index = -1;
            lastMove = move;
            lastDirection = dir;
        }
        if (index < anim.length / 4 - 1) {
            index++;
        } else {
            index = 0;
        }

        return anim[anim.length / 4 * dir.getMoltiplier() + index];
    }

    @Override
    public Type getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        
        write(walk, out);
        write(die, out);
        write(jump, out);
        write(run, out);
        write(up, out);
        write(stand, out);
        //out.writeObject(transformation);

    }

    private void write(BufferedImage[] anim, ObjectOutputStream out) throws IOException {
        if (anim != null) {
            out.writeInt(anim.length); // how many images are serialized?

            for (int j = 0; j < anim.length; j++) {
                if (anim[j] != null) {
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    ImageIO.write(anim[j], "png", buffer);
                    out.writeInt(buffer.size()); // Prepend image with byte count
                    buffer.writeTo(out);         // Write 
                } else {
                    out.writeInt(-1);
                }
            }
        } else {
            out.writeInt(-1);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        walk = read(in);
        die = read(in);
        jump = read(in);
        run = read(in);
        up = read(in);
        stand = read(in);

        //transformation = (Anim) in.readObject();
    }

    private BufferedImage[] read(ObjectInputStream in) throws IOException {
        int imageCount = in.readInt();
        if (imageCount == -1) {
            return null;
        }
        BufferedImage[] anim = new BufferedImage[imageCount];

        for (int i = 0; i < anim.length; i++) {
            int size = in.readInt(); // Read byte count
            if (size != -1) {
                byte[] buffer = new byte[size];
                in.readFully(buffer); // Make sure you read all bytes of the image

                anim[i] = (ImageIO.read(new ByteArrayInputStream(buffer)));
            }
        }
        return anim;
    }

    public Anim getSuper() {
        if (transformation == null) {
            transformation = new Anim(type, path);
        }
        return transformation;
    }

    public boolean isEndDie() {
        if (index < die.length) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String getTile() {
        return Move.WALK.name();
    }

    @Override
    public BufferedImage getBgImage(String id) {
        Move m = null;
        if (id.equals("")) {
            m = Move.WALK;
        } else {
            return null;
        }

        return getImage(m, Direction.RIGHT);
    }

}
