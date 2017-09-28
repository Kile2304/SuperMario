package mario.rm.Menu.sprite_estractor.input;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import mario.rm.Menu.Griglia;
import mario.rm.Menu.home.Home;
import mario.rm.input.Loader;

/**
 *
 * @author LENOVO
 */
public class Selezione implements MouseListener {

    private Griglia g;
    private JFrame fr;

    private Collegamenti cl;

    private static final int spostamento = 5;

    private int type;

    private Cursor cur1;
    private Cursor cur2;

    public Selezione(Griglia g, JFrame fr) {
        this.g = g;
        this.fr = fr;
        cl = new Collegamenti();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("src/mario/res/Immagini/cursor/tool.png");
        cur1 = toolkit.createCustomCursor(image, new Point(fr.getX(), fr.getY()), "img");
        image = toolkit.getImage("src/mario/res/Immagini/cursor/pencil.png");
        cur2 = toolkit.createCustomCursor(image, new Point(fr.getX(), fr.getY()), "img");
    }

    private void load() {
        
        JFileChooser c = new JFileChooser(new File("src/mario/res/Immagini/extract").getAbsolutePath());

        int valid = c.showOpenDialog(fr.getContentPane());

        if (valid == JFileChooser.APPROVE_OPTION) {
            try {
                action("New");
            } catch (IOException ex) {}
            BufferedImage level = new Loader().LoadImageCompletePath(c.getSelectedFile().getAbsolutePath());
            g.loadImage(level);
        } else {
            return;
        }

        cl = new Collegamenti(c.getSelectedFile().getAbsolutePath());
    }

    public void action(String code) throws IOException {
        switch (code) {
            case "+":
                g.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                type = 0;
                g.increasePixel();
                fr.repaint();
                fr.revalidate();
                break;
            case "-":
                fr.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                g.decreasePixel();
                type = 0;
                fr.repaint();
                fr.revalidate();
                break;
            case "Load":
                g.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                load();
                type = 0;
                fr.repaint();
                fr.revalidate();
                break;
            case "Extract":
                g.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                type = 0;
                cl.estrazione();
                break;
            case "CREATE POINT":
                g.setCursor(cur2);
                type = 1;
                break;
            case "DELETE POINT":
                g.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                type = 2;
                break;
            case "New":
                g.newPage(50, 50);
            case "Clean":
                g.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                type = 0;
                cl.partialClean();
                fr.repaint();
                fr.revalidate();
                break;
            case "Home":
                g.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                Pannelli.elenco.clear();
                Pannelli.type.setText("");
                Pannelli.move.setSelectedIndex(0);
                Pannelli.isPlayer.setSelected(true);
                Pannelli.tile = null;
                fr.dispose();
                new Home().setVisible(true);
                break;
            case "Selettore":
                g.setCursor(cur1);
                type = 3;
                break;
            case "Manuale":
                String input = JOptionPane.showInputDialog("Inserisci il valore di RGB dello sfondo:", "( , , )");
                break;
            case "Exit":
                System.exit(0);
            case "Info":
                StringBuilder build = new StringBuilder();
                try {
                    FileReader fr = new FileReader("src/dragon/ball/res/info/infoEstrattore.html");
                    BufferedReader br = new BufferedReader(fr);

                    String temp = "";

                    while ((temp = br.readLine()) != null) {
                        build.append(temp + "\n");
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Selezione.class.getName()).log(Level.SEVERE, null, ex);
                }
                JEditorPane textArea = new JEditorPane();
                URL resourceUrl = new URL("file:"+
                        System.getProperty("user.dir")+
                        System.getProperty("file.separator")+
                        //"src"+
                        //System.getProperty("file.separator")+
                        "src/dragon/ball/res/info/infoEstrattore.html");
                textArea.setPage(resourceUrl);
                //textArea.setText(build.toString());
                textArea.setOpaque(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                //textArea.setLineWrap(true);
                //textArea.setWrapStyleWord(true);
                textArea.setEditable(false);
                scrollPane.setPreferredSize(new Dimension(700, 500));
                JOptionPane.showMessageDialog(null, scrollPane, "Informazioni",
                        JOptionPane.YES_NO_OPTION);
                //JOptionPane.showMessageDialog(null, build.toString(), "Informazioni", JOptionPane.INFORMATION_MESSAGE);                //JOptionPane.showMessageDialog(null, build.toString(), "Informazioni", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int colonna = (e.getX()) / g.getPixel() + g.getMovX();
        int riga = (e.getY() + 30) / g.getPixel() + g.getMovY();

        switch (type) {
            case 1: {
                Punto[] p = cl.nuovoPunto(colonna, riga);
                for (int i = 0; i < p.length; i++) {
                    g.setItem(p[i].getX(), p[i].getY());
                }
                break;
            }
            case 2: {
                Punto[] p = cl.rimuoviCollegamento(colonna, riga);
                for (int i = 0; i < p.length; i++) {
                    g.setItem(p[i].getX(), p[i].getY());
                }
                break;
            }
            case 3:
                try {
                    Robot r = new Robot();
                    Color c = r.getPixelColor(e.getXOnScreen(), e.getYOnScreen());
                    System.out.println("red: " + c.getRed() + " green: " + c.getGreen() + " blue: " + c.getBlue());
                    type = 0;
                    g.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                } catch (AWTException ex) {
                    Logger.getLogger(Selezione.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            default:
                break;
        }
        fr.repaint();
        fr.revalidate();

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void repaint() {
        fr.repaint();
        fr.revalidate();
    }

}