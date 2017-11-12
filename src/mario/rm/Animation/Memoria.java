package mario.rm.Animation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import mario.MainComponent;
import mario.rm.input.Sound;
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
/**
 *
 *
 * @return CARICA IN MEMORIA TUTTE LE ANIMAZIONI
 */
public class Memoria {

    private static ArrayList<Cut> enemy;
    private static ArrayList<Cut> player;
    private static ArrayList<Cut> tiles;
    private static ArrayList<Cut> unlockable;
    private static ArrayList<Cut> terreni;

    private String[] audio = {"nsmb_world3-C.mid", "nsmb_bowser_jr_tower.mid"};

    private int numberOfLevel = 0;
    private String indirizzo;

    private Sound level;

    private static File jarFile = null;

    public Memoria(boolean temp) {
        if (jarFile == null) {
            jarFile = MainComponent.jar;
        }
    }

    public Memoria() {

        if (jarFile == null) {
            jarFile = MainComponent.jar;
        }

        Thread[] t = new Thread[1];

        indirizzo = "mario/res/Animazioni/";

        //System.out.println(""+indirizzo);
        enemy = new ArrayList<>();  //ELENCO IMMAGINI NEMICI
        player = new ArrayList<>(); //ELENCO IMMAGINI PLAYER
        tiles = new ArrayList<>();  //ELENCO IMMAGINI TILESF
        unlockable = new ArrayList<>();     //IMMAGINI CHE SI POSSONO SBLOCCARE
        terreni = new ArrayList<>();

        //long time = System.currentTimeMillis();
        t[0] = new Thread(() -> {
            String path = indirizzo + "Tile/unlockable";    // CARICO IN MEMORIA LE IMMAGINI TILES
            getAnim(path, unlockable);
        });
        t[0].start();

        for (int i = 0; i < t.length; i++) {
            try {
                t[i].join();
            } catch (InterruptedException ex) {
                Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
            }
        }

        //System.out.println("Tempo caricamento immagini: " + (System.currentTimeMillis() - time));
    }

