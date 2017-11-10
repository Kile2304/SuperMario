package mario.rm.Menu.opzioni;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

        setLayout(new GridBagLayout());

        normal();
        
        add(center, new GridBagConstraints());

        this.mario = mario;

    }

    private void normal() {
        center.removeAll();

        center.setPreferredSize(new Dimension(SuperMario.WIDTH / 5, SuperMario.HEIGHT / 3));
        
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JButton resume = new JButton("RESUME");
        resume.addActionListener(this);
        center.add(resume);

        JButton option = new JButton("OPTION");
        option.addActionListener(this);
        center.add(option);

        JButton home = new JButton("HOME");
        home.addActionListener(this);
        center.add(home);

        JButton esci = new JButton("ESCI");
        esci.addActionListener(this);
        center.add(esci);

        revalidate();
        repaint();
    }

    private void option() {
        center.removeAll();
        center.setPreferredSize(new Dimension(SuperMario.WIDTH / 3, SuperMario.HEIGHT / 3));

        center.add((sound = new JCheckBox("Sound")));
        sound.setSelected(Sound.soundON);
        
        float vol = Sound.getVolume().getValue();
        vol += Math.abs(Sound.getVolume().getMinimum());
        float range = Math.abs(Sound.getVolume().getMinimum()) + Math.abs(Sound.getVolume().getMaximum());
        vol = (float) (100.0 / range * vol);

        slider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) vol);
        slider.setMajorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.addChangeListener(this);
        center.add(slider);
        
        center.add((current = new JLabel("Il volume corrente e': " + (int) vol+"%")));
        
        JButton indietro = new JButton("Indietro");
        indietro.addActionListener(this);
        center.add(indietro);
        
        JButton applica = new JButton("Applica");
        applica.addActionListener(this);
        center.add(applica);

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
        System.out.println("Convertito in db: "+temp);
        Sound.setVolume(temp);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
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
        }

    }

}
