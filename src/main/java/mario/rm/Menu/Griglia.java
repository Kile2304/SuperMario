package mario.rm.Menu;

import mario.rm.Menu.Componenti.bottoni.Specifiche;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import mario.MainComponent;
import static mario.MainComponent.memoryUsed;
import mario.rm.Menu.Componenti.Checkable;
import mario.rm.Menu.Componenti.draw.ToolTip;
import mario.rm.Menu.level_editor.Script;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;
import mario.rm.utility.Punto;

/**
 *
 * @author LENOVO
 */
public class Griglia extends JPanel {

    private static Preview livello;
    private JFrame ed;

    private int moveX;
    private int moveY;

    private boolean grid;

    private int pixel;

    private Checkable pannello;

    private boolean background;

    private BufferedImage img;

    private boolean col;
    private boolean attach;

    private Font f;

    private boolean isEraser;

    private boolean toolTip;

    public Griglia(int WIDTH, int HEIGHT, JFrame ed, int pixel) {
        super();
        this.pixel = pixel;

        this.ed = ed;

        livello = new Preview(WIDTH / pixel, HEIGHT / pixel, true);

        System.out.println("debug griglia: " + memoryUsed());
        Dimension size = new Dimension(livello.getMappa().length * pixel, livello.getMappa()[0].length * pixel);

        setPreferredSize(size);

        grid = true;

        background = true;

        col = false;

        isEraser = false;
        attach = false;
        toolTip = false;

    }

    public Griglia(int WIDTH, int HEIGHT, JFrame ed, int pixel, boolean grid) {
        super();

        this.pixel = pixel;

        this.ed = ed;

        livello = new Preview(WIDTH / pixel, HEIGHT / pixel, true);

        Dimension size = new Dimension(livello.getMappa().length * pixel, livello.getMappa()[0].length * pixel);

        setPreferredSize(size);

        grid = false;

        background = true;

        col = false;

        isEraser = false;
        attach = false;
        toolTip = false;

    }

    public Griglia(JFrame ed, String path, int pixel) {
        super();

        this.ed = ed;

        grid = false;

        livello = new Preview();

        load(path);

        Dimension size = new Dimension(livello.getMappa().length * pixel + 1, livello.getMappa()[0].length * pixel + 1);

        setPreferredSize(size);

        this.pixel = pixel;

        background = true;

        col = false;

        isEraser = false;
        attach = false;
        toolTip = false;

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Cell[][] elenco = livello.getMappa();

        if (elenco == null) {
            Log.append("errore nessun livello caricato", DefaultFont.INFORMATION);
            return;
        }

        g.clearRect(0, 0, elenco.length * pixel, elenco[0].length * pixel);

        if (background && img != null) {
            int x = (pixel) - (moveX * pixel);
            int y = (pixel) - (moveY * pixel);
            g.drawImage(img, x, y, img.getWidth() * pixel, img.getHeight() * pixel, this);
        }

        if (grid) {
            for (int i = 0; i < elenco.length * pixel; i += pixel) {
                g.drawLine(i, 0, i, elenco[0].length * pixel);
            }
            for (int j = 0; j < elenco[0].length * pixel; j += pixel) {
                g.drawLine(0, j, elenco.length * pixel, j);
            }
        }
        for (int k = 0; k < elenco[0].length; k++) {
            for (int i = 0; i < elenco.length; i++) {
                if (elenco[i][k] != null) {
                    int x = (i * pixel) - (moveX * pixel);
                    int y = (k * pixel) - (moveY * pixel);
                    g.drawImage(elenco[i][k].getImg(), x, y, pixel, pixel, null);
                    if (col && elenco[i][k].getCollider() || attach && !elenco[i][k].getScript().equals("") || toolTip) {
                        //f = new Font("TimesRoman", Font.BOLD, pixel);
                        //g.setFont(f);
                        if (col) {
                            g.setColor(Color.RED);
                            g.drawString("1", x + pixel / 2, y + pixel);
                        } else if (attach) {
                            g.setColor(Color.BLUE);
                            g.drawString("2", x + pixel / 2, y + pixel);
                        }
                        if (toolTip) {
                            try {
                                Point pos = this.getMousePosition();
                                int xM = (int) (pos.getX() / pixel);
                                int yM = (int) (pos.getY() / pixel);
                                if (elenco[xM][yM] != null && i == xM && yM == k && !elenco[xM][yM].getScript().equals("")) {
                                    //g.drawString(elenco[xM][yM].getScript(), x, y);
                                    ToolTip.draw(g, new Punto(xM, yM), pixel, elenco[xM][yM].getScript());
                                }
                            } catch (NullPointerException e) {
                            }
                        }
                    }
                }
            }
        }

    }

