package mario.rm.Menu.sprite_estractor.input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import mario.rm.Menu.Componenti.Checkable;
import static mario.rm.Menu.Componenti.Checkable.elenco;
import mario.rm.Menu.Componenti.bottoni.Specifiche;

/**
 *
 * @author LENOVO
 */
public class Strumenti extends JPanel implements ActionListener, Checkable {

    private Selezione s;

    public Strumenti(Selezione s, int height) {
        this.s = s;

        BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                img.setRGB(j, i, 65280);
            }
        }

        Specifiche righello = new Specifiche("Righello", img, "VOID", true);
        elenco.add(righello);
        righello.getButton().addActionListener(this);
        add(righello.getButton());
        
        Specifiche altro = new Specifiche("altro");
        elenco.add(altro);
        altro.getButton().addActionListener(this);
        add(altro.getButton());

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String check = "";

        switch (e.getActionCommand()) {
            case "Righello":
                check = "Righello";
                break;
            case "altro":
                check = "altro";
                Collegamenti.resetDone();
                break;
        }
        if (!check.equals("")) {
            for (Iterator<Specifiche> it = elenco.iterator(); it.hasNext();) {
                Specifiche specifiche = it.next();
                if (specifiche.getButton().getActionCommand().equals(check)) {
                    //specifiche.getButton().setEnabled(false);
                    specifiche.setCheck();
                }
            }
        }

        try {
            s.action(e.getActionCommand());
        } catch (IOException ex) {
            Logger.getLogger(Strumenti.class.getName()).log(Level.SEVERE, null, ex);
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

}
