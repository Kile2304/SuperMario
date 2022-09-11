package mario.rm.Menu.Componenti;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import mario.rm.Menu.Componenti.Selezione;

/**
 *
 * @author LENOVO
 */
public class ScrollButton extends JPanel {

   private JButton incrementa;
    private JButton decrementa;

    public static final int ORIZZONTALE = 0;
    public static final int VERTICALE = 1;

    private Selezione listener;

    public ScrollButton(int allineamento, int WIDTH, int HEIGHT, Selezione listener, int spazio) {
        super();

        this.listener = listener;

        //setLayout(null);

        if (allineamento == ORIZZONTALE) {
            setLayout(new GridLayout(1, 2, WIDTH - spazio, 0));
            //setBounds(0, HEIGHT - 89, WIDTH - 359, 60);
            decrementa = new JButton("←");
            incrementa = new JButton("→");

            /*incrementa.setBounds(getX() + getWidth() - 105, 5, 50, 50);
            decrementa.setBounds(0, 5, 50, 50);*/

        } else if (allineamento == VERTICALE) {
            setBounds(1501, 30, 60, HEIGHT - 80);

            /*decrementa = new JButton("↑");
            incrementa = new JButton("↓");

            decrementa.setBounds(5, 30, 50, 50);
            incrementa.setBounds(5, getY() + getHeight() - 90, 50, 50);*/
            setLayout(new GridLayout(2, 1, 0, HEIGHT - 200));
            incrementa = new JButton("↓");
            decrementa = new JButton("↑");
        }
        incrementa.addActionListener(listener);
        decrementa.addActionListener(listener);

        decrementa.setEnabled(false);

        add(decrementa);
        add(incrementa);
    }

    public void changeState(int stato) {
        switch (stato) {
            case 1:
                incrementa.setEnabled(true);
                break;
            case -1:
                incrementa.setEnabled(false);
                break;
            case 2:
                decrementa.setEnabled(true);
                break;
            case -2:
                decrementa.setEnabled(false);
        }
    }


}