    public boolean isToolTip() {
        return toolTip;
    }

    public void setToolTip() {
        toolTip = Boolean.logicalXor(toolTip, true);
    }

    public int getMovX() {
        return moveX;
    }

    public int getMovY() {
        return moveY;
    }

    public void setItem(int colonna, int riga) {
        Specifiche s = pannello.getChecked();
        System.out.println("" + attach);
        if (!col && !attach) {
            if (s == null && !isEraser) {
                return;
            }
            livello.addElement(colonna, riga, s);
        } else if (livello.getMappa()[colonna][riga] != null || attach) {
            if (col) {
                livello.getMappa()[colonna][riga].changeCollider();
            } else if (attach) {
                new Script(this);
            }
        }
    }

    public void setItem(int colonna, int riga, Cell s) {
        if (!col) {
            if (s == null && !isEraser) {
                return;
            }

            livello.addElement(colonna, riga, s);
        } else if (livello.getMappa()[colonna][riga] != null) {
            if (col) {
                livello.getMappa()[colonna][riga].changeCollider();
            } else if (attach) {
                new Script(this);
            }
        }
    }

    public void load() {
        moveX = 0;
        moveY = 0;
        String s = livello.load();
        if (s != null) {
            loadCollider(s);
            Cell[][] temp = livello.getMappa();
            setPreferredSize(new Dimension(temp.length * pixel, temp[0].length * pixel));
            ed.repaint();
        }
    }

    public boolean isAttach() {
        return attach;
    }

    public void changeAttach() {
        attach = Boolean.logicalXor(attach, true);
    }

    public void load(String path) {
        if (!path.equals("")) {
            moveX = 0;
            moveY = 0;

            livello.load(path);

            setPreferredSize(new Dimension(livello.getMappa().length * pixel, livello.getMappa()[0].length * pixel));
            ed.repaint();
        }

    }

    private void loadCollider(String path) {
        String finalPath = path.substring(0, path.lastIndexOf(".level"));
        finalPath += ".level";
        //System.out.println("finalPath: " + finalPath);
        livello.carica(finalPath, true);

    }

    public void loadImage(BufferedImage img) {
        this.img = img;
        if (img != null) {
            livello.resize(img);
            moveX = 0;
            moveY = 0;
            Cell[][] temp = livello.getMappa();
            setPreferredSize(new Dimension(temp.length * pixel, temp[0].length * pixel));
        }
    }

