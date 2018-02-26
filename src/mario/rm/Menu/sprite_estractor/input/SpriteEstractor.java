package mario.rm.Menu.sprite_estractor.input;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import static mario.MainComponent.memoryUsed;
import mario.rm.Menu.Griglia;

/**
 *
 * @author LENOVO
 */
public class SpriteEstractor extends JFrame {

    private static int WIDTH;
    private int HEIGHT;
    private static final String TITLE = "Sprite Estractor";

    private static Selezione s;

    private Setting p;

    private static final int PIXEL = 30;

    private JScrollPane gri;
    
    public static JLabel coordinate;
    
    public static int adaptedWidth = adaptWidth(250);

   // private final GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

    public SpriteEstractor() {
        super(TITLE);

        setAlwaysOnTop(false);

        /*device.setFullScreenWindow(this);
        WIDTH = device.getFullScreenWindow().getWidth() / 16 * 13;
        HEIGHT = device.getFullScreenWindow().getHeight() / 16 * 13;
        device.setFullScreenWindow(null);*/
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        WIDTH = d.width / 16 * 13;
        HEIGHT = d.height / 16 * 13;
        

        setSize(new Dimension(WIDTH, HEIGHT));

        setLayout(new BorderLayout());

        adaptedWidth = adaptWidth(250);
        
        final int adaptedHeight = adaptHeight(250);
System.out.println("pre Griglie: "+memoryUsed());
        Griglia g = new Griglia(WIDTH + adaptedWidth, HEIGHT + adaptedHeight, this, PIXEL);
        g.setLayout(new GridLayout());
System.out.println("dopo griglia: "+memoryUsed());
        gri = new JScrollPane(g);
        gri.setPreferredSize(new Dimension(WIDTH - adaptedWidth, HEIGHT));

        gri.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        gri.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        s = new Selezione(g, this);
        
        p = new Setting(s, HEIGHT);
        JScrollPane panelScroll = new JScrollPane(p);
        panelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //p.setLayout(new GridLayout(8, 4, 25, 25));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(adaptedWidth + 25, HEIGHT - adaptWidth(60)));
        tabbedPane.addTab("Settings", null, panelScroll,
                  "Does nothing");
        tabbedPane.addTab("Strumenti", null, new Strumenti(s, HEIGHT),
                  "Does nothing2");
        
        addMenuBar();

        JPanel east = new JPanel();
        east.add(tabbedPane, BorderLayout.EAST);
        add(east, BorderLayout.EAST);

        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        //center.setBackground(Color.red);
        add(center, BorderLayout.CENTER);

        center.add(gri);
        center.add((coordinate = new JLabel("C:0 R:0 X:0 Y:0")), BorderLayout.SOUTH);

        g.addMouseListener(s);
        g.addMouseMotionListener(s);
        g.setPanel(p);
        
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //setResizable(false);
        
        setVisible(true);
        //System.gc();
        System.out.println("Fine costruzione estrattore: "+memoryUsed());

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

    public static void main(String[] args) {
        new SpriteEstractor();
    }

    public static int adaptWidth(int val) {
        return (int) ((double) val / 1200 * WIDTH);
    }

    public int adaptHeight(int val) {
        return (int) ((double) val / 900 * HEIGHT);
    }

}
