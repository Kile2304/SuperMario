package mario.rm.Menu.home;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import mario.MainComponent;
import mario.rm.Menu.Componenti.Visualizzatore;
import mario.rm.Menu.editor.Editor;
import mario.rm.Menu.sprite_estractor.input.SpriteEstractor;
import mario.rm.handler.SelectLevel;
import mario.rm.multigiocatore.TypeMulti;

/**
 *
 * @author LENOVO
 */
public class Home extends JFrame implements ActionListener{
    
    private static final String TITLE = "HOME";
    
    private static final String[] bottonList = new String[]{"INIZIA GIOCO","MULTIGIOCATORE","SELEZIONA LIVELLO","CREA LIVELLO",
        "SPRITE ESTRACTOR","RINGRAZIAMENTI","ESCI"};
    
    private static MainComponent main;

    private JPasswordField passwordField;

    private Dimension dim = getToolkit().getScreenSize();

    /**
     * Creates new form Home
     *
     * @param main
     */
    public Home() {
        super(TITLE);
        setLayout(new BorderLayout());
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setResizable(false);

        Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);
        JPanel panel = new JPanel();
        panel.add(bottoni());
        add(panel, BorderLayout.CENTER);
        //add(bottoni(), BorderLayout.NORTH);
        
        setVisible(true);

    }
    
    public void setMainComponent(MainComponent main){
        this.main = main;
    }

    public void inizia(String path) {
        new SelectLevel(path);
        System.out.println("" + path);
        dispose();
        main.start();
    }                       

    private void start() {                                               
        dispose();
        new SelectLevel(false).reset();
        main.start();
    }                                              

    private void esci() {                                               
        System.exit(0);
    }                                              

    private void ringraziamenti() {                                               
        FileInputStream fis = null;
        //System.out.println(""+(this.getClass().getResource("../")));
        try {
            System.out.println("Path:" + (new File(MainComponent.jarPath.getAbsolutePath() + "/res/crediti/ringraziamenti.txt").getAbsolutePath()));
            fis = new FileInputStream(new File(MainComponent.jarPath.getAbsolutePath() + "/res/crediti/ringraziamenti.txt").getAbsolutePath());
        } catch (FileNotFoundException ex) {
            System.out.println("Errore in caricare in memoria il file: " + new File(MainComponent.jarPath.getAbsolutePath() + "/res/crediti/ringraziamenti.txt").getAbsolutePath());
        }
        InputStreamReader isr = new InputStreamReader(fis);
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
        String password = "pamplona";
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
        JPanel panel = new JPanel();
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
                System.out.println("WELCOME ADMIN");
                dispose();
                JOptionPane.showMessageDialog(rootPane, "Attenzione mentre si usa questa modalita\nsi potrebbe rompere l'intero progetto", "titolo", JOptionPane.ERROR_MESSAGE);
                new SpriteEstractor();
                
            }
        }
    }                                              

    private void mutltigiocatore() {                                               
        new TypeMulti().setVisible(true);
        dispose();
    }                    

    private JPanel bottoni() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        for (int i = 0; i < bottonList.length; i++) {
            JButton b = new JButton(bottonList[i]);
            b.addActionListener(this);
            panel.add(b);
        }
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "INIZIA GIOCO":
                start();
                break;
            case "MULTIGIOCATORE":
                mutltigiocatore();
                break;
            case "SELEZIONE LIVELLO":
                visualizza();
                break;
            case "CREA LIVELLO":
                creaLivello();
                break;
            case "SPRITE ESTRACTOR":
                spriteEstractor();
                break;
            case "RIGRAZIAMENTI":
                ringraziamenti();
                break;
            case "ESCI":
                esci();
                break;
        }
    }
    
}
