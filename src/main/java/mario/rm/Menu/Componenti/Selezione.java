package mario.rm.Menu.Componenti;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mario.rm.Menu.Griglia;
import mario.rm.Menu.home.Home;
import mario.rm.Menu.sprite_estractor.input.Setting;
import mario.rm.utility.Punto;

/**
 *
 * @author LENOVO
 */
public class Selezione implements MouseListener, ActionListener, MouseMotionListener {

    private static Griglia g;
    private final JFrame ed;

    private ArrayList<Punto> punto;

    private static final int spostamento = 5;

    private static boolean att = false;

    public Selezione(Griglia g, JFrame ed) {
        this.g = g;
        this.ed = ed;
        punto = new ArrayList<>();
    }

    public static void changeCollider() {
        g.changeCollider();
    }

    public static void XORAtt() {
        att = Boolean.logicalXor(att, true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int colonna = e.getX() / g.getPixel() + g.getMovX();
        int riga = e.getY() / g.getPixel() + g.getMovY();

        if (!att) {
            g.setItem(colonna, riga);
        } else {
            JFileChooser c = new JFileChooser(new File("src/dragon/ball/res/action"));
            int valid = c.showOpenDialog(null);
            
            if(valid == JFileChooser.APPROVE_OPTION){
                //g.attach(c.getSelectedFile(), colonna, riga);
            }
            
            att = false;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        punto.clear();
        ed.repaint();
        ed.revalidate();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
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
            case "Home":
                ed.dispose();
                //new Home();   creare metodo new home
                Setting.elenco.clear();
                break;
            case "Exit":
                System.exit(0);
                break;
            
        }
        ed.repaint();
        ed.revalidate();
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
        ed.repaint();
        ed.revalidate();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
