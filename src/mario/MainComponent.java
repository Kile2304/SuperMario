package mario;

import java.io.File;
import java.net.URL;
import mario.rm.Animation.Memoria;
import mario.rm.Menu.home.Home;

import mario.rm.SuperMario;
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;
import mario.rm.utility.joystick.Joystick;

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

    private SuperMario su;

    public static Log log;

    public MainComponent() {
        
        su = null;
        
        if (log == null) {
            log = new Log(this);
        }

        jar = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        try {
            jarPath = new File(MainComponent.class.getResource("./MainComponent.class").getFile());
        } catch (NullPointerException e) {

        }
        
        Memoria.setJarFile(jar);
        
        //System.out.println(jarPath.getAbsolutePath());

        Home home = new Home();
        home.setMainComponent(this);

        //File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        Log.append(jar.getAbsolutePath(), DefaultFont.INFORMATION);

        Joystick.UPDATE = false;

        new Thread(new Joystick()).start();

    }

    public static void main(String[] args)  {
        /*try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(SuperMario.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        new MainComponent();
        
    }

    public void start(int player) {
        su = new SuperMario(player);
    }

    public SuperMario getSuperMario() {
        return su;
    }

}
