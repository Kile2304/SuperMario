package mario.rm.Menu.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import mario.rm.Menu.Componenti.ScrollButton;
import mario.rm.Menu.Componenti.Scrollable;
import mario.rm.Menu.Griglia;
import mario.rm.Menu.home.Home;
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class Editor extends JFrame implements Scrollable {

    private int HEIGHT;
    private int WIDTH;

    private ScrollButton orizontal;
    private ScrollButton vertical;

    private static Selezione s;
    
    private Pannelli p;

    private static final int PIXEL = 32;
    
    private static final String TITLE = "EDITOR";
    
    private JScrollPane gri;
    
    
    public static void main(String[] args) {
        //new Editor();
    }

    public Editor(Home home) {
        super(TITLE);
        
        
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        WIDTH = d.width;
        HEIGHT = d.height;
        
        setSize(WIDTH, HEIGHT);
        
        setLayout(new BorderLayout());
        
        
        final int adaptedWidth = adaptWidth(250);
        final int adaptedHeight = adaptHeight(250);
        Log.append(adaptedWidth+ " "+adaptedHeight, DefaultFont.ERROR);
        Griglia g = new Griglia(WIDTH + adaptedWidth, HEIGHT + adaptedHeight, this, PIXEL);
        
        gri = new JScrollPane(g);
        gri.setPreferredSize(new Dimension(WIDTH - adaptedWidth, HEIGHT));
        gri.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        gri.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        s = new Selezione(g, this);
        
       
        p = new Pannelli(this, g.getPreview().getMemoria());
        p.setLayout(new GridLayout(8, 2, 25, 25));
        
        JScrollPane sc = new JScrollPane(p);
        sc.setPreferredSize(new Dimension(adaptedWidth, HEIGHT));
        //sc.setSize(300, HEIGHT);
        sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        addMenuBar();

        JPanel east = new JPanel();
        east.add(sc, BorderLayout.EAST);
        add(east, BorderLayout.EAST);
        
        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        //center.setBackground(Color.red);
        add(center, BorderLayout.CENTER);
        
        mario.rm.Menu.Componenti.Selezione c = new mario.rm.Menu.Componenti.Selezione(g, this);
        
        center.add(gri);
        
        g.addMouseListener(s);
        g.addMouseMotionListener(s);
        g.setPanel(p);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setVisible(true);
    }

    public Selezione getSelezione() {
        return s;
    }

    @Override
    public void changeStateOrizontal(int stato) {
        orizontal.changeState(stato);
    }

    @Override
    public void changeStateVertical(int stato) {
        //vertical.changeState(stato);
    }
    
    public void changeState(){
        gri.repaint();
        gri.revalidate();
    }

    private void addMenuBar(){
        JMenuBar bar = new JMenuBar();
        
        JMenu file = new JMenu("File");
        String[] name = new String[]{"New", "Load", "Save", "Home", "Exit"};
        
        
        JMenu help = new JMenu("Help");
        JMenuItem manuale = new JMenuItem("Manuale");
        
        JMenu opzioni = new JMenu("Opzioni");
        JMenuItem option = new JMenuItem("Opzioni");
        JMenuItem clean = new JMenuItem("Clean");
        JMenuItem info = new JMenuItem("Info");
        
        JMenu insert = new JMenu("Insert");
        JMenuItem row = new JMenuItem("Row");
        JMenuItem column = new JMenuItem("Column");
        JMenuItem rowEColumn = new JMenuItem("Row e Column");
        
        JMenu bg = new JMenu("NOBG");
        JMenuItem get = new JMenuItem("Selettore");
        
        
        ArrayList<JMenuItem> item = new ArrayList<>();
        
        for (int i = 0; i < name.length; i++) {
            item.add(new JMenuItem(name[i]));
            item.get(i).addActionListener(p);
            file.add(item.get(i));
            if(i < name.length-1){
                file.addSeparator();
            }
        }
        
        opzioni.add(insert);
        insert.add(row);
        row.addActionListener(p);
        insert.add(column);
        column.addActionListener(p);
        insert.add(rowEColumn);
        rowEColumn.addActionListener(p);
        
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
 
    public int adaptWidth(int val){
        return (int) ((double)val / 1200 * WIDTH);
    }
    public int adaptHeight(int val){
        return (int) ((double)val / 900 * HEIGHT);
    }
    
}
