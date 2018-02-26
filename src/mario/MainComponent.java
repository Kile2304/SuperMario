package mario;

import com.sun.jna.NativeLibrary;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import mario.rm.Animation.Memoria;
import mario.rm.Menu.home.Home;

import mario.rm.SuperMario;
import mario.rm.identifier.Tipologia;
import mario.rm.input.Sound;
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Ini;
import mario.rm.utility.Log;
import mario.rm.utility.joystick.Joystick;
import uk.co.caprica.vlcj.binding.LibC;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.version.LibVlcVersion;

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

    public static Ini settings;

    public static boolean VLC;

    public MainComponent() {

        su = null;

        /*if (log == null) {
            log = new Log(this);
        }*/
        jar = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        try {
            jarPath = new File(MainComponent.class.getResource("./MainComponent.class").getFile());
        } catch (NullPointerException e) {

        }

        Memoria.setJarFile(jar);

        //System.out.println(jarPath.getAbsolutePath());
        Home home = new Home();
        home.setMainComponent(this);
        if (VLC) {
            home.play("Untitled.wmv");
        }
        //home.intro();

        //File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        Log.append(jar.getAbsolutePath(), DefaultFont.INFORMATION);

        Joystick.UPDATE = false;

        new Thread(new Joystick()).start();

    }

    public static void main(String[] args) {
        System.out.println(""+memoryUsed());
        init();
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(SuperMario.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.setProperty("sun.java2d.opengl","True");
        new MainComponent();
        System.gc();
        System.out.println("Begin: "+memoryUsed());
    }

    private static void init() {
        new Thread() {
            @Override
            public void run() {
                checkFile();
                settings = new Ini(System.getProperty("user.home") + "/Luigi/settings.ini");
                Sound.setSound();
                Tipologia.init();
            }
        }.start();

        loadVLC();

    }

    private static void loadVLC() {
        //VLC = new NativeDiscovery().discover();
        try {
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
            if (LibVlcVersion.getVersion().atLeast(LibVlcVersion.LIBVLC_220)) {
                if (System.getenv("VLC_PLUGIN_PATH") == null) {
                    String pluginPath = String.format("%s\\%s", "C:\\Program Files\\VideoLAN\\VLC", "plugins");
                    LibC.INSTANCE._putenv(String.format("%s=%s", "VLC_PLUGIN_PATH", pluginPath));
                }
            }
            VLC = true;
        } catch (UnsatisfiedLinkError e) {
            System.err.println(Log.stackTraceToString(e));
            VLC = false;
        }
        VLC = false;
        if (VLC) {
            System.out.println("Trovata la versione: " + LibVlc.INSTANCE.libvlc_get_version() + " di VLC");
        } else {
            System.out.println("VLC non trovato.");
        }
    }

    private static void checkFile() {
        String path = System.getProperty("user.home");
        File f = new File(path + "/Luigi");
        if (!f.isDirectory()) {
            System.out.println(f.getAbsolutePath());
            f.mkdir();
        }
        File conf = new File(f.getAbsolutePath() + "/settings.ini");
        if (!conf.isFile()) {
            try {
                conf.createNewFile();
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(conf))) {
                    bw.append("sound=false\nvolume=50\nscale=2\nfullscreen=false\n");
                }
            } catch (IOException ex) {
                Logger.getLogger(MainComponent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void start(int player) {
        su = new SuperMario(player);
    }

    public SuperMario getSuperMario() {
        return su;
    }

    public static long memoryUsed() {
        return (((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024) / 1024);
    }

}
