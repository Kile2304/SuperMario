package mario.rm.Menu.sprite_estractor.input;

import mario.rm.utility.Punto;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.Robot;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import mario.MainComponent;
import mario.rm.Animation.Test;
import mario.rm.Menu.Cell;
import mario.rm.Menu.Griglia;
import mario.rm.Menu.Componenti.bottoni.Specifiche;
import mario.rm.Menu.home.Home;
import mario.rm.Menu.sprite_estractor.output.Design;
import mario.rm.input.Loader;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class Selezione implements MouseListener, MouseMotionListener {

    private Griglia g;
    private JFrame fr;

    private Collegamenti cl;

    private static final int spostamento = 5;

    private int type;

    private boolean isDefaultCursor;

    private final Specifiche delete = new Specifiche("");

    public Selezione(Griglia g, JFrame fr) {
        this.g = g;
        this.fr = fr;
        cl = new Collegamenti();
        isDefaultCursor = true;

    }

    private void load() {

        JFileChooser c = new JFileChooser(MainComponent.filePath + "/Luigi/resources");

        int valid = c.showOpenDialog(fr);

        if (valid == JFileChooser.APPROVE_OPTION) {
            try {
                action("New");
            } catch (IOException ex) {
            }
            // s = (c.getSelectedFile().getAbsolutePath());
            //System.out.println("" + s);
            //s = s.substring(s.lastIndexOf("Immagini\\extract"));
            BufferedImage level = Loader.LoadImageNormal(c.getSelectedFile().getAbsolutePath());
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
                isDefaultCursor = true;
                //type = 0;
                g.increasePixel();
                fr.repaint();
                fr.revalidate();
                break;
            case "-":
                fr.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                isDefaultCursor = true;
                g.decreasePixel();
                //type = 0;
                fr.repaint();
                fr.revalidate();
                break;
            case "Load":
                g.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                isDefaultCursor = true;
                load();
                type = 0;
                fr.repaint();
                fr.revalidate();
                break;
            case "Extract":
                g.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                isDefaultCursor = true;
                //type = 0;
                Log.append("estrazione", DefaultFont.DEBUG);
                cl.estrazione();
                break;
            case "CREATE POINT":
                // g.setCursor(cur2);

                isDefaultCursor = false;
                type = 1;
                break;
            case "DELETE POINT":
                g.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                isDefaultCursor = true;
                type = 2;
                break;
            case "New":
                g.newPage(50, 50);
            case "Clean":
                g.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                isDefaultCursor = true;
                //type = 0;
                cl.partialClean();
                fr.repaint();
                fr.revalidate();
                break;
            case "Home":
                g.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                isDefaultCursor = true;
                Setting.elenco.clear();
                Setting.type.setText("");
                Setting.move.setSelectedIndex(0);
                Setting.isPlayer.setSelected(true);
                Setting.tile = null;
                fr.dispose();
                new Home().setVisible(true);
                break;
            case "Selettore":
                //g.setCursor(cur1);
                isDefaultCursor = false;
                type = 3;
                break;
            case "Manuale":
                String input = JOptionPane.showInputDialog("Inserisci il valore di RGB dello sfondo:", "( , , )");
                break;
            case "Exit":
                System.exit(0);
            case "Info":
                JEditorPane textArea = new JEditorPane();

                textArea.setPage(MainComponent.class.getClassLoader().getResource("info/infoEstrattore.html"));
                //textArea.setText(build.toString());
                textArea.setOpaque(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                //textArea.setLineWrap(true);
                //textArea.setWrapStyleWord(true);
                textArea.setEditable(false);
                scrollPane.setPreferredSize(new Dimension(700, 500));
                JOptionPane.showMessageDialog(fr, scrollPane, "Sprite Estractor",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            case "toAcFile":
                Log.append("toAcFile", DefaultFont.DEBUG);
                new Thread() {
                    @Override
                    public void run() {
                        new Design(fr);
                    }
                }.start();
                break;
            case "testAnim": {
                try {
                    new Test(fr);
                    fr.setVisible(false);
                } catch (FileNotFoundException | ClassNotFoundException ex) {
                    Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
                }
            }
            break;
            case "Righello":
                type = 4;
                break;
            case "altro":
                fr.repaint();
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int colonna = (e.getX()) / g.getPixel() + g.getMovX();
        int riga = 0;
        if (isDefaultCursor) {
            riga = (e.getY()) / g.getPixel() + g.getMovY();
        } else {
            //riga = (e.getY() + 30 / 500 * g.getHeight()) / g.getPixel() + g.getMovY();
            riga = (e.getY()) / g.getPixel() + g.getMovY();
        }

        //System.out.println("colonna: " + colonna + " riga: " + riga);
        switch (type) {
            case 4:
            case 1: {
                new Thread() {
                    @Override
                    public void run() {
                        int colonna = (e.getX()) / g.getPixel() + g.getMovX();
                        int riga = 0;
                        if (isDefaultCursor) {
                            riga = (e.getY()) / g.getPixel() + g.getMovY();
                        } else {
                            //riga = (e.getY() + 30 / 500 * g.getHeight()) / g.getPixel() + g.getMovY();
                            riga = (e.getY()) / g.getPixel() + g.getMovY();
                        }
                        //System.out.println(""+riga+" "+colonna);
                        Punto[] p = null;
                        if (type == 1) {
                            p = cl.nuovoPunto(colonna, riga);
                        } else {
                            p = cl.nuovoPuntoRighello(colonna, riga);
                        }
                        if (p != null) {
                            for (int i = 0; i < p.length; i++) {
                                if (p[i] != null) {
                                    g.setItem(p[i].getX(), p[i].getY());
                                }
                            }
                        }
                    }
                }.start();
                break;
            }

            case 2: {
                Punto[] p = cl.rimuoviCollegamento(colonna, riga);
                if (p != null) {
                    for (int i = 0; i < p.length; i++) {
                        if (p[i] != null) {
                            g.setItem(p[i].getX(), p[i].getY(), delete);
                        }
                    }
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
                    isDefaultCursor = true;
                } catch (AWTException ex) {
                    Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
                }
                break;
            default:
                return;
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

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int colonna = (e.getX()) / g.getPixel() + g.getMovX();
        int riga = 0;
        if (isDefaultCursor) {
            riga = (e.getY()) / g.getPixel() + g.getMovY();
        } else {
            //riga = (e.getY() + 30 / 500 * g.getHeight()) / g.getPixel() + g.getMovY();
            riga = (e.getY()) / g.getPixel() + g.getMovY();
        }
        SpriteEstractor.coordinate.setText("C:" + colonna + " R:" + riga + " X:" + e.getX() + " Y:" + e.getY());
        //SpriteEstractor.coordinate.repaint();
    }

}
