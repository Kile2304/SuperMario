package mario.rm.Menu;

import java.awt.Color;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import mario.rm.Menu.Componenti.bottoni.TranslucentButton;
import mario.rm.identifier.Type;
import mario.rm.utility.RGB;

/**
 *
 * @author LENOVO
 */
public class Specifiche extends Cell {

    private boolean check;

    private JButton button;

    public Specifiche(Type id, BufferedImage img, String title) {
        super(id, img, title);

        button = new JButton();

        //button.setMargin(new Insets(0, 0, 0, 0));
        button.setBorder(null);
        //button.setFocusPainted(true);
        //button.setContentAreaFilled(false);
        
        button.setIcon(new ImageIcon(img));
    }

    public Specifiche(String s) {
        button = initColor();
        button.setText(s);
    }

    public Specifiche(String s, BufferedImage img, Type id) {
        button = initColor();
        button.setText(s);
        this.img = img;
        this.type = id;
    }

    private TranslucentButton initColor() {
        TranslucentButton button = new TranslucentButton();
        button.setBgCol(Color.gray);
        button.setBgColro(Color.LIGHT_GRAY);
        button.setFgCol(Color.BLACK);
        button.setFgColsel(Color.BLACK);

        return button;
    }

    public Specifiche() {
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    /**
     * return IMPOSTA IL BOTTONA A PREMUTO, QUINDI SE UNA SEZIONE DELLA GRIGLIA
     * VERRA' PREMUTA, ASSUMERA' IL VALORE DI QUESTO BOTTONE
     */
    public void setCheck() {
        check = true;
    }

    /**
     * return IMPOSTA IL BOTTONA A NON PREMUTO
     */
    public void unCheck() {
        check = false;
    }

    public boolean isCheck() {
        return check;
    }

}
