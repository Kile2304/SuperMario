package mario.rm.Menu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import mario.MainComponent;
import mario.rm.Animation.Anim;
import mario.rm.Animation.Tile;
import mario.rm.identifier.Direction;
import mario.rm.identifier.Move;
import mario.rm.identifier.TilePart;
import mario.rm.identifier.Type;
import mario.rm.input.Loader;
import mario.rm.input.MemoriaAC;
import mario.rm.input.Reader;
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;
import mario.rm.utility.Punto;

/**
 *
 * @author LENOVO
 */
public class Preview {

    private Cell[][] mappa;

    private MemoriaAC memoria;

    public Preview(int width, int height) {
        mappa = new Cell[width][height];
        memoria = new MemoriaAC();
        memoria.carica();
    }

    public Preview() {
        memoria = new MemoriaAC();
        memoria.carica();
    }

    public Cell[][] getMappa() {
        return mappa;
    }

    public void addElement(int column, int row, Cell s) {
        if (column >= 0 && column < mappa.length && row >= 0 && row < mappa[0].length) {
            if (s != null) {
                mappa[column][row] = new Cell(s.getType(), s.getImg(), s.getPartTil());
                mappa[column][row].setTerrain(s.getTerrain());

            } else {
                mappa[column][row] = null;
            }
        }
    }

    public String load() {
        JFileChooser c = null;
        if (MainComponent.jar.isFile()) {
            c = new JFileChooser(new File(MainComponent.jar.getAbsolutePath() + "/res/Immagini/livelli"));
        } else {
            String f = MainComponent.jarPath.getAbsolutePath().substring(0, MainComponent.jarPath.getAbsolutePath().lastIndexOf("\\"));
            c = new JFileChooser(new File(f + "/res/Immagini/livelli"));
        }

        FileNameExtensionFilter filter = new FileNameExtensionFilter(".level", "level", "level");
        c.setFileFilter(filter);

        int valid = c.showOpenDialog(null);

        if (valid == JFileChooser.APPROVE_OPTION) {
            clear();

            carica(c.getSelectedFile().getAbsolutePath(), false);

            return c.getSelectedFile().getAbsolutePath();
        }
        return null;

    }

    public void load(String path) {
        clear();

        carica(path, false);
    }

    public void carica(String path, boolean isCollider) {  //DA MODIFICARE PER USO ESCLUSIVO FILE TXT PER CARICAMENTO LIVELLI

        EditLoad ed = new EditLoad();

        new Loader().convertTextInMap(path, ed);

        ArrayList<Cell> cl = ed.getCell();
        ArrayList<Punto> coord = ed.getCoord();

        int maxX = coord.get(0).getX();
        int maxY = coord.get(0).getY();

        for (int i = 0; i < coord.size(); i++) {
            Punto p1 = coord.get(i);
            if (p1.getX() > maxX) {
                maxX = p1.getX();
            }
            if (p1.getY() > maxY) {
                maxY = p1.getY();
            }
        }

        //System.out.println("maxX: "+maxX+" maxY: "+maxY);
        if (!isCollider) {
            mappa = new Cell[maxX + 1][maxY + 1];
            for (int i = 0; i < cl.size(); i++) {
                Punto p1 = coord.get(i);
                mappa[p1.getX()][p1.getY()] = cl.get(i);
            }
        } else {
            for (int i = 0; i < coord.size(); i++) {
                if (mappa[coord.get(i).getX()][coord.get(i).getY()] != null) {
                    mappa[coord.get(i).getX()][coord.get(i).getY()].changeCollider();
                }
            }
        }

    }

    public void resize(BufferedImage img) {
        clear();
        mappa = new Cell[img.getWidth()][img.getHeight()];
    }

    public void clear() {
        mappa = null;
    }

    void setMappa(Cell[][] temp2) {
        mappa = temp2;
    }

    class EditLoad implements Reader {

        private ArrayList<Cell> cl;
        private ArrayList<Punto> coord;
        private final BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        private MemoriaAC memoria;

        EditLoad() {
            cl = new ArrayList<>();
            coord = new ArrayList<>();
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    img.setRGB(j, i, (255 & 0xFF));
                }
            }
            memoria = new MemoriaAC();
            memoria.carica();
        }

        @Override
        public void creaLivello(int x0, int y0, Type type, Type unlockable, String tile) {

            boolean stop = false;
            boolean terreno = false;

            Object ob = null;

            ArrayList<Anim> player = memoria.getPlayer();
            ArrayList<Anim> enemy = memoria.getEnemy();
            ArrayList<Tile> tiles = memoria.getTiles();
            ArrayList<Tile> unlock = memoria.getUnlockable();
            ArrayList<Tile> terreni = memoria.getTerreni();

            for (Iterator<Anim> it = player.iterator(); it.hasNext();) {
                Anim a = it.next();
                if (a.getType() == unlockable) {
                    ob = a;
                    stop = true;
                    break;
                }
            }
            if (!stop) {
                for (Iterator<Anim> it = enemy.iterator(); it.hasNext();) {
                    Anim a = it.next();
                    if (a.getType() == type) {
                        ob = a;
                        stop = true;
                        break;
                    }
                }
            }
            if (!stop) {
                for (Iterator<Tile> it = tiles.iterator(); it.hasNext();) {
                    Tile a = it.next();
                    if (a.getType() == type || a.getType() == unlockable) {
                        ob = a;
                        stop = true;
                        break;
                    }
                }
            }
            if (!stop) {
                for (Iterator<Tile> it = unlock.iterator(); it.hasNext();) {
                    Tile a = it.next();
                    if (a.getType() == type || a.getType() == unlockable) {
                        ob = a;
                        stop = true;
                        break;
                    }
                }
            }
            if (!stop) {
                for (Iterator<Tile> it = terreni.iterator(); it.hasNext();) {
                    Tile a = it.next();
                    if (a.getType() == type || a.getType() == unlockable) {
                        ob = a;
                        terreno = true;
                        stop = true;
                        break;
                    }
                }
            }
            coord.add(new Punto(x0, y0));
            if (!tile.equals("")) {
                Tile ti = (Tile) ob;
                TilePart part = TilePart.valueOf(tile);
                BufferedImage[] bg = ti.getImage(part);
                if (bg != null) {
                    cl.add(new Cell(type, bg[0], tile));
                    if(terreno){
                        cl.get(cl.size() - 1).setTerrain(true);
                    }
                }
            } else if (type == Type.VOID) {
                cl.add(new Cell(type, img, ""));
            } else {
                Anim an = (Anim) ob;
                if (an != null) {
                    cl.add(new Cell(type, an.getImage(Move.WALK, Direction.RIGHT), ""));
                } else {
                    Log.append("Anim = null", DefaultFont.INFORMATION);
                }
            }
        }

        public ArrayList<Cell> getCell() {
            return cl;
        }

        public ArrayList<Punto> getCoord() {
            return coord;
        }

    }

}
