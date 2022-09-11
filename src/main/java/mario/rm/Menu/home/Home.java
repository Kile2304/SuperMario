package mario.rm.Menu.home;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import Connessione.Connessione;
import Connessione.Login;
import Connessione.Profilo;
import mario.MainComponent;
import mario.rm.Menu.Componenti.Visualizzatore;
import mario.rm.Menu.Componenti.bottoni.RoundedCornerButton;
import mario.rm.Menu.Componenti.bottoni.TranslucentButton;
import mario.rm.Menu.level_editor.Editor;
import mario.rm.Menu.opzioni.Impostazioni;
import mario.rm.Menu.opzioni.MenuHome;
import mario.rm.Menu.sprite_estractor.input.SpriteEstractor;
import mario.rm.handler.SelectLevel;
import mario.rm.input.Loader;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Font;
import mario.rm.utility.Font.ColorName;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class Home extends JFrame implements ActionListener {

    private static final String TITLE = "HOME";

    private static final Object[] bottonList = new Object[]{
        "INIZIA GIOCO",
        "MULTIGIOCATORE",
        "SELEZIONA LIVELLO",
        "CREA LIVELLO",
        "SPRITE ESTRACTOR",
        "SETTINGS",
//        new ImageIcon(MainComponent.class.getResource("Immagini/settings.png")),
        "RINGRAZIAMENTI",
        "X",
        "Sito Web"
    };

    private static MainComponent main;

    protected BufferedImage background = Loader.LoadImage("Immagini/menu2.jpg");

    private Dimension dim = getToolkit().getScreenSize();

    private final ArrayList<TranslucentButton> list;

    private JPanel all;
    private Impostazioni menuHome;

    //Video video;
    /**
     * Creates new form Home
     *
     * @param main
     */
    public Home() {
        super(TITLE);

        this.list = new ArrayList<>();

        setLayout(new GridLayout());    //layout per avere un pannello a pieno schermo e disegnarmi il background

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        sizer();

        setIconImage(Loader.LoadImage("Immagini/Luma-Yellow-icon.png"));

        setUndecorated(true);   //tolgo barre x _ ed il resto
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        if (MainComponent.loading) {
            //intro();
            loading();
        } else {
            home();
        }
        setVisible(true);

    }

    private void loading() {
        URL url = MainComponent.class.getClassLoader().getResource("Immagini/main_load.gif");
        Icon icon = new ImageIcon(url);
        JLabel label = new JLabel(icon);
        label.setBounds(0, 0, getWidth(), getHeight());
        add(label);
    }

    private void sizer() {
        boolean fullscreen = Boolean.parseBoolean(MainComponent.settings.getValue("fullscreen"));
        int scale = Integer.parseInt(MainComponent.settings.getValue("scale"));
        if (fullscreen) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            scale = 1;
        }
        setSize(dim.width / scale, dim.height / scale);
        setLocationRelativeTo(null);
    }

    public void home() {

        all = new JPanel() {   //pannello per disegnarmi lo sfondo
            public void paintComponent(Graphics g) {
                g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
            }
        };
        all.setLayout(new GridLayout());
        //all.setBounds(0, 0, getWidth(), getHeight());
        all.add(bottoni());
        add(all);
        revalidate();
        repaint();
    }

    public void reset() {
        remove(menuHome);
        sizer();
        all = null;
        home();
        add(all);
        sizer();
        revalidate();
        repaint();
    }

    public void setMainComponent(MainComponent main) {
        this.main = main;
    }

    public void inizia(String path) {
        new SelectLevel(path);
        Log.append(path, DefaultFont.INFORMATION);
        dispose();
        main.start(1);
    }

    private void start() {
        list.clear();
        all = null;
        dispose();
        SelectLevel.reset();
        main.start(1);
    }

    private void esci() {
        System.exit(0);
    }

    private void ringraziamenti() {
        FileInputStream fis = null;
        //System.out.println(""+(this.getClass().getResource("../")));
        InputStreamReader isr = new InputStreamReader(MainComponent.class.getClassLoader().getResourceAsStream("crediti\\crediti.txt"));
        BufferedReader br = new BufferedReader(isr);

        String line = "";
        //String text = "";
        StringBuffer text = new StringBuffer();
        try {
            while ((line = br.readLine()) != null) {
                text.append(line + "\n");
            }
            isr.close();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        setVisible(false);
        JOptionPane.showMessageDialog(rootPane, text, "Riconoscimenti", 0);
        setVisible(true);
    }

    private void visualizza() {
        dispose();
        new Visualizzatore(dim.width, dim.height, this);
    }

    private void creaLivello() {
        dispose();
        new Editor(this);
    }

    private boolean isCorrect(char[] input) {
        String password = null;
        /*String hex=null;
        try {
            hex = String.format("%040x", new BigInteger(1, "here<<<<2.getBytes("UTF8")));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(""+hex);*/
 /*try {
            //System.out.println(""+hex+" "+Integer.toHexString(Integer.parseInt(hex)));
            System.out.println(""+new String(hex.getBytes(), "UTF8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        String result = convertStringToHex("70616d706c6f6e61");
        //System.out.println(""+(int)result.charAt(0));
        StringBuilder s = new StringBuilder();
        s.append(result);
        for (int i = 0; i < s.length(); i++) {
            if ((int) s.charAt(i) == 0) {
                s.delete(i, i + 1);
                i--;
            } else {
                break;
            }
        }
        //System.out.println(""+s);
        password = s.toString();
        char[] converted = password.toCharArray();

        boolean correct = true;

        if (input.length != converted.length) {
            correct = false;
        }
        if (correct) {
            for (int i = 0; i < converted.length; i++) {
                if (converted[i] != input[i]) {
                    correct = false;
                    break;
                }
            }
        }
        return correct;
    }
    
    private static String convertStringToHex(String str) {
        StringBuilder sb = new StringBuilder();

        char[] charArray = str.toCharArray();

        for (char c : charArray) {
            String charToHex = Integer.toHexString(c);
            sb.append(charToHex);
        }

        return sb.toString();
    }

    private void spriteEstractor() {
        dispose();
        new SpriteEstractor();
    }

    private void mutltigiocatore() {
        new SelectLevel(false).reset();
        dispose();
        main.start(2);
    }

    private JPanel bottoni() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, getWidth(), getHeight());
        panel.setOpaque(false); //gli dico di non disegnarmi lo sfondo

        final int[] width = new int[]{
            adaptWidth(170),
            adaptWidth(220),
            adaptWidth(180),
            adaptWidth(180),
            adaptWidth(180),
            adaptWidth(50),
            0,
            adaptWidth(50),
            adaptWidth(170)
        };
        final int[] height = new int[]{
            adaptHeight(60),
            adaptHeight(60),
            adaptHeight(60),
            adaptHeight(60),
            adaptHeight(60),
            adaptHeight(50),
            0,
            adaptHeight(50),
            adaptHeight(60)
        };

        final int[] x = new int[]{
            (getWidth() - width[0]) / 2,
            (getWidth() - width[1]) / 2,
            (getWidth() - width[1]) / 2 - width[2],
            (getWidth() + width[1]) / 2,
            adaptWidth(20),
            getWidth() - width[5] - adaptWidth(20),
            0,
            (getWidth() - width[7]) / 2,
            (getWidth() - width[8]) / 2
        };
        final int[] y = new int[]{
            (getHeight() - height[0]) / 2,
            (getHeight() + height[1]) / 2,
            (getHeight() + height[1]) / 2,
            (getHeight() + height[1]) / 2,
            getHeight() - height[4] - adaptHeight(10),
            getHeight() - height[5] - adaptHeight(10),
            0,
            getHeight() - height[7] - adaptHeight(10),
            (getHeight() + height[8]) / 2 + height[1]
        };

        final java.awt.Font f = new java.awt.Font("arial", java.awt.Font.BOLD, adaptWidth(14));
        for (int i = 0; i < bottonList.length; i++) {
            /*JButton temp = new JButton(bottonList[i]);
            temp.setBounds(x[i], y[i], width[i], height[i]);
            System.out.println("x: " + x[i] + " y: " + y[i] + " width: " + width[i] + " height: " + height[i]);
            temp.addActionListener(this);*/
            JButton temp = null;
            if (bottonList[i] instanceof String) {
                temp = new RoundedCornerButton((String) bottonList[i]);
            } else {
                Icon newimg = new ImageIcon(((ImageIcon)bottonList[i]).getImage().getScaledInstance(width[i], height[i], java.awt.Image.SCALE_SMOOTH));
                temp = new JButton(newimg);
                temp.setActionCommand("OPZIONI");
                temp.setBorder(null);
                temp.setContentAreaFilled(false);
            }
            temp.setBounds(x[i], y[i], width[i], height[i]);
            temp.setBackground(Color.LIGHT_GRAY);
            temp.addActionListener(this);
            temp.setFont(f);

            panel.add(temp);
        }

        /*Profilo.looged = true;
        Profilo.username = "carpaccio";*/
        String temp = Profilo.looged ? "Loggato come: " + Profilo.username : "Nessuno loggato";
        JLabel label = new JLabel(Profilo.looged ? Font.setHtmlColor(temp, ColorName.GREEN) : Font.setHtmlColor(temp, ColorName.RED));
        int strWi = label.getFontMetrics(label.getFont()).stringWidth(temp);
        label.setBounds(
                getWidth() - adaptWidth(10) - strWi - adaptWidth(120) - adaptWidth(50),
                adaptHeight(50),
                adaptWidth(250),
                adaptHeight(60));
        panel.add(label);
        //JButton bb = new JButton(Profilo.looged ? "LOGOUT" : "LOGIN");
        RoundedCornerButton bb = new RoundedCornerButton(!Profilo.looged ? "LOGIN" : "LOGOUT");
        bb.setBackground(Color.LIGHT_GRAY);
        bb.setFont(f);
        bb.setBounds(
                getWidth() - adaptWidth(120) - adaptWidth(50),
                adaptHeight(50),
                adaptWidth(120),
                adaptHeight(60));
        bb.addActionListener(this);
        panel.add(bb);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "LOGIN":
                if (Connessione.isConnected()) {
                    dispose();
                    new Login(this.getSize());
                } else System.out.println("break");
                break;
            case "LOGOUT":
                Profilo.logout();
                remove(all);
                all = null;
                home();
                repaint();
                revalidate();
                break;
            case "INIZIA GIOCO":
                start();
                break;
            case "MULTIGIOCATORE":
                mutltigiocatore();
                break;
            case "SELEZIONA LIVELLO":
                visualizza();
                break;
            case "CREA LIVELLO":
                System.out.println("asdasdas");
                creaLivello();
                break;
            case "SPRITE ESTRACTOR":
                spriteEstractor();
                break;
            case "OPZIONI":
                this.remove(all);
                menuHome = new MenuHome(this);
                add(menuHome);
                repaint();
                revalidate();
                break;
            case "RINGRAZIAMENTI":
                ringraziamenti();
                break;
            case "Sito Web":
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(new URI("http://localhost/SuperMario/index.php"));
                    } else {
                        System.err.println("Isn't desktop supported");
                    }
                } catch (IOException | URISyntaxException ex) {
                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "X":
                esci();
                break;
        }
    }

    public int adaptWidth(int val) { //RIADATTO LA LARGHEZZA DELLE IMMAGINI IN BASE ALLA GRANDEZZA DELLO SCHERMO
        return (int) ((double) val / 1200.0 * getWidth());
    }

    public int adaptHeight(int val) {    //RIADATTA LA ALTEZZA DELLE IMMAGINI IN BASE ALLA GRANDEZZA DELLO SCHERMO
        return (int) ((double) val / 900.0 * getHeight());
    }

}
