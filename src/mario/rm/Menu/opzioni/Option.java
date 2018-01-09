/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario.rm.Menu.opzioni;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import mario.rm.SuperMario;
import mario.rm.input.Sound;

/**
 *
 * @author LENOVO
 */
public abstract class Option extends JPanel implements ActionListener, ChangeListener{  //non ncora fatto
    
    private JCheckBox sound;
    private JSlider slider;
    private JLabel current;
    
    public Option(){
        
        setLayout(new GridLayout());
        
        JPanel center = new JPanel();
        
        add(center);
        
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
    
    
    
}
