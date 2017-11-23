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
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class Menu extends JPanel implements ActionListener, ChangeListener {

    private final SuperMario mario;
    private static final JPanel center = new JPanel();
    private JCheckBox sound;
    private JSlider slider;
    private JLabel current;

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

    private void normal() {
        center.removeAll();

        //center.setPreferredSize(new Dimension(SuperMario.WIDTH / 5, SuperMario.HEIGHT / 3));
        
        //center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        
        
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

    private void option() {
        center.removeAll();
        //center.setPreferredSize(new Dimension(SuperMario.WIDTH / 3, SuperMario.HEIGHT / 3));

        //center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        
        center.setLayout(new GridBagLayout()); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int w = (int) (5.0 / 960.0 * SuperMario.WIDTH);
        int h = (int) (5.0 / 540.0 * SuperMario.HEIGHT);
        // System.out.println(""+w+" "+h);
        gbc.insets = new Insets(w, h, w, h);    //gli dico di mettermi uno spazio fra ogni bottone
        
        center.add((sound = new JCheckBox("Sound")));
        sound.setSelected(Sound.soundON);
        sound.addActionListener(this);
        
        float vol = Sound.getVolume().getValue();
        vol += Math.abs(Sound.getVolume().getMinimum());
        float range = Math.abs(Sound.getVolume().getMinimum()) + Math.abs(Sound.getVolume().getMaximum());
        vol = (float) (100.0 / range * vol);

        slider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) vol);
        slider.setMajorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.addChangeListener(this);
        slider.setEnabled(sound.isSelected());
        center.add(slider, gbc);
        
        center.add((current = new JLabel("Il volume corrente e': " + (int) vol+"%")), gbc);
        
        JButton indietro = new JButton("Indietro");
        indietro.addActionListener(this);
        center.add(indietro, gbc);
        
        JButton applica = new JButton("Applica");
        applica.addActionListener(this);
        center.add(applica, gbc);

        revalidate();
        repaint();
    }

    public void stateChanged(ChangeEvent e) {
        int sliderValue = slider.getValue();
        current.setText("Il volume corrente e': " + sliderValue+"%");
    }

    private void applica() {
        Sound.soundON = sound.isSelected();
        
        float value = slider.getValue();
        float range = Math.abs(Sound.getVolume().getMinimum()) + Math.abs(Sound.getVolume().getMaximum());
        float temp = (float) (((value / 100.0) * range) - Math.abs(Sound.getVolume().getMinimum()));
        Log.append("Convertito in db: "+temp, DefaultFont.INFORMATION);
        Sound.setVolume(temp);
        
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
                option();
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
