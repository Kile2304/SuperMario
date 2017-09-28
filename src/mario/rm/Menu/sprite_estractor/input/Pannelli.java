package mario.rm.Menu.sprite_estractor.input;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mario.rm.Menu.Componenti.Checkable;
import mario.rm.Menu.Specifiche;
import mario.rm.identifier.Direction;
import mario.rm.identifier.Move;
import mario.rm.identifier.TilePart;
import mario.rm.identifier.Type;
import mario.rm.utility.RGB;

/**
 *
 * @author LENOVO
 */
public class Pannelli extends JPanel implements Checkable, ActionListener { //PANNELLO CONTENENTE IL MENU PER L'ESTRATTORE

    private Selezione s;    //vero e proprio listener
    
    public static JTextField type; //textfielf per inserire il Type
    public static JComboBox move;   //combobox dove si scegliera' il tipo di movimento
    public static JComboBox direction;  //combobox per scegliere la direzione
    public static JTextField fileName;  //textfield per scegliere il nome del file
    public static JComboBox transformation; //indica se si e' trasformato in qualcosa o e' normale
    public static JCheckBox isPlayer; //se e' selezionato stiamo trattando con un tile
    
    public static JComboBox tile;   //direzione specifica per i tile
    public static JCheckBox isUnlockable;
    public static JComboBox unlockType;
    
    private ArrayList<JLabel> lb;   //server per eliminare le jlabel quando viene selezionata la checkbox

    public Pannelli(Selezione s) {
        super();
        this.s = s;
        lb = new ArrayList<>();
        home();
    }

    private void home() {

        BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                img.setRGB(i, j, 255);
            }
        }

        elenco.add(new Specifiche("+"));
        elenco.add(new Specifiche("-"));

        elenco.add(new Specifiche("DELETE POINT"));
        elenco.add(new Specifiche("CREATE POINT", img, Type.VOID));

        for (Specifiche jButton : elenco) {
            jButton.getButton().addActionListener(this);
            add(jButton.getButton());
        }

        type = new JTextField();

        ArrayList<String> l = new ArrayList<>();

        for (Move m : Move.values()) {
            l.add(m.name());
        }

        String[] mov = new String[l.size()];

        for (int i = 0; i < mov.length; i++) {
            mov[i] = l.get(i);
        }
        l.clear();
        for (Direction dir : Direction.values()) {
            l.add(dir.getName());
        }
        String[] mov1 = new String[l.size()];
        for (int i = 0; i < mov1.length; i++) {
            mov1[i] = l.get(i);
        }
        direction = new JComboBox(mov1);
        move = new JComboBox(mov);

        isPlayer = new JCheckBox("Player", true);
        isPlayer.addActionListener(this);

        add(new JLabel("Inserisci il type:"));
        add(type);

        add(new JLabel("Inserisci il nome del file:"));
        add((fileName = new JTextField()));

        add(new JLabel("Attiva la per la modalita' player"));
        add(isPlayer);

        addPlayer();
        
        TilePart[] t = TilePart.values();
        mov = new String[t.length];
        for (int i = 0; i < mov.length; i++) {
            mov[i] = t[i].name();
        }
        tile = new JComboBox(mov);
        
        unlockType = new JComboBox(new String[]{"COIN","MUSHROOM","LIFE"});

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Specifiche s = new Specifiche();
        
        switch (e.getActionCommand()) {
            case "Player":
                if (isPlayer.isSelected()) {
                    addPlayer();
                } else {
                    remPlayer();
                }
                this.s.repaint();
                break;
            case "Unlockable":
                if(isUnlockable.isSelected()){
                    add(unlockType);
                    //setLayout(new GridLayout(7, 4, 25, 25));
                }else{
                    remove(unlockType);
                    unlockType.setSelectedIndex(0);
                    //setLayout(new GridLayout(7, 4, 25, 25));
                }
                this.s.repaint();
                break;
        }
        
        if ((e.getSource() instanceof JButton)) {
            s.setButton((JButton) e.getSource());
            
            elenco.stream().forEach((specifiche) -> {
                if (s.getButton().toString().equals(specifiche.getButton().toString())) {
                    specifiche.setCheck();
                } else {
                    specifiche.unCheck();
                }
            });
            s.setCheck();
        }
        
        try {
            this.s.action(e.getActionCommand());
        } catch (IOException ex) {
            Logger.getLogger(Pannelli.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Specifiche getChecked() {
        for (Iterator<Specifiche> it = elenco.iterator(); it.hasNext();) {
            Specifiche specifiche = it.next();
            if (specifiche.isCheck()) {
                return specifiche;
            }
        }
        return null;
    }

    public void addPlayer() {

        if (tile != null) {
            remove(tile);
            tile.setSelectedIndex(0);
            remove(lb.get(0));
            lb.clear();
        }
        if(isUnlockable != null){
            if(isUnlockable.isSelected()){
                isUnlockable.setSelected(false);
                remove(isUnlockable);
                remove(unlockType);
                unlockType.setSelectedIndex(0);
            }else{
                remove(isUnlockable);
            }
        }

        lb.add(new JLabel("Scegli il tipo di movimento:"));
        add(lb.get(lb.size() - 1));
        add(move);

        lb.add(new JLabel("Scegli la direzione dello sprite:"));
        add(lb.get(lb.size() - 1));
        add(direction);

        lb.add(new JLabel("Scegli il tipo di trasformazione:"));
        add(lb.get(lb.size() - 1));
        add((transformation = new JComboBox(new String[]{"normal", "transformation", "trasformation2", "trasformation3"})));
        setLayout(new GridLayout(8, 4, 25, 25));
    }

    public void remPlayer() {
        remove(move);
        remove(direction);
        remove(transformation);
        for (JLabel l : lb) {
            remove(l);
        }
        lb.clear();
        lb.add(new JLabel("Inserisci posizionamento tiles"));
        add(lb.get(0));
        add(tile);
        add(isUnlockable = new JCheckBox("Unlockable", false));
        isUnlockable.addActionListener(this);
        setLayout(new GridLayout(7, 4, 25, 25));
    }

}
