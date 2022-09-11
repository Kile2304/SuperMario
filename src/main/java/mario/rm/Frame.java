package mario.rm;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import mario.rm.input.Loader;

/**
 *
 * @author LENOVO
 */
public class Frame extends JFrame {

    private int WIDTH;
    private int HEIGHT;
    private final String TITLE;

    public Frame(String title) {
        super();
        TITLE = title;
    }

    public void inizializza(int width, int height) {
        HEIGHT = height;
        WIDTH = width;

        setTitle(TITLE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);

        setIconImage(new Loader().LoadImage("Immagini/Luma-Yellow-icon.png"));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        
        setLocationRelativeTo(null);

        setVisible(true);
    }

}
