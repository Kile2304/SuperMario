package mario;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import mario.rm.Animation.Memoria;
import mario.rm.Menu.home.Home;

import mario.rm.SuperMario;

/**
 *
 * @author LENOVO
 */
public class MainComponent {

    private static final Class<?> referenceClass = MainComponent.class;
    public static final URL url
            = referenceClass.getProtectionDomain().getCodeSource().getLocation();
    public static File jarPath;

    public static File jar;

    public MainComponent() {
        jar = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        try {
            jarPath = new File(MainComponent.class.getResource("./MainComponent.class").getFile());
        } catch (NullPointerException e) {

        }
        Memoria memoria = new Memoria(true);
        //System.out.println(jarPath.getAbsolutePath());
        
        Home home = new Home();
        home.setMainComponent(this);
        home.setVisible(true);

        File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        System.out.println("" + jarFile.getAbsolutePath());
    }

    public static void main(String[] args) throws IOException {
        /*try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(SuperMario.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        new MainComponent();
    }

    public void start() {
        new SuperMario();
    }

}
