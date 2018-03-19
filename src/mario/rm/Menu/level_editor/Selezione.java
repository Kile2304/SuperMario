package mario.rm.Menu.level_editor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mario.rm.Menu.Componenti.Checkable;
import mario.rm.Menu.Griglia;
import mario.rm.Menu.home.Home;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;
import mario.rm.utility.Punto;

/**
 *
 * @author LENOVO
 */
public class Selezione implements MouseListener, MouseMotionListener, Runnable {

    private static Griglia g;
    private final JFrame fr;

    public static ArrayList<Punto> punto;

    private static final int spostamento = 5;

    private static int type;

    public Selezione(Griglia g, JFrame ed) {
        this.g = g;
        this.fr = ed;
        punto = new ArrayList<>();
    }

    public void normal() {
        g.normal();
    }

    public static void changeCollider() {
        g.changeCollider();
    }

    public static void changeAttach() {
        Selezione.type = 4;
        g.changeAttach();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int colonna = e.getX() / g.getPixel() + g.getMovX();
        int riga = e.getY() / g.getPixel() + g.getMovY();
        if (punto.isEmpty()) {
            punto.add(new Punto(colonna, riga));
        }

        if (!g.isAttach()) {
            g.setItem(colonna, riga);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!g.isAttach()) {
            punto.clear();
        } else {
            g.setItem(0, 0);
        }
        fr.repaint();
        fr.revalidate();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void action(String action) {
        switch (action) {
            case "Save":
                g.saveBackground();
                break;
            case "Load":
                g.load();
                break;
            case "+":
                g.increasePixel();
                break;
            case "-":
                g.decreasePixel();
                break;
            case "GOMMA":
                g.changeErase();
                break;
            case "New":
                String righe = JOptionPane.showInputDialog(fr, "Inserisci il numero di (righe, colonne) da aggiungere", "ES: 10,15", JOptionPane.DEFAULT_OPTION);
                String[] insert = null;
                try {
                    insert = righe.split(",");
                    Punto p = null;
                    try {
                        p = new Punto(Integer.parseInt(insert[0]), Integer.parseInt(insert[1]));
                        g.newPage(p.getX(), p.getY());
                    } catch (NumberFormatException e) {
                        Log.append(Log.stackTraceToString(e), DefaultFont.ERROR);
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    Log.append(Log.stackTraceToString(e), DefaultFont.ERROR);
                }
            case "Clean":
                g.clean();
                g.loadImage(null);
                Selezione.type = 0;
                fr.repaint();
                fr.revalidate();
                break;
            case "Home":
                Checkable.elenco.clear();
                fr.dispose();
                new Home().setVisible(true);   //constructor home senza parametri
                break;
            case "Selettore":
                //type = 3;
                break;
            case "Manuale":
                String input = JOptionPane.showInputDialog("Inserisci il valore di RGB dello sfondo:", "( , , )");
                break;
            case "Exit":
                System.exit(0);
                break;
            case "Row":
                String valueRow = JOptionPane.showInputDialog(fr, "Inserisci il numero di righe da aggiungere", "ROW=", JOptionPane.DEFAULT_OPTION);
                if (valueRow != null) {
                    int val = 0;
                    try {
                        val = Integer.parseInt(valueRow);
                        g.addRow(val);
                    } catch (NumberFormatException e) {
                        System.out.println("Valore inserito non valido");
                    }
                }
                break;
            case "Column":
                String valueColumn = JOptionPane.showInputDialog(fr, "Inserisci il numero di colonne da aggiungere", "COLUMN=", JOptionPane.DEFAULT_OPTION);
                if (valueColumn != null) {
                    int val = 0;
                    try {
                        val = Integer.parseInt(valueColumn);
                        g.addColumn(val);
                    } catch (NumberFormatException e) {
                        System.out.println("Valore inserito non valio");
                    }
                }
                break;
            case "ToolTip Script":
                g.normal();
                g.setToolTip();
                new Thread(this).start();
                break;
            case "Row e Column":
                String valueColumnRow = JOptionPane.showInputDialog(fr, "Inserisci il numero di righe e colonne da aggiungere", "COLUMN=", JOptionPane.DEFAULT_OPTION);
                if (valueColumnRow != null) {
                    int val = Integer.parseInt(valueColumnRow);
                    g.addColumn(val);
                    g.addRow(val);
                }
                break;
            case "Info":
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int colonna = e.getX() / g.getPixel() + g.getMovX();
        int riga = e.getY() / g.getPixel() + g.getMovY();

        for (Punto punto1 : punto) {
            if (punto1.compare(new Punto(colonna, riga))) {
                return;
            }
        }

        punto.add(new Punto(colonna, riga));

        if (!g.isAttach()) {
            g.setItem(colonna, riga);
        } else {
            g.setScript(punto.get(punto.size() - 1));
        }
        fr.repaint();
        fr.revalidate();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    void checkEraser() {
        if (g.getErase()) {
            g.changeErase();
        }
    }

    @Override
    public void run() {
        while (g.isToolTip()) {
            try {
                sleep(600);
            } catch (InterruptedException ex) {
                Logger.getLogger(Selezione.class.getName()).log(Level.SEVERE, null, ex);
            }
            g.repaint();
        }
    }

}
