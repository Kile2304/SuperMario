/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario.rm.input;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import mario.MainComponent;
import mario.rm.Animation.Anim;
import mario.rm.Animation.Tile;
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class MemoriaAC {

    private static ArrayList<Anim> enemy;
    private static ArrayList<Anim> player;
    private static ArrayList<Tile> tiles;
    private static ArrayList<Tile> unlockable;
    private static ArrayList<Tile> terreni;
    private static ArrayList<Anim> bullet;
    private static ArrayList<Tile> special;

    private String[] audio = {"nsmb_world3-C.mid", "nsmb_bowser_jr_tower.mid"};

    private int numberOfLevel = 0;
    private String indirizzo;

    private Sound level;

    private static File jarFile = null;

    public MemoriaAC(boolean temp) {
        if (jarFile == null) {
            jarFile = MainComponent.jar;
        }
    }

    public MemoriaAC() {

        if (jarFile == null) {
            jarFile = MainComponent.jar;
        }

        indirizzo = "Animazioni/";

        enemy = new ArrayList<>();  //ELENCO IMMAGINI NEMICI
        player = new ArrayList<>(); //ELENCO IMMAGINI PLAYER
        tiles = new ArrayList<>();  //ELENCO IMMAGINI TILESF
        unlockable = new ArrayList<>();     //IMMAGINI CHE SI POSSONO SBLOCCARE
        terreni = new ArrayList<>();
        bullet = new ArrayList<>();
        special = new ArrayList<>();

    }

    public void carica() {
        Thread[] t = new Thread[6];
        Log.append("5)CARICO LE ANIMAZIONI IN MEMORIA", DefaultFont.INFORMATION);

        /**
         *
         * PLAYER
         *
         */
        t[0] = new Thread(() -> {
            String path = indirizzo + "player";    //CARICO IN MEMORIA LE IMMAGINI KOOMPA
            getAnim(path, player);
        });
        t[0].start();
        t[1] = new Thread(() -> {
            String path = indirizzo + "enemy";    //CARICO IN MEMORIA LE IMMAGINI KOOMPA
            getAnim(path, enemy);
        });
        t[1].start();
        t[2] = new Thread(() -> {
            String path = indirizzo + "tile/terrain";    //CARICO IN MEMORIA LE IMMAGINI KOOMPA
            getAnim(path, terreni);
        });
        t[2].start();
        t[3] = new Thread(() -> {
            String path = indirizzo + "tile/other";    //CARICO IN MEMORIA LE IMMAGINI KOOMPA
            getAnim(path, tiles);
        });
        t[3].start();
        t[4] = new Thread(() -> {
            String path = indirizzo + "tile/unlockable";    //CARICO IN MEMORIA LE IMMAGINI KOOMPA
            getAnim(path, unlockable);
        });
        t[4].start();
        t[5] = new Thread(() -> {
            String path = indirizzo + "enemy/missile";    //CARICO IN MEMORIA LE IMMAGINI KOOMPA
            getAnim(path, bullet);
        });
        t[5].start();
        t[5] = new Thread(() -> {
            String path = indirizzo + "tile/special";    //CARICO IN MEMORIA LE IMMAGINI KOOMPA
            getAnim(path, special);
        });
        t[5].start();

        for (int i = 0; i < t.length; i++) {
            try {
                t[i].join();
            } catch (InterruptedException ex) {
                Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
            }
        }
    }

    public void nextSound() {
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

    public ArrayList<Object> getAnim(String path, ArrayList list) {
        ArrayList<String> Files = new ArrayList<>();
        
        if (jarFile.isFile()) {
            CodeSource src = MainComponent.class.getProtectionDomain().getCodeSource();
            ZipInputStream zip = null;
            try {
                URL jar = src.getLocation();
                zip = new ZipInputStream(jar.openStream());
                while (true) {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null) {
                        break;
                    }
                    String name = e.getName();
                    if (name.startsWith(path + "/")) {
                        /* Do something with this entry. */
                        //System.out.println("" + name);
                        //Log.append(name, DefaultFont.DEBUG);
                        if (name.charAt(name.length() - 1) != '/') {
                            //Log.append(name, DefaultFont.DEBUG);
                            Files.add(name);
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(MemoriaAC.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    zip.close();
                } catch (IOException ex) {
                    Logger.getLogger(MemoriaAC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            LinkedList<String> Dir = new LinkedList<>();
            String pat = "res/" + path;
            File f = new File(pat);

            Dir.add(f.getAbsolutePath());
            //System.out.println(""+f.getAbsolutePath());
            while (!Dir.isEmpty()) {
                //System.out.println("" +f.getAbsolutePath());
                f = new File(Dir.pop());
                    //Log.append(""+f.getAbsolutePath(), DefaultFont.DEBUG);
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
                    InputStream in = new BufferedInputStream(MainComponent.class.getClassLoader().get‌​ResourceAsStream(string));
                    ob = new ObjectInputStream(in).readObject();

                    in.close();
                } else {
                    FileInputStream fos = new FileInputStream(string);
                    ObjectInputStream obi = new ObjectInputStream(fos);
                    ob = obi.readObject();
                    fos.close();
                    obi.close();
                }
                //System.out.println(""+string);
                if (ob instanceof Anim) {
                    list.add((Anim) ob);
                } else if(ob instanceof Tile){
                    list.add((Tile) ob);
                }else{
                    System.out.println("Altro elemento");
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }
        return list;
    }

    public ArrayList<Anim> getEnemy() {
        return enemy;
    }

    public ArrayList<Anim> getPlayer() {
        return player;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public ArrayList<Tile> getUnlockable() {
        return unlockable;
    }

    public ArrayList<Tile> getTerreni() {
        return terreni;
    }
    public ArrayList<Anim> getBullet() {
        return bullet;
    }
    public ArrayList<Tile> getSpecial() {
        return special;
    }

    public void clean() {
        enemy.clear();
        player.clear();
        tiles.clear();
        terreni.clear();
        unlockable.clear();
        bullet.clear();
        special.clear();
    }

    public void adaptImage(int width, int height) {
        for (Anim anim : enemy) {
            anim.adapt(width, height);
        }
        for (Anim anim : player) {
            anim.adapt(width, height);
        }
        for (Anim anim : bullet) {
            anim.adapt(width, height);
        }
        for (Tile anim : tiles) {
            anim.adapt(width, height);
        }
        for (Tile anim : terreni) {
            anim.adapt(width, height);
        }
        for (Tile anim : unlockable) {
            anim.adapt(width, height);
        }
        for (Tile anim : special) {
            anim.adapt(width, height);
        }
    }
}
