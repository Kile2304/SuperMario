package mario.rm.Menu.Componenti;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import mario.rm.Menu.Griglia;
import mario.rm.Menu.home.Home;
import mario.rm.handler.SelectLevel;

/**
 *
 * @author LENOVO
 */
public class Visualizzatore extends JFrame implements Scrollable, ActionListener {

    private SelectLevel livelli;

    private ScrollButton orizontal;
    private ScrollButton vertical;

    private Home home;

    private Griglia g;

    public Visualizzatore(int width, int height, Home home) {
        super("Anteprima");

        setLayout(null);

        setSize(width, height);
        setLocationRelativeTo(null);

        this.home = home;

        livelli = new SelectLevel(true);

        g = new Griglia(this, livelli.getNext(), 15);
        g.setBounds(0, 0, width - adaptWidth(420), height - adaptHeight(80));

        Selezione s = new Selezione(g, this);

        //rizontal = new ScrollButton(ScrollButton.ORIZZONTALE, width, height, s, 0);
        //vertical = new ScrollButton(ScrollButton.VERTICALE, width, height, s, adaptWidth(550));

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //add(orizontal);
        //add(vertical);
        add(g);
        add(controlli(width, height));

        setVisible(true);

    }

    public JPanel controlli(int width, int height) {
        JPanel panel = new JPanel();
        System.out.println("sadasd");
        panel.setBounds(adaptWidth(1561), 0, width - adaptHeight(1561), height);
        
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JButton next = new JButton("NEXT");
        JButton before = new JButton("BEFORE");
        JButton home = new JButton("HOME");
        JButton inizia = new JButton("INIZIA");
        JButton ingrandisci = new JButton("+");
        JButton diminuisci = new JButton("-");

        next.addActionListener(this);
        before.addActionListener(this);
        home.addActionListener(this);
        inizia.addActionListener(this);
        ingrandisci.addActionListener(this);
        diminuisci.addActionListener(this);
        
        gbc.fill = GridBagConstraints.BOTH;
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(next, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(before, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(home, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(inizia, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(ingrandisci, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(diminuisci, gbc);

        return panel;
    }

    @Override
    public void changeStateOrizontal(int stato) {
        orizontal.changeState(stato);
    }

    @Override
    public void changeStateVertical(int stato) {
        vertical.changeState(stato);
    }
    
    @Override
    public void changeState() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "INIZIA":
                dispose();
                String current = livelli.getCurrent().substring(livelli.getCurrent().indexOf("Luigi\\Level\\"), livelli.getCurrent().lastIndexOf("."))+ ".level";
                System.out.println("asdas: "+current);
                //current = current.substring(current.lastIndexOf("\\Immagini"), current.length());
                home.inizia(current);
                break;
            case "HOME":
                home.setVisible(true);
                dispose();
                break;
            case "NEXT":
                g.load(livelli.getNext());
                repaint();
                revalidate();
                break;
            case "BEFORE":
                g.load(livelli.getBefore());
                repaint();
                revalidate();
                break;
            case "+":
                g.increasePixel();
                repaint();
                revalidate();
                break;
            case "-":
                g.decreasePixel();
                repaint();
                revalidate();
                break;
        }
    }
    
    private int adaptWidth(int val){
        return (int) ((double) val / 1920 * getWidth());
    }
    private int adaptHeight(int val){
        return (int) ((double) val / 1080 * getHeight());
    }


}
