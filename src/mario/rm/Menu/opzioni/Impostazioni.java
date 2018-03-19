/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario.rm.Menu.opzioni;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import mario.MainComponent;
import mario.rm.SuperMario;
import mario.rm.input.Loader;
import mario.rm.input.Sound;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Ini;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public abstract class Impostazioni extends JPanel implements ChangeListener, ActionListener{
    
    protected static final JPanel center = new JPanel();
    protected JCheckBox sound;
    protected JSlider slider;
    protected JLabel current;
    protected static BufferedImage img = Loader.LoadImage("Immagini/settings.jpg");
    protected Ini settings;
    protected JCheckBox full;
    
    public Impostazioni(){
        center.setOpaque(false);
    }
    
    protected abstract void normal();
    
    protected void option(boolean isHome) {
        settings = MainComponent.settings;
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
        
        center.add((sound = new JCheckBox(changeColor("Sound", "yellow"))));
        sound.setSelected(Boolean.parseBoolean(settings.getValue("sound")));
        
        sound.addActionListener(this);
        
        int vol = Integer.parseInt(settings.getValue("volume"));
        slider = new JSlider(JSlider.HORIZONTAL, 0, 100, vol);
        slider.setMajorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.addChangeListener(this);
        slider.setEnabled(Boolean.parseBoolean(settings.getValue("sound")));
        center.add(slider, gbc);
        
        
        center.add((current = new JLabel(changeColor("Il volume corrente e': " + (int) vol+"%", "yellow"))), gbc);
        
        if(isHome){
            (full = new JCheckBox(changeColor("Fullscreen", "yellow")))
                    .setSelected(Boolean.parseBoolean(settings.getValue("fullscreen")));
            //center.add(new JLabel(changeColor("Fullscreen", "yellow")));
            center.add(full);
        }
        gbc.gridx++;
        
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
        current.setText(changeColor("Il volume corrente e': " + sliderValue+"%", "yellow"));
    }
    
    protected void applica() {
        Sound.soundON = sound.isSelected();
        settings.update("sound", ""+sound.isSelected());
        
        float value = slider.getValue();
        settings.update("volume", ""+(int)value);
        Sound.setVolume(Integer.parseInt(settings.getValue("volume")));
        
        settings.update("fullscreen", ""+full.isSelected());
        
        settings.update();
        
        
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
    }
    
    public static String changeColor(String toOverride, String color){
        return "<html><b><font color='"+color+"'>"+toOverride+"</font></b></html>";
    }
    
}
