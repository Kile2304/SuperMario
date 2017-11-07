package mario.rm.Menu.sprite_estractor.input;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import mario.rm.Menu.Componenti.ScrollButton;
import mario.rm.Menu.Componenti.Scrollable;
import mario.rm.Menu.Griglia;

/**
 *
 * @author LENOVO
 */
public class SpriteEstractor extends JFrame implements Scrollable {

    private int WIDTH;
    private int HEIGHT;
    private static final String TITLE = "Sprite Estractor";

    private ScrollButton orizontal;
    private ScrollButton vertical;

    private static Selezione s;

    private Pannelli p;

    private static final int PIXEL = 30;

    private JScrollPane gri;

    private final GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

    public SpriteEstractor() {
        super(TITLE);

        setAlwaysOnTop(false);

        device.setFullScreenWindow(this);
        WIDTH = device.getFullScreenWindow().getWidth() / 16 * 13;
        HEIGHT = device.getFullScreenWindow().getHeight() / 16 * 13;
        device.setFullScreenWindow(null);

        setSize(new Dimension(WIDTH, HEIGHT));

        setLayout(new BorderLayout());

        final int adaptedWidth = adaptWidth(250);
        final int adaptedHeight = adaptHeight(250);

        Griglia g = new Griglia(WIDTH + adaptedWidth, HEIGHT + adaptedHeight, this, PIXEL);
        g.setLayout(new GridLayout());

        gri = new JScrollPane(g);
        gri.setPreferredSize(new Dimension(WIDTH - adaptedWidth, HEIGHT));

        gri.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        gri.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        s = new Selezione(g, this);

        p = new Pannelli(s);
        p.setLayout(new GridLayout(8, 4, 25, 25));

        addMenuBar();

        JPanel east = new JPanel();
        east.add(p, BorderLayout.EAST);
        add(east, BorderLayout.EAST);

        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        //center.setBackground(Color.red);
        add(center, BorderLayout.CENTER);

        center.add(gri);

        g.addMouseListener(s);
        g.setPanel(p);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setVisible(true);

    }

    private void addMenuBar() {

        String[] name = new String[]{"New", "Load", "Extract", "toAcFile", "testAnim", "Home", "Exit"};

        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu opzioni = new JMenu("Opzioni");
        JMenu help = new JMenu("Help");

        JMenuItem option = new JMenuItem("Opzioni");
        JMenuItem clean = new JMenuItem("Clean");
        JMenuItem info = new JMenuItem("Info");

        JMenu bg = new JMenu("NOBG");
        JMenuItem get = new JMenuItem("Selettore");
        JMenuItem manuale = new JMenuItem("Manuale");

        ArrayList<JMenuItem> item = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            item.add(new JMenuItem(name[i]));
            item.get(i).addActionListener(p);
            file.add(item.get(i));
            if (i < name.length - 1) {
                file.addSeparator();
            }
        }

        opzioni.add(option);
        opzioni.add(clean);
        clean.addActionListener(p);
        bg.add(get);
        get.addActionListener(p);
        bg.add(manuale);
        manuale.addActionListener(p);
        opzioni.add(bg);

        info.addActionListener(p);
        help.add(info);
        //bg.addActionListener(p);

        bar.add(file);
        bar.add(opzioni);
        bar.add(help);

        setJMenuBar(bar);
    }

    public static Selezione getSelezione() {
        return s;
    }

    public ScrollButton getScrollHorizontal() {
        return orizontal;
    }

    public ScrollButton getScrollVertical() {
        return vertical;
    }

    @Override
    public void changeStateOrizontal(int stato) {
        orizontal.changeState(stato);
    }

    @Override
    public void changeStateVertical(int stato) {
        vertical.changeState(stato);
    }

    public static void main(String[] args) {
        new SpriteEstractor();
    }

    @Override
    public void changeState() {
        gri.repaint();
        gri.revalidate();
    }

    public int adaptWidth(int val) {
        return (int) ((double) val / 1200 * WIDTH);
    }

    public int adaptHeight(int val) {
        return (int) ((double) val / 900 * HEIGHT);
    }

}
