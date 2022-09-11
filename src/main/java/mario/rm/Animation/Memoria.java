package mario.rm.Animation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mario.MainComponent;
import mario.rm.input.Sound;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 *
 * @author LENOVO
 */
public class Memoria {

    private static List<Cut> enemy = new ArrayList<>();
    private static List<Cut> player = new ArrayList<>();
    private static List<Cut> tiles = new ArrayList<>();
    private static List<Cut> unlockable = new ArrayList<>();
    private static List<Cut> terreni = new ArrayList<>();

    private static String[] audio = {"nsmb_world3-C.mid", "nsmb_bowser_jr_tower.mid"};

    private static int numberOfLevel = 0;
    private static String indirizzo = "Animation/";

    private static Sound level;

    private Memoria() { }
    
    public static void init() {

        Thread[] t = new Thread[1];

        //long time = System.currentTimeMillis();
        t[0] = new Thread(() -> {
            String path = indirizzo + "tile/unlockable";    // CARICO IN MEMORIA LE IMMAGINI TILES
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

    public static void carica() {
//        clean();
        Thread[] t = new Thread[3];
        Log.append("5)CARICO LE ANIMAZIONI IN MEMORIA", DefaultFont.INFORMATION);

        //String indirizzo = "C:\\Users\\LENOVO\\Christian\\Scuola\\supermario\\SuperMario_1\\src\\Animazioni";
        /**
         *
         * ENEMY
         *
         */
        t[0] = new Thread(() -> {
            String path = indirizzo + "enemy/";    //CARICO IN MEMORIA LE IMMAGINI KOOMPA
            getAnim(path, enemy);
        });
        t[0].start();

        /*t[1] = new Thread(() -> {
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
        t[4].start();*/
        t[1] = new Thread(() -> {
            String path = indirizzo + "player/";   //CARICO IN MEMORIA LE IMMAGINI DEL PLAYER
            getAnim(path, player);
        });
        t[1].start();

        t[2] = new Thread(() -> {
            String path = indirizzo;    // CARICO IN MEMORIA LE IMMAGINI TILES
            getAnim(path + "tile/other", tiles);
            getAnim(path + "tile/unlockable", tiles);
            getAnim(path + "tile/terrain", terreni);
            getAnim(path + "tile/special", terreni);
        });
        t[2].start();
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

    public static void nextSound() {
        String indirizzo = "Sound/level/";
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

    public static List<Cut> getAnim(String path, List<Cut> list) {
    	List<String> Files = new ArrayList<>();

        LinkedList<String> Dir = new LinkedList<>();
        String pat = MainComponent.filePath + "/Luigi/" + path;
        File f = new File(pat);
        Dir.add(f.getAbsolutePath());
        System.out.println("cart: " + f.getAbsolutePath());
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
                        //System.out.println("file: "+f.getAbsolutePath());
                        //System.out.println(""+Dir.get(i));
                    }
                } catch (NullPointerException exp) {
                    Dir.remove(f.getAbsoluteFile());
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
                ob = new ObjectInputStream(new FileInputStream(string)).readObject();
                System.out.println(string);
                if (ob instanceof Cut) {
                    list.add((Cut) ob);
                } else {
                    System.out.println("Altro elemento: " + ob.getClass().getSimpleName());
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }
        return list;
    }

    public static String[] getFile(String path) {
        ArrayList<String> Files = new ArrayList<>();
        Log.append(path, DefaultFont.ERROR);

        LinkedList<String> Dir = new LinkedList<>();
        File f = new File(path);
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

    public static String[] getDirectory(String path) {
        ArrayList<String> Files = new ArrayList<>();

        String pat = path;
        LinkedList<String> Dir = new LinkedList<>();
        File f = new File(MainComponent.filePath + pat);
        System.out.println("directory: " + f.getAbsolutePath());
        Dir.add(f.getAbsolutePath());
        //Log.append(f.getAbsolutePath(), DefaultFont.ERROR);
        while (!Dir.isEmpty()) {
            //System.out.println("" + f.getAbsolutePath());
            f = new File(Dir.pop());
            //Log.append(f.getAbsolutePath(), DefaultFont.ERROR);
            if (f.isDirectory()) {
                //Log.append(f.getAbsolutePath(), DefaultFont.ERROR);
                Files.add(f.getAbsolutePath());
                String arr[] = f.list();
                try {
                    for (int i = 0; i < arr.length; i++) {
                        Dir.add(f.getAbsolutePath() + "/" + arr[i]);
                    }
                } catch (NullPointerException exp) {
                    Dir.remove(f.getAbsoluteFile());
                }
            }
        }
        String[] elenco = new String[Files.size()];

        for (int i = 0; i < Files.size(); i++) {
            elenco[i] = Files.get(i);
            System.out.println("" + elenco[i]);
        }
        return elenco;
    }

    public static List<Cut> getEnemy() {
        return enemy;
    }

    public static List<Cut> getPlayer() {
        return player;
    }

    public static List<Cut> getTiles() {
        return tiles;
    }

    public static List<Cut> getUnlockable() {
        return unlockable;
    }

    public static List<Cut> getTerreni() {
        return terreni;
    }

    public static void clean() {
        enemy.clear();
        player.clear();
        tiles.clear();
        terreni.clear();
        unlockable.clear();
    }

    /**
     * Export a resource embedded into a Jar file to the local file path.
     *
     * @param resourceName ie.: "/SmartLibrary.dll"
     * @return The path to the exported resource
     * @throws Exception
     */
    public static String ExportResource(String resourceName, String newName) throws Exception {
        String jarFolder;
        try (InputStream stream = MainComponent.class.getResourceAsStream(resourceName)){
            if (stream == null)
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");

            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = MainComponent.filePath;
            File f = new File(jarFolder + newName);
            if (!f.isFile())
                f.createNewFile();

            try (OutputStream resStreamOut = new FileOutputStream(jarFolder + newName)) {
	            while ((readBytes = stream.read(buffer)) > 0)
	                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        }

        return jarFolder + newName;
    }

}
