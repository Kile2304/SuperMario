package mario;

import Connessione.Profilo;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import mario.rm.Animation.Memoria;
import mario.rm.Menu.home.Home;
import mario.rm.Menu.sprite_estractor.input.SpriteEstractor;

import mario.rm.SuperMario;
import mario.rm.handler.SelectLevel;
import mario.rm.identifier.Tipologia;
import mario.rm.input.Sound;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Ini;
import mario.rm.utility.Log;
import mario.rm.utility.ThreadList;
import mario.rm.utility.Utility;
import mario.rm.utility.joystick.Joystick;

/**
 *
 * @author LENOVO
 */
public class MainComponent {

    /**
     * Oggetto per la creazione di una nuova partita
     */
    private SuperMario su;

    /**
     * Oggetto per la scrittura di dati di debug
     */
    public static Log log;

    /**
     * Oggetto per la lettura del file %user_path%\\conf.ini per il settaggio
     * delle impotazioni
     */
    public static Ini settings;

    public static boolean isRunningFromJar = false;

    public static String filePath;

    public static boolean loading = false;
    
    public static final boolean JOYSTICK_UPLOAD = false;

    private static Home home;

    public MainComponent() {

        System.gc();
        
        su = null;

        /*if (log == null) {
            log = new Log(this);
        }*/

        //System.out.println(jarPath.getAbsolutePath());
        home = new Home();
        home.setMainComponent(this);
        /*if (VLC) {
            home.play("Untitled.wmv");
        }*/
        //home.intro();

        //File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        Log.append(""+isRunningFromJar, DefaultFont.INFORMATION);
        
        if(!Utility.isThread(ThreadList.JOYSTICK.threadName))   new Joystick(JOYSTICK_UPLOAD);
        
        System.gc();
        System.out.println("Begin: " + memoryUsed());

    }

    public static void main(String[] args) throws IOException {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("" + memoryUsed());
                System.out.println("Current path: " + MainComponent.class.getProtectionDomain().getCodeSource().getLocation());
                init();
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(SuperMario.class.getName()).log(Level.SEVERE, null, ex);
                }
                //System.setProperty("sun.java2d.opengl","True");
                new MainComponent();

            }
        });
    }

    /**
     * Controlla se i file indispensabili sono presenti. Carica in memoria il
     * file settings. Imposta l'audio on/off. Inizializza il file con le
     * tipologie di sprite.
     */
    private static void init() {
        filePath = MainComponent.class.getProtectionDomain().getCodeSource().getLocation().toString();
        filePath = filePath.substring(6, filePath.charAt(filePath.length() - 1) == '/' ? filePath.length() : filePath.lastIndexOf("/"));
        System.out.println("" + filePath);
        isRunningFromJar = MainComponent.class.getResource("MainComponent.class").toString().startsWith("jar:");
        loading = true;
        new Thread() {
            @Override
            public void run() {
                initFile();
                //settings = new Ini(System.getProperty("user.home") + "/Luigi/settings.ini");
                settings = new Ini(filePath + "/Luigi/settings.ini");
                Sound.setSound();
                Tipologia.initTipologia();
                SpriteEstractor.initAnimation();
                SelectLevel.initLevel();
                loading = false;
                if (home != null) {
                    home.dispose();
                    home = new Home();
                }

            }
        }.start();

        loadVLC();

    }

    /**
     * Setta le librerie per il funzionamento di vlcj.
     */
    private static void loadVLC() {
        //VLC = new NativeDiscovery().discover();
        /*try {
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
        }*/
        /*if (VLC) {
            System.out.println("Trovata la versione: " + LibVlc.INSTANCE.libvlc_get_version() + " di VLC");
        } else {
            System.out.println("VLC non trovato.");
        }*/
    }

    /**
     * Controlla se i in %user_path%\\Luigi , sono presenti i file: conf.ini e ,
     * indispensabili per l'esecuzione del gioco
     */
    private static void initFile() {
        File f = new File(filePath + "/Luigi");
        if (!f.isDirectory()) {
            System.out.println(f.getAbsolutePath());
            f.mkdir();
        }
        File resource = new File(f.getAbsolutePath() + "/resources");
        if (!resource.isDirectory()) {
            resource.mkdir();
            
            File resFolder = new File(resource.getAbsolutePath()+"\\player");
            if(!resFolder.isDirectory()) resFolder.mkdir();
            
            resFolder = new File(resource.getAbsolutePath()+"\\enemy");
            if(!resFolder.isDirectory()) resFolder.mkdir();
            
            resFolder = new File(resource.getAbsolutePath()+"\\tile");
            if(!resFolder.isDirectory()){
                resFolder.mkdir();
                
            }
            File resFolderTile = new File(resFolder.getAbsolutePath() + "\\other"); if(!resFolderTile.isDirectory())    resFolderTile.mkdir();
            resFolderTile = new File(resFolder.getAbsolutePath() + "\\special");    if(!resFolderTile.isDirectory())    resFolderTile.mkdir();
            resFolderTile = new File(resFolder.getAbsolutePath() + "\\terrain");    if(!resFolderTile.isDirectory())    resFolderTile.mkdir();
            resFolderTile = new File(resFolder.getAbsolutePath() + "\\unlockable"); if(!resFolderTile.isDirectory())    resFolderTile.mkdir();
        }
        File conf = new File(f.getAbsolutePath() + "/settings.ini");
        Ini temp = new Ini(conf.getAbsolutePath());
        if (temp.isEmpty()) {
            temp.modify("sound", "false");
            temp.modify("volume", "50");
            temp.modify("scale", "2");
            temp.modify("fullscreen", "false");
            temp.update();
        }
        File user = new File(f.getAbsolutePath() + "/user.ini");
        temp = new Ini(user.getAbsolutePath());
        if (temp.isEmpty()) {
            temp.modify("remember", "false");
            temp.modify("email", "");
            temp.modify("username", "");
            temp.modify("password", "");
            temp.update();
        } else {
            Ini i = new Ini(user.getAbsolutePath());
            if (Boolean.getBoolean(i.getValue("remember"))) {
                Profilo.email = i.getValue("email");
                Profilo.looged = true;
                Profilo.password = i.getValue("password");
                Profilo.username = i.getValue("username");
            }
        }
    }

    /**
     * Inizia una nuova partita.
     *
     * @param player : Numero di player che devono giocare.
     */
    public void start(int player) {
        home = null;
        su = new SuperMario(player);
    }

    public SuperMario getSuperMario() {
        return su;
    }

    /**
     *
     * @return Memoria in Mb utilizzata attualmente.
     */
    public static long memoryUsed() {
        return (((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024) / 1024);
    }

}
