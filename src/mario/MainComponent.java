package mario;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import mario.rm.Animation.Memoria;
import mario.rm.Menu.home.Home;

import mario.rm.SuperMario;
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;
import mario.rm.utility.joystick.Joystick;
import net.java.games.input.Controller;

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

    ArrayList<Controller> contr = new ArrayList<>();

    public MainComponent() {
        if (log == null) {
            log = new Log(this);
        }

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

        Log.append(jarFile.getAbsolutePath(), DefaultFont.INFORMATION);

        Joystick.UPDATE = true;
        
        //Joystick.ControllerLoader();
        
        new Thread(new Joystick()).start();
        
        /*Controller[] joystick = Joystick.getController();
        PlaystationController[] play = new PlaystationController[joystick.length];

        for (int i = 0; i < play.length; i++) {
            play[i] = new PlaystationController(joystick[i]);
        }

        while (true) {
            for (PlaystationController controller : play) {
                String[] button = controller.listener();
                if (button != null) {
                    Log.append("" + button[0], DefaultFont.DEBUG);
                }
            }
            try {
                sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainComponent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        //ControllerEnvironment cc = ControllerEnvironment.getDefaultEnvironment();
        //cc.addControllerListener(this);
        

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
        su = new SuperMario();
    }

    public SuperMario getSuperMario() {
        return su;
    }

    /*@Override
    public void controllerRemoved(ControllerEvent ce) {
        Log.append("remove: " + ce.getController().getName(), DefaultFont.DEBUG);
        contr.remove(ce.getController());
    }

    @Override
    public void controllerAdded(ControllerEvent ce) {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        int indexOf = ce.getController().getType() == Controller.Type.GAMEPAD ? contr.indexOf(ce.getController()) : -1;
        if (indexOf == -1) {
            Log.append("collegato: " + ce.getController().getName(), DefaultFont.DEBUG);
            contr.add(ce.getController());
        }
    }*/

}
