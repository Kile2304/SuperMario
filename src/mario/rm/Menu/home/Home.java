package mario.rm.Menu.home;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.xml.bind.DatatypeConverter;
import mario.MainComponent;
import mario.rm.Menu.Componenti.Impostazioni;
import mario.rm.Menu.Componenti.bottoni.TranslucentButton;
import mario.rm.Menu.Componenti.Visualizzatore;
import mario.rm.Menu.level_editor.Editor;
import mario.rm.Menu.opzioni.MenuHome;
import mario.rm.Menu.sprite_estractor.input.SpriteEstractor;
import mario.rm.handler.SelectLevel;
import mario.rm.input.Loader;
import mario.rm.multigiocatore.TypeMulti;
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;
import mario.rm.utility.Video;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

/**
 *
 * @author LENOVO
 */
public class Home extends JFrame implements ActionListener {

    private static final String TITLE = "HOME";

    private static final String[] bottonList = new String[]{"INIZIA GIOCO", "MULTIGIOCATORE", "SELEZIONA LIVELLO", "CREA LIVELLO",
        "SPRITE ESTRACTOR", "OPZIONI", "RINGRAZIAMENTI", "ESCI"};

    private static MainComponent main;

    protected BufferedImage background = Loader.LoadImage("Immagini/menu2.jpg");

    private Dimension dim = getToolkit().getScreenSize();

    private final ArrayList<TranslucentButton> list;

    private JPanel all;
    private Impostazioni menuHome;

    Video video;

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
        if (MainComponent.VLC) {
            intro();
        } else {
            home();
        }
        setVisible(true);

        

    }

    public void play(String file){
        video.play(file);
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

    private void intro() {

        add((video = new Video(this)));
        video.getPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                home();
            }
        });
        int volume = Boolean.parseBoolean(MainComponent.settings.getValue("sound"))
                ? Integer.parseInt(MainComponent.settings.getValue("volume"))
                : 0;
        //System.out.println(""+volume);
        video.getPlayer().setVolume(volume);
    }

    public void home() {
        if(video != null) remove(video);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        all = new JPanel() {   //pannello per disegnarmi lo sfondo
            public void paintComponent(Graphics g) {
                g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
            }
        };
        all.setLayout(new GridBagLayout());   //layout per accentrarmi i bottoni
        all.add(bottoni(), gbc);
        add(all);
        revalidate();
        repaint();
    }

    public void reset() {
        remove(menuHome);
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
        dispose();
        new SelectLevel(false).reset();
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
        setVisible(false);
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
        byte[] bytes = DatatypeConverter.parseHexBinary("70616d706c6f6e61");
        try {
            String result = new String(bytes, "UTF8");
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
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private void spriteEstractor() {
        /*JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter a password:");
        JPasswordField pass = new JPasswordField(10);
        panel.add(label);
        panel.add(pass);
        String[] options = new String[]{"OK", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, panel, "Login",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, pass);
        if (option == 0) // pressing OK button
        {
            char[] password = pass.getPassword();
            //System.out.println("Your password is: " + new String(password));
            if (isCorrect(password)) {
                Log.append("WELCOME ADMIN", DefaultFont.INFORMATION);
                dispose();
                JOptionPane.showMessageDialog(rootPane, "Attenzione mentre si usa questa modalita\nsi potrebbe rompere l'intero progetto", "titolo", JOptionPane.ERROR_MESSAGE);
                new SpriteEstractor();
            } else {
                Log.append("PASSWORD: " + new String(password) + ". NON RICONOSCIUTA.", DefaultFont.ERROR);
            }
        }*/
        dispose();
        new SpriteEstractor();
    }

    private void mutltigiocatore() {
        new TypeMulti(main).setVisible(true);
        dispose();
    }

    private JPanel bottoni() {
        JPanel panel = new JPanel();
        panel.setOpaque(false); //gli dico di non disegnarmi lo sfondo

        panel.setLayout(new GridBagLayout());   //layout per mettere bottoni in verticale spaziati
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int w = (int) (5.0 / 960.0 * getWidth());
        int h = (int) (5.0 / 540.0 * getHeight());
        // System.out.println(""+w+" "+h);
        gbc.insets = new Insets(w, h, w, h);    //gli dico di mettermi uno spazio fra ogni bottone

        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (int i = 0; i < bottonList.length; i++) {
            TranslucentButton b = new TranslucentButton(bottonList[i]);

            b.setBgCol(new Color(42, 82, 190));
            b.setBgCol(new Color(18, 10, 143)); //background

            b.setBgColro(Color.GRAY);

            b.setFgCol(new Color(127, 255, 212));   //bordi e scritte

            b.setFgColsel(Color.BLACK);

            b.addActionListener(this);
            list.add(b);
            panel.add(b, gbc);
        }
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
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
                creaLivello();
                break;
            case "SPRITE ESTRACTOR":
                spriteEstractor();
                break;
            case "OPZIONI":
                this.remove(all);
                this.add((menuHome = new MenuHome(this)));
                repaint();
                revalidate();
                break;
            case "RINGRAZIAMENTI":
                ringraziamenti();
                break;
            case "ESCI":
                esci();
                break;
        }
    }

}
