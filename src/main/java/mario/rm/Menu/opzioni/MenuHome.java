package mario.rm.Menu.opzioni;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import mario.rm.Menu.home.Home;

/**
 *
 * @author LENOVO
 */
public class MenuHome extends Impostazioni {

    private final Home mario;

    public MenuHome(Home mario) {
        super();

        setLayout(new GridLayout());

        option(true);

        add(center);
        repaint();

        this.mario = mario;
    }
    
    protected void normal() {
        mario.reset();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
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
