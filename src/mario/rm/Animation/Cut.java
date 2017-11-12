package mario.rm.Animation;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import mario.rm.identifier.Move;
import mario.rm.utility.Punto;
import mario.rm.input.Loader;
import mario.rm.identifier.Direction;
import mario.rm.identifier.Type;
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class Cut implements Serializable {

    private static final long serialUID = 1;

    transient BufferedImage[] normal;
    transient BufferedImage[] mirror;

    private int numeroDiImmagini;

    private Move move;
    private Type type;
    private Direction dir;

    private String path;
    private String transformation;

    private String tile;

    private Type unlockable;

    private boolean errore;

    /*
    *
    *   COSTRUTTORE PER IMMAGINI CON LO STESSO NUMERO DI FIGURE PER 1 RIGA
    *
     */
    public Cut(BufferedImage original, int x, int y, int width, int height, int numeroDiImmagini, int xOffset, int yOffset, Move move, Type type, Direction dir) {

        this.move = move;
        this.type = type;
        this.dir = dir;

        ArrayList<BufferedImage> scena = new ArrayList<>();

        this.numeroDiImmagini = numeroDiImmagini;

        for (int i = 0; i < numeroDiImmagini; i++) {
            scena.add(original.getSubimage(x + (xOffset + width) * i, y + (yOffset), width, height));
        }
        normal = new BufferedImage[scena.size()];
        mirror = new BufferedImage[scena.size()];

        int i = 0;

        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        for (BufferedImage bufferedImage : scena) {
            normal[i] = bufferedImage;
            /*
            *
            * INVERTE LE IMMAGINI E LE MEMORIZZA NEL BUFFER SPECCHIO (IL CODICE SOTTO)
            *
             */
            tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-bufferedImage.getWidth(null), 0);
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            bufferedImage = op.filter(bufferedImage, null);

            mirror[i] = bufferedImage;

            i++;
        }
    }

    public Cut(String path, LinkedList<Punto> punti, Move move, Type type, Direction dir, String cart, String transformation) {

        errore = false;

        BufferedImage original = null;
        //System.out.println(""+path);
        original = new Loader().LoadImageCompletePath(path);
        this.type = type;

        this.move = move;
        this.dir = dir;

        this.path = cart;
        if (move == null && dir == null) {
            tile = transformation;
        } else {
            this.transformation = transformation;
        }

        ArrayList<BufferedImage> scena = new ArrayList<>();

        //System.out.println("" + (punti.size() / 2));
        numeroDiImmagini = punti.size();

        for (int i = 0; i < numeroDiImmagini; i += 2) {

            Punto secondoX = punti.get(0).getX() > punti.get(1).getX() ? punti.pop() : punti.get(1);
            Punto primoX = punti.size() % 2 == 0 ? punti.get(0) : punti.pop();
            if (punti.size() % 2 != 0) {
                punti.removeFirst();
            } else if (punti.size() == numeroDiImmagini - i) {
                punti.removeFirst();
                punti.removeFirst();
            }

            Punto primoY = primoX.getY() > secondoX.getY() ? primoX : secondoX;
            Punto secondoY = primoY.compare(primoX) ? secondoX : primoX;

            if (secondoX.getX() - primoX.getX() > 0) {
                scena.add(original.getSubimage(primoX.getX(), secondoY.getY(), secondoX.getX() - primoX.getX(), primoY.getY() - secondoY.getY()));
                //System.out.println("x: "+primoX.getX() + " y: " + secondoY.getY() + " width: " + (secondoX.getX() - primoX.getX()) + " height: " + (primoY.getY() - secondoY.getY()));
            } else {
                errore = true;
                Log.append("immagine " + path + " corrotta", DefaultFont.INFORMATION);
                return;
            }
        }

        numeroDiImmagini = punti.size() / 2;

        normal = new BufferedImage[scena.size()];
        mirror = new BufferedImage[scena.size()];

        int i = 0;

        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        for (BufferedImage bufferedImage : scena) {
            normal[i] = bufferedImage;
            if (dir == Direction.RIGHT || dir == Direction.LEFT) {
                /*
            *
            * INVERTE LE IMMAGINI E LE MEMORIZZA NEL BUFFER SPECCHIO (IL CODICE SOTTO)
            *
                 */
                tx = AffineTransform.getScaleInstance(-1, 1);
                tx.translate(-bufferedImage.getWidth(null), 0);
                op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                bufferedImage = op.filter(bufferedImage, null);

                mirror[i] = bufferedImage;
            }
            i++;
        }
        //System.out.println("" + normal.length);
    }

    /*
    *
    *   COSTRUTTORE PER IMMAGINI CON LO STESSO NUMERO DI FIGURE PER 1 O PIU' RIGHE
    *
     */
    public Cut(BufferedImage original, int x, int y, int width, int height, int numeroDiImmaginiX, int numeroDiImmaginiY, int xOffset, int yOffset, Move move, Type type, Direction dir) {
        this.move = move;
        this.type = type;
        this.dir = dir;

        ArrayList<BufferedImage> scena = new ArrayList<>();
        for (int y0 = 0; y0 < numeroDiImmaginiY; y0++) {
            for (int x0 = 0; x0 < numeroDiImmaginiX; x0++) {
                scena.add(original.getSubimage(x + (xOffset + width) * x0, y + (yOffset + height) * y0, width, height));
            }
        }
        numeroDiImmagini = scena.size();

        normal = new BufferedImage[numeroDiImmagini];
        mirror = new BufferedImage[numeroDiImmagini];

        int i = 0;

        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        for (BufferedImage bufferedImage : scena) {
            normal[i] = bufferedImage;
            /*
            *
            * INVERTE LE IMMAGINI E LE MEMORIZZA NEL BUFFER SPECCHIO (IL CODICE SOTTO)
            *
             */
            tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-bufferedImage.getWidth(null), 0);
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            bufferedImage = op.filter(bufferedImage, null);

            mirror[i] = bufferedImage;

            i++;
        }
    }

    public Cut(BufferedImage original, int x, int y, int width, int height, int[] numeroDiImmaginiX, int numeroDiImmaginiY, int xOffset, int yOffset, Move move, Type type, Direction dir) {    //SPECIALE
        this.move = move;
        this.type = type;
        this.dir = dir;

        ArrayList<BufferedImage> scena = new ArrayList<>();
        for (int y0 = 0; y0 < numeroDiImmaginiY; y0++) {
            for (int x0 = 0; x0 < numeroDiImmaginiX[y0]; x0++) {
                scena.add(original.getSubimage(x + (xOffset + width) * x0, y + (yOffset + height) * y0, width, height));
            }
        }

        numeroDiImmagini = scena.size();

        normal = new BufferedImage[numeroDiImmagini];
        mirror = new BufferedImage[numeroDiImmagini];

        int i = 0;

        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        for (BufferedImage bufferedImage : scena) {
            normal[i] = bufferedImage;
            /*
            *
            * INVERTE LE IMMAGINI E LE MEMORIZZA NEL BUFFER SPECCHIO (IL CODICE SOTTO)
            *
             */
            tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-bufferedImage.getWidth(null), 0);
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            bufferedImage = op.filter(bufferedImage, null);

            mirror[i] = bufferedImage;

            i++;
        }
    }

    public Cut(BufferedImage original, int x, int y, int width, int height, int numeroDiImmaginiX, int numeroDiImmaginiY, int xOffset, int yOffset, Move move, Type type, int jump, Direction dir) {    //SPECIALE
        this.move = move;
        this.type = type;
        this.dir = dir;

        ArrayList<BufferedImage> scena = new ArrayList<>();
        for (int y0 = 0; y0 < numeroDiImmaginiY; y0++) {
            for (int x0 = 0; x0 < numeroDiImmaginiX; x0++) {
                if (x0 < 14) {
                    y -= jump;
                } else if (x0 < 26) {
                    y += 2;
                } else {
                    y += 5;
                }
                scena.add(original.getSubimage(x + (xOffset + width) * x0, y + (yOffset + height) * y0, width, height));
            }
        }

        numeroDiImmagini = scena.size();

        normal = new BufferedImage[numeroDiImmagini];
        mirror = new BufferedImage[numeroDiImmagini];

        int i = 0;

        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        for (BufferedImage bufferedImage : scena) {
            normal[i] = bufferedImage;
            /*
            *
            * INVERTE LE IMMAGINI E LE MEMORIZZA NEL BUFFER SPECCHIO (IL CODICE SOTTO)
            *
             */
            tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-bufferedImage.getWidth(null), 0);
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            bufferedImage = op.filter(bufferedImage, null);

            mirror[i] = bufferedImage;

            i++;
        }
    }

    public Cut(String path, ArrayList<Punto> coord, Move move, Type type) { //da visionare
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Cut(String path, LinkedList<Punto> punti, Move move, Type type, Direction dir, String cart, String transformation, Type unlockable) {

        errore = false;

        BufferedImage original = null;
        original = new Loader().LoadImageCompletePath(path);
        this.type = type;

        this.unlockable = unlockable;

        this.move = move;
        this.dir = dir;

        this.path = cart;
        if (move == null && dir == null) {
            tile = transformation;
        } else {
            this.transformation = transformation;
        }

        ArrayList<BufferedImage> scena = new ArrayList<>();

        //System.out.println("" + (punti.size() / 2));
        numeroDiImmagini = punti.size();

        Punto primo = null;
        Punto secondo = null;

        for (int i = 0; i < numeroDiImmagini; i += 2) {
            if (punti.get(i).getX() < punti.get(i + 1).getX() && punti.get(i).getY() < punti.get(i + 1).getY()) {
                primo = punti.get(i);
                secondo = punti.get(i + 1);
            } else {
                secondo = punti.get(i);
                primo = punti.get(i + 1);
            }
            scena.add(original.getSubimage(primo.getX(), primo.getY(), secondo.getX() - primo.getX(), secondo.getY() - primo.getY()));
        }

        numeroDiImmagini = punti.size() / 2;

        normal = new BufferedImage[scena.size()];
        mirror = new BufferedImage[scena.size()];

        int i = 0;

        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

        for (BufferedImage bufferedImage : scena) {
            normal[i] = bufferedImage;
            if (Direction.RIGHT == dir || Direction.LEFT == dir) {
                /*
            *
            * INVERTE LE IMMAGINI E LE MEMORIZZA NEL BUFFER SPECCHIO (IL CODICE SOTTO)
            *
                 */
                tx = AffineTransform.getScaleInstance(-1, 1);
                tx.translate(-bufferedImage.getWidth(null), 0);
                op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                bufferedImage = op.filter(bufferedImage, null);

                mirror[i] = bufferedImage;
            }

            i++;
        }
        //System.out.println("" + normal.length);
    }

    public Move getMove() {
        return move;
    }

    public Type getType() {
        return type;
    }

    public Direction getDirection() {
        return dir;
    }

    public int getNumImmagini() {
        return numeroDiImmagini;
    }

    public BufferedImage[] getNormal() {
        return normal;
    }

    public BufferedImage[] getMirror() {
        return mirror;
    }

    public String getPath() {
        return path;
    }

    public String getTransformation() {
        return transformation;
    }

    public String getTile() {
        return tile;
    }

    public Type getUnlockable() {
        return unlockable;
    }

    public boolean getErrore() {
        return errore;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        
        out.writeInt(normal.length); // how many images are serialized?
        
        int rep = mirror[0] != null ? 2 : 1;    
        out.writeInt(rep);  //has mirrored image?

        for (int i = 0; i < rep; i++) {
            for (int j = 0; j < normal.length; j++) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                try {
                    if (i == 0) {
                        ImageIO.write(normal[j], "png", buffer);
                    } else {
                        ImageIO.write(mirror[j], "png", buffer);
                    }
                    out.writeInt(buffer.size()); // Prepend image with byte count
                    buffer.writeTo(out);         // Write image
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.append(Log.stackTraceToString(e), DefaultFont.ERROR);
                }
            }

        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        int imageCount = in.readInt();
        int mir = in.readInt();
        
        normal = new BufferedImage[imageCount];
        mirror = new BufferedImage[imageCount];
        for (int i = 0; i < mir; i++) {
            for (int j = 0; j < imageCount; j++) {
                int size = in.readInt(); // Read byte count
                

                byte[] buffer = new byte[size];
                in.readFully(buffer); // Make sure you read all bytes of the image

                if (i == 0) {
                    normal[j] = (ImageIO.read(new ByteArrayInputStream(buffer)));
                } else {
                    mirror[j] = (ImageIO.read(new ByteArrayInputStream(buffer)));
                    
                }
            }

        }
    }

}
