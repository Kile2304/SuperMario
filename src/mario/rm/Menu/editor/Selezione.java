package mario.rm.Menu.editor;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import mario.rm.Menu.Componenti.Checkable;
import mario.rm.Menu.Griglia;
import mario.rm.Menu.home.Home;
import mario.rm.utility.Punto;

/**
 *
 * @author LENOVO
 */
public class Selezione implements MouseListener, MouseMotionListener {

    private static Griglia g;
    private final JFrame fr;

    ArrayList<Punto> punto;

    private static final int spostamento = 5;

    private int type;

    private Cursor cur1;
    private Cursor cur2;

    public Selezione(Griglia g, JFrame ed) {
        this.g = g;
        this.fr = ed;
        punto = new ArrayList<>();
        /*Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("src/mario/res/Immagini/cursor/tool.png");
        cur1 = toolkit.createCustomCursor(image, new Point(fr.getX(), fr.getY()), "img");
        image = toolkit.getImage("src/mario/res/Immagini/cursor/pencil.png");
        cur2 = toolkit.createCustomCursor(image, new Point(fr.getX(), fr.getY()), "img");*/
    }

    public static void changeCollider() {
        g.changeCollider();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int colonna = e.getX() / g.getPixel() + g.getMovX();
        int riga = e.getY() / g.getPixel() + g.getMovY();

        g.setItem(colonna, riga);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        punto.clear();
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
            case "↑":
                g.setY(-spostamento);
                break;
            case "↓":
                g.setY(spostamento);
                break;
            case "→":
                g.setX(spostamento);
                break;
            case "←":
                g.setX(-spostamento);
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
                try{
                    insert = righe.split(",");
                    Punto p = null;
                    try{
                        p = new Punto(Integer.parseInt(insert[0]), Integer.parseInt(insert[1]));
                        g.newPage(p.getX(), p.getY());
                    }catch(NumberFormatException e){
                        System.out.println("Valore inserito non valido");
                    }
                }catch(StringIndexOutOfBoundsException e){
                    System.out.println("Valore inserito non valido");
                }
            case "Clean":
                //g.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                g.clean();
                g.loadImage(null);
                type = 0;
                fr.repaint();
                fr.revalidate();
                break;
            case "Home":
                //g.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                Checkable.elenco.clear();
                fr.dispose();
                new Home().setVisible(true);   //constructor home senza parametri
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
                    try{
                        val = Integer.parseInt(valueColumn);
                        g.addColumn(val);
                    }catch(NumberFormatException e){
                        System.out.println("Valore inserito non valio");
                    }
                }
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
                StringBuilder build = new StringBuilder();
                try {
                    FileReader fr = new FileReader("src/dragon/ball/res/info/infoEstrattore.html");
                    BufferedReader br = new BufferedReader(fr);

                    String temp = "";

                    while ((temp = br.readLine()) != null) {
                        build.append(temp + "\n");
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(mario.rm.Menu.sprite_estractor.input.Selezione.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Selezione.class.getName()).log(Level.SEVERE, null, ex);
                }
                JEditorPane textArea = new JEditorPane();
                URL resourceUrl;
                try {
                    resourceUrl = new URL("file:"
                            + System.getProperty("user.dir")
                            + System.getProperty("file.separator")
                            + //"src"+
                            //System.getProperty("file.separator")+
                            "src/dragon/ball/res/info/infoEstrattore.html");
                    textArea.setPage(resourceUrl);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Selezione.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Selezione.class.getName()).log(Level.SEVERE, null, ex);
                }
                //textArea.setText(build.toString());
                textArea.setOpaque(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                //textArea.setLineWrap(true);
                //textArea.setWrapStyleWord(true);
                textArea.setEditable(false);
                scrollPane.setPreferredSize(new Dimension(700, 500));
                JOptionPane.showMessageDialog(null, scrollPane, "Informazioni",
                        JOptionPane.YES_NO_OPTION);
        }
        fr.repaint();
        fr.revalidate();
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

        g.setItem(colonna, riga);
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

}
