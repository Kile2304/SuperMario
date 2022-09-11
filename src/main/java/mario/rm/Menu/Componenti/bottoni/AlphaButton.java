package mario.rm.Menu.Componenti.bottoni;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;

/**
 *
 * @author LENOVO
 */
public class AlphaButton extends JButton {

    private float opacity = 1;

    public AlphaButton(String name){
        super(name);
        addMouseListener(new MouseAdapter(){

            @Override
            public void mouseEntered(MouseEvent e) {
                setOpacity(0.7f);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setOpacity(0.5f);
            }
            
        });
    }
    
    public void setOpacity(float opacity) {
        this.opacity = opacity;
        repaint();
    }

    public float getOpacity() {
        return opacity;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity));
        super.paint(g2);
        g2.dispose();
    }

}