    public void carica() {
        Thread[] t = new Thread[7];
        Log.append("5)CARICO LE ANIMAZIONI IN MEMORIA", DefaultFont.INFORMATION);

        //String indirizzo = "C:\\Users\\LENOVO\\Christian\\Scuola\\supermario\\SuperMario_1\\src\\Animazioni";
        /**
         *
         * ENEMY
         *
         */
        t[0] = new Thread(() -> {
            String path = indirizzo + "enemy/koompa";    //CARICO IN MEMORIA LE IMMAGINI KOOMPA
            getAnim(path, enemy);
        });
        t[0].start();

        t[1] = new Thread(() -> {
            String path = indirizzo + "enemy/tartosso"; //CARICO IN MEMORIA LE IMMAGNI TARTOSSO
            getAnim(path, enemy);
        });
        t[1].start();

        t[2] = new Thread(() -> {
            String path = indirizzo + "enemy/piranha_plant";    //CARICO IN MEMORIA LE IMMAGINI DELLA PIANTA PIRANHA
            getAnim(path, enemy);
        });
        t[2].start();

        t[3] = new Thread(() -> {
            String path = indirizzo + "enemy/chain_chomp";  //CARICO IN MEMORIA LE IMMAGINI DEL CATENACCIO
            getAnim(path, enemy);
        });
        t[3].start();

        t[4] = new Thread(() -> {
            String path = indirizzo + "enemy/boo";  //CARICO IN MEMORIA LE IMMAGINI DEL FANTASMINO
            getAnim(path, enemy);
        });
        t[4].start();

        t[5] = new Thread(() -> {
            String path = indirizzo + "player/luigi";   //CARICO IN MEMORIA LE IMMAGINI DEL PLAYER
            getAnim(path, player);
        });
        t[5].start();

        t[6] = new Thread(() -> {
            String path = indirizzo;    // CARICO IN MEMORIA LE IMMAGINI TILES
            getAnim(path + "Tile/other", tiles);
            getAnim(path + "Tile/unlockable", tiles);
            //getAnim(path + "Tile/terrain", terreni);
        });
        t[6].start();
        /*t[7] = new Thread(new Runnable() {
            public void run() {
                String path = indirizzo + "/Tile/terrain/ice";    // CARICO IN MEMORIA LE IMMAGINI TILES
                getAnim(path, tiles);
            }
        });
        t[7].start();
        t[8] = new Thread(new Runnable() {
            public void run() {
                String path = indirizzo + "/Tile/terrain/snow";    // CARICO IN MEMORIA LE IMMAGINI TILES
                getAnim(path, tiles);
            }
        });
        t[8].start();
        t[9] = new Thread(new Runnable() {
            public void run() {
                String path = indirizzo + "/Tile/terrain/desert";    // CARICO IN MEMORIA LE IMMAGINI TILES
                getAnim(path, tiles);
            }
        });
        t[9].start();
        t[10] = new Thread(new Runnable() {
            public void run() {
                String path = indirizzo + "/Tile/terrain/grownd";    // CARICO IN MEMORIA LE IMMAGINI TILES
                getAnim(path, tiles);
            }
        });
        t[10].start();
        t[11] = new Thread(new Runnable() {
            public void run() {
                String path = indirizzo + "/Tile/terrain/dark";    // CARICO IN MEMORIA LE IMMAGINI TILES
                getAnim(path, tiles);
            }
        });
        t[11].start();*/

        for (int i = 0; i < t.length; i++) {
            try {
                t[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Memoria.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void nextSound() {
        String indirizzo = "mario/Sound/level/";
        if (numberOfLevel < audio.length) {
            level = new Sound(indirizzo + audio[numberOfLevel]);
            numberOfLevel++;
        } else {
            numberOfLevel = 0;
            level = new Sound(indirizzo + audio[numberOfLevel]);
        }

    }

    public Sound getSound() {
        return level;
    }

    public ArrayList<Cut> getAnim(String path, ArrayList<Cut> list) {
        ArrayList<String> Files = new ArrayList<>();
        if (jarFile.isFile()) {
            try {
                // Run with JAR file
                JarFile jar = new JarFile(jarFile);
                Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    if (name.startsWith(path + "/")) { //filter according to the path
                        //System.out.println(name);
                        //System.out.println("" + name);
                        if (!(new File(name).isDirectory())) {
                            //System.out.println("" + name);
                            if (name.charAt(name.length() - 1) != '/') {
                                Files.add(name);
                            }
                        }
                    }
                }
                jar.close();
            } catch (IOException ex) {
                Logger.getLogger(Memoria.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            LinkedList<String> Dir = new LinkedList<>();
            String pat = "src/" + path;
            File f = new File(pat);
            Dir.add(f.getAbsolutePath());
            //System.out.println(""+f.getAbsolutePath());
            while (!Dir.isEmpty()) {
                //System.out.println("" +f.getAbsolutePath());
                f = new File(Dir.pop());
                //System.out.println(""+f.getAbsolutePath());
                if (f.isFile()) {
                    Files.add(f.getAbsolutePath());
                } else {
                    String arr[] = f.list();
                    try {
                        for (int i = 0; i < arr.length; i++) {
                            Dir.add(f.getAbsolutePath() + "/" + arr[i]);
                            //System.out.println(""+Dir.get(i));
                        }
                    } catch (NullPointerException exp) {
                        Dir.remove(f.getAbsoluteFile());
                    }
                }
            }
        }

        //Print the files
        /*for (int i = 0; i < Files.size(); i++) {
            System.out.println(Files.get(i));
        }*/
        try {
            for (String string : Files) {
                Object ob = null;
                if (jarFile.isFile()) {
                    //InputStream in = MainComponent.class.getClassLoader().getResourceAsStream("mario/Animazioni/enemy/boo/boo-stand.anim");
                    InputStream in = new BufferedInputStream(MainComponent.class.getClassLoader().get‌​ResourceAsStream(string));
                    ob = new ObjectInputStream(in).readObject();

                    in.close();
                } else {
                    ob = new ObjectInputStream(new FileInputStream(string)).readObject();
                }
                if (ob instanceof Cut) {
                    list.add((Cut) ob);
                } else {
                    System.out.println("Altro elemento");
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }
        return list;
    }

    public static String[] getFile(String path) {
        ArrayList<String> Files = new ArrayList<>();
        if (jarFile.isFile()) {
            try {
                // Run with JAR file
                JarFile jar = new JarFile(jarFile);
                Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    if (name.startsWith(path + "/")) { //filter according to the path
                        //System.out.println(name);
                        //System.out.println("" + name);
                        if (!(new File(name).isDirectory())) {
                            //System.out.println("" + name);
                            if (name.charAt(name.length() - 1) != '/') {
                                Files.add(name);
                            }
                        }
                    }
                }
                jar.close();
            } catch (IOException ex) {
                Logger.getLogger(Memoria.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            LinkedList<String> Dir = new LinkedList<>();
            String pat = "src/" + path;
            File f = new File(pat);
            Dir.add(f.getAbsolutePath());
            //System.out.println(""+f.getAbsolutePath());
            while (!Dir.isEmpty()) {
                //System.out.println("" +f.getAbsolutePath());
                f = new File(Dir.pop());
                //System.out.println(""+f.getAbsolutePath());
                if (f.isFile()) {
                    Files.add(f.getAbsolutePath());
                } else {
                    String arr[] = f.list();
                    try {
                        for (int i = 0; i < arr.length; i++) {
                            Dir.add(f.getAbsolutePath() + "/" + arr[i]);
                            //System.out.println(""+Dir.get(i));
                        }
                    } catch (NullPointerException exp) {
                        Dir.remove(f.getAbsoluteFile());
                    }
                }
            }
        }

        ArrayList<String> z = new ArrayList<>();
        for (int i = 0; i < Files.size(); i++) {
            String original = Files.get(i);
            //String file = original.substring(original.lastIndexOf("\\"));
            //String extension = file.substring(file.indexOf("."));
            String file = "";
            String extension = "";
            try {
                file = original.substring(original.lastIndexOf("\\"));
            } catch (StringIndexOutOfBoundsException e) {
                file = original;
            } finally {
                try {
                    extension = file.substring(file.indexOf("."));
                    if (extension.equals(".level")) {
                        z.add(Files.get(i));
                    }
                } catch (StringIndexOutOfBoundsException e) {
                }

            }

        }
        String[] elenco = new String[z.size()];

        for (int i = 0; i < z.size(); i++) {
            elenco[i] = z.get(i);
        }
        return elenco;
    }

    public ArrayList<Cut> getEnemy() {
        return enemy;
    }

    public ArrayList<Cut> getPlayer() {
        return player;
    }

    public ArrayList<Cut> getTiles() {
        return tiles;
    }

    public ArrayList<Cut> getUnlockable() {
        return unlockable;
    }

    public ArrayList<Cut> getTerreni() {
        return terreni;
    }

}