    private void saveAll(String path) {

        if (!path.equals("")) {

            try {
                FileWriter fw = new FileWriter(path);
                BufferedWriter bw = new BufferedWriter(fw);

                Cell[][] cl = livello.getMappa();

                for (int i = 0; i < cl.length; i++) {
                    for (int j = 0; j < cl[0].length; j++) {
                        if (cl[i][j] != null) {
                            String str = processSave(i, j, cl);
                            bw.append(str);
                            bw.append("\n");
                        }
                    }
                }
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(Griglia.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void savePart(String path) {
        if (!path.equals("")) {

            try {
                FileWriter fw = new FileWriter(path);
                BufferedWriter bw = new BufferedWriter(fw);

                Cell[][] cl = livello.getMappa();

                for (int i = 0; i < cl.length; i++) {
                    for (int j = 0; j < cl[0].length; j++) {
                        if (cl[i][j] != null && cl[i][j].getCollider()) {
                            String str = processSave(i, j, cl);
                            bw.append(str);
                            bw.append("\n");
                        }
                    }
                }
                bw.close();
            } catch (IOException ex) {
                Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
            }
        }
    }

    private String processSave(int i, int j, Cell[][] cl) {
        String str = i + " " + j + " " + cl[i][j].getType();
        str += ((!cl[i][j].getPartTil().equals(""))
                ? " " + cl[i][j].getPartTil()
                : "")
                + "/";
        if (cl[i][j].getScript() != null && !cl[i][j].equals("")) {
            str += cl[i][j].getScript();
        }

        return str;
    }

    public void saveBackground() {

        int pix = 64;

        Cell[][] elenco = livello.getMappa();

        Punto min = new Punto(elenco.length, elenco[0].length);
        Punto max = new Punto(0, 0);
        //calcolo salvo spazio
        for (int i = 0; i < elenco[0].length; i++) {
            boolean notNull = true;
            int xMin = 0, xMax = 0, yMin = 0, yMax = 0;
            for (int j = 0; j < elenco.length; j++) {
                if (elenco[j][i] != null) {
                    if (xMin > j) {
                        xMin = j;
                    }
                    if (xMax < j) {
                        xMax = j;
                    }
                    if (yMin > i) {
                        yMin = i;
                    }
                    if (yMax < i) {
                        yMax = i;
                    }
                    notNull = false;
                }
            }
            if (!notNull) {
                if (min.getX() > xMin) {
                    min.setX(xMin);
                }
                if (max.getX() < xMax) {
                    max.setX(xMax);
                }
                if (min.getY() > yMin) {
                    min.setY(yMin);
                }
                if (max.getY() < yMax) {
                    max.setY(yMax);
                }
            }
        }
        //fine calcolo salvo spazio
        System.out.println("xMin: " + min.getX() + " xMax: " + max.getX() + " yMin: " + min.getY() + " yMax: " + max.getY());

        BufferedImage image = new BufferedImage((max.getX() - min.getX()) * pix, (max.getY() - min.getY()) * pix, BufferedImage.TYPE_INT_RGB);

        JFileChooser c = null;
        /*if (MainComponent.jar.isFile()) {
            c = new JFileChooser(new File("src/mario/res/Immagini/livelli"));
        } else {
            // String f = MainComponent.jarPath.getAbsolutePath().substring(0, MainComponent.jarPath.getAbsolutePath().lastIndexOf("\\"));
            c = new JFileChooser(new File("src/mario/res/Immagini/livelli"));
        }*/
        /*if (MainComponent.jar.isFile()) {
            c = new JFileChooser(new File(MainComponent.class.getClassLoader().getResource("Immagini/livelli").getFile()));
        } else {
            c = new JFileChooser(new File("res/Immagini/livelli"));
        }*/
        
        c = new JFileChooser(new File(MainComponent.filePath+"/Luigi/Level"));

        int valid = c.showSaveDialog(null);

        String path = "";

        if (valid == JFileChooser.APPROVE_OPTION) {

            String original = c.getSelectedFile().getAbsolutePath();
            String file = "";
            String extension = "";
            try {
                file = original.substring(original.lastIndexOf("\\"));
            } catch (StringIndexOutOfBoundsException e) {
                file = original;
            } finally {
                try {
                    extension = file.substring(file.indexOf("."));
                    path = original.substring(0, original.length() - extension.length()) + ".level";
                    if (!extension.equals(".png")) {
                        original = original.substring(0, original.length() - extension.length()) + ".png";
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    path = original + ".level";
                    original += ".png";
                }

            }

            //savePart(path);

            try {
                path = path.substring(0, path.length() - 6);
            } catch (StringIndexOutOfBoundsException e) {
            }
            path += ".level";

            saveAll(path);

            /*Graphics2D g = image.createGraphics();

            //Color color = new Color(255, 255, 255);
            for (int i = min.getY(); i < max.getY(); i++) {
                for (int j = min.getX(); j < max.getX(); j++) {
                    if (elenco[j][i] != null && elenco[j][i].getTerrain() || elenco[j][i] != null && !elenco[j][i].getCollider()) {
                        //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                        g.setComposite(AlphaComposite.Src);
                        BufferedImage im = elenco[j][i].getImg();

                        for (int k = 0; k < im.getHeight(); k++) {
                            for (int l = 0; l < im.getWidth(); l++) {
                                if (!Integer.toHexString(im.getRGB(l, k)).equals("0xFFFFFF")) {
                                    g.setComposite(AlphaComposite.Src);
                                    //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                                    int color = im.getRGB(l, k);
                                    g.setColor(new Color(color));
                                    g.fillRect(j * pix + l, i * pix + k, pix, pix);
                                } else {  //prestare attenzione
                                    //g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
                                    //int color = ((255 & 0xff) << 24);
                                    //g.setColor(new Color(color));
                                    g.setComposite(AlphaComposite.Clear);
                                    g.fillRect(j * pix + l, i * pix + k, pix, pix);
                                }
                            }
                        }
                        //g.drawImage(elenco[j][i].getImg(), j * pix, i * pix, pix, pix, null);
                    } else {
                        //g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
                        //int color = ((255 & 0xff) << 24);
                        //g.setColor(new Color(color));
                        g.setComposite(AlphaComposite.Clear);
                        g.fillRect(j * pix, i * pix, pix, pix);
                    }
                }
            }

            try {
                ImageIO.write(image, "bmp", new File(original));
            } catch (IOException ex) {
                Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
            }

            Log.append("Immagine creata", DefaultFont.INFORMATION);*/
        }
    }

    /*public void setX(int x) {
        Cell[][] elenco = livello.getMappa();
        int width = 1511 / pixel;
        if (width + this.moveX + x < elenco.length && (this.moveX + x) >= 0) {
            this.moveX += x;
        }
        if (width + this.moveX + x > elenco.length) {
            ed.changeStateOrizontal(-1);
        } else {
            ed.changeStateOrizontal(1);
        }
        if ((this.moveX + x) < 0) {
            ed.changeStateOrizontal(-2);
        } else {
            ed.changeStateOrizontal(2);
        }

    }*/
    public int getPixel() {
        return pixel;
    }

    /*public void setY(int y) {
        Cell[][] elenco = livello.getMappa();
        int height = 1050 / pixel;
        if (height + this.moveY + y < elenco[0].length && (this.moveY + y) >= 0) {
            this.moveY += y;
        }

        if (height + this.moveY + y > elenco[0].length) {
            ed.changeStateVertical(-1);
        } else {
            ed.changeStateVertical(1);
        }
        if ((this.moveY + y) < 0) {
            ed.changeStateVertical(-2);
        } else {
            ed.changeStateVertical(2);
        }
    }*/
    public void setPanel(Checkable pannello) {
        this.pannello = pannello;
    }

    public void increasePixel() {
        pixel += 5;
        setPreferredSize(new Dimension(livello.getMappa().length * pixel, livello.getMappa()[0].length * pixel));
        revalidate();
    }

    public void decreasePixel() {
        if (pixel > 5) {
            pixel -= 5;
            setPreferredSize(new Dimension(livello.getMappa().length * pixel, livello.getMappa()[0].length * pixel));
        } else {
            pixel = 5;
            setPreferredSize(new Dimension(livello.getMappa().length * pixel, livello.getMappa()[0].length * pixel));
        }
        revalidate();
    }

    public void changeCollider() {
        col = Boolean.logicalXor(col, true);
    }

    public boolean getCollider() {
        return col;
    }

    public void newPage(int x, int y) {
        livello = new Preview(y, x, false);
        setPreferredSize(new Dimension(y * pixel, x * pixel));
        ed.repaint();
        img = null;

    }

    public static void clean() {
        Cell[][] cell = livello.getMappa();
        for (int i = 0; i < cell.length; i++) {
            for (int j = 0; j < cell[0].length; j++) {
                if (cell[i][j] != null) {
                    livello.addElement(i, j, null);
                }
            }
        }

    }

    public void normal() {
        attach = false;
        col = false;
        isEraser = false;
        toolTip = false;
    }

    public void addRow(int val) {
        Cell[][] temp = livello.getMappa().clone();

        Cell[][] temp2 = new Cell[temp.length][val + temp[0].length];

        for (int i = 0; i < temp.length; i++) {
            System.arraycopy(temp[i], 0, temp2[i], 0, temp[0].length);
        }
        livello.setMappa(temp2);
        setPreferredSize(new Dimension(livello.getMappa().length * pixel, livello.getMappa()[0].length * pixel));
        repaint();
    }

    public void addColumn(int val) {
        Cell[][] temp = livello.getMappa().clone();

        Cell[][] temp2 = new Cell[temp.length + val][temp[0].length];

        for (int i = 0; i < temp.length; i++) {
            System.arraycopy(temp[i], 0, temp2[i], 0, temp[0].length);
        }
        livello.setMappa(temp2);
        setPreferredSize(new Dimension(livello.getMappa().length * pixel, livello.getMappa()[0].length * pixel));
        repaint();
    }

    public void changeErase() {
        isEraser = Boolean.logicalXor(isEraser, true);
    }

    public boolean getErase() {
        return isEraser;
    }

    public Preview getPreview() {
        return livello;
    }

    public void setScript(Punto p) {
        livello.getMappa()[p.getX()][p.getY()].setScript("A");
    }

}
