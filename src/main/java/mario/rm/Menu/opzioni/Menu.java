package mario.rm.Menu.opzioni;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import mario.rm.SuperMario;
import mario.rm.input.Sound;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class Menu extends Impostazioni implements ActionListener, ChangeListener {

    private final SuperMario mario;

    public Menu(SuperMario mario) {
        super();

        setLayout(new GridLayout());

        normal();
        
        /*GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;*/
        
        add(center);

        this.mario = mario;

    }

    protected void normal() {
        center.removeAll();

        center.setLayout(new GridBagLayout());   //layout per mettere bottoni in verticale spaziati
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int w = (int) (5.0 / 960.0 * SuperMario.WIDTH);
        int h = (int) (5.0 / 540.0 * SuperMario.HEIGHT);
        // System.out.println(""+w+" "+h);
        gbc.insets = new Insets(w, h, w, h);    //gli dico di mettermi uno spazio fra ogni bottone

        

        JButton resume = new JButton("RESUME");
        resume.addActionListener(this);
        center.add(resume, gbc);
        
        JButton restart = new JButton("RESTART");
        restart.addActionListener(this);
        center.add(restart, gbc);

        JButton option = new JButton("OPTION");
        option.addActionListener(this);
        center.add(option, gbc);

        JButton home = new JButton("HOME");
        home.addActionListener(this);
        center.add(home, gbc);

        JButton esci = new JButton("ESCI");
        esci.addActionListener(this);
        center.add(esci, gbc);

        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "RESTART":
                mario.createLV(true);
            case "RESUME":
                mario.removeOption();
                break;
            case "OPTION":
                option(false);
                break;
            case "HOME":
                mario.stopGame();
                break;
            case "ESCI":
                System.exit(0);
                break;
            case "Indietro":
                normal();
                break;
            case "Applica":
                applica();
                normal();
                break;
            case "Sound":
                slider.setEnabled(sound.isSelected());
                break;
        }

    }

}
