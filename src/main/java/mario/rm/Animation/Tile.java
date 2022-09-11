package mario.rm.Animation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import mario.rm.identifier.TilePart;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class Tile implements Serializable, Animated {

    transient private HashMap<BufferedImage[], String> image;

    private String tile;

    private String path;

    private String type;

    private String unlock;

    private int index;

    public Tile(BufferedImage[] image, String tile, String path, String type) {
        this.image = new HashMap<>();
        this.tile = tile;
        this.image.put(image, tile);

        this.path = path;
        this.type = type;
        index = -1;
    }

    public Tile(BufferedImage[] image, String tile, String path, String type, String unlock) {
        this.image = new HashMap<>();
        this.tile = tile;
        this.image.put(image, tile);

        this.path = path;
        this.type = type;
        this.unlock = unlock;
        index = -1;
    }

    public void addAnimation(BufferedImage[] ima, TilePart ti) {
        image.put(ima, ti.name());
    }

    public String getTile() {
        return tile;
    }

    public Set<Entry<BufferedImage[], String>> getSetImage() {
        return image.entrySet();
    }

    public MultiAnim getImage(MultiAnim index) {
        Set<Entry<BufferedImage[], String>> s = image.entrySet();
        for (Entry<BufferedImage[], String> entry : s) {
            if (entry.getValue().equals(TilePart.UPLEFT.name())) {
                if (!(entry.getKey().length > index.getIndex())) {
                    index.setIndex(0);
                }
                index.setImg(entry.getKey()[index.getIndex()]);
                index.setIndex(index.getIndex() + 1);
            }
        }
        return index;
    }

    public BufferedImage[] getImage(TilePart ti) {
        /*if (index < image.length) {
            index++;
        } else {
            index = 0;
        }
        return image[index];*/

        Set<Entry<BufferedImage[], String>> s = image.entrySet();
        for (Entry<BufferedImage[], String> entry : s) {
            if (entry.getValue().equals(ti.name())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getPath() {
        return path;
    }

    public String getUnlock() {
        return unlock;
    }

    private void writeObject(ObjectOutputStream out) {
        try {
            out.defaultWriteObject();

            Set<Entry<BufferedImage[], String>> s = image.entrySet();

            out.writeInt(s.size());
            out.flush();
            //System.out.println("size: "+s.size());
            for (Entry<BufferedImage[], String> entry : s) {
                BufferedImage[] img = entry.getKey();
                int length = img.length;
                out.writeInt(length);
                out.flush();
                //System.out.println("length: "+length);
                for (int i = 0; i < length; i++) {
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    ImageIO.write(img[i], "png", buffer);

                    out.writeInt(buffer.size()); // Prepend image with byte count
                    out.flush();
                    buffer.writeTo(out);         // Write image
                    out.flush();
                    //System.out.println("buffer: "+buffer.size());
                }
                String key = entry.getValue();
                //out.write(key.length());
                out.writeUTF(key);
                out.flush();
            }
        } catch (ArrayIndexOutOfBoundsException | IOException e) {
            Log.append(Log.stackTraceToString(e), DefaultFont.ERROR);
        }
        //System.out.println("finito");

        //System.out.println("finito");
    }

    private void readObject(ObjectInputStream in) {
        try {
            in.defaultReadObject();

            image = new HashMap<>();

            int sizeHash = in.readInt();
            //System.out.println("size: "+sizeHash);
            for (int j = 0; j < sizeHash; j++) {
                int imageCount = in.readInt(); // Read byte count
                //System.out.println("length: "+imageCount);
                BufferedImage[] img = new BufferedImage[imageCount];
                for (int i = 0; i < imageCount; i++) {
                    int size = in.readInt();
                    //System.out.println("buffer: "+size);
                    byte[] buffer = new byte[size];
                    in.readFully(buffer); // Make sure you read all bytes of the image

                    img[i] = (ImageIO.read(new ByteArrayInputStream(buffer)));
                }
                String key = in.readUTF();
                image.put(img, key);
            }
        } catch (IOException | ClassNotFoundException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }
    }

    public String getType() {
        return type;
    }

    @Override
    public BufferedImage getBgImage(String id) {
        BufferedImage[] img = getImage(TilePart.valueOf(id));
        if (img != null) {
            return img[0];
        } else {
            return null;
        }
    }

    public void adapt(int width, int height) {
        Set<Entry<BufferedImage[], String>> s = image.entrySet();
        for (Entry<BufferedImage[], String> entry : s) {
            BufferedImage[] img = entry.getKey();
            for (int i = 0; i < img.length; i++) {
                BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = resized.createGraphics();
                g2d.drawImage(img[i], 0, 0, width, height, null);
                img[i] = resized;
            }
        }
        s = null;
    }

}
