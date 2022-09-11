/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario.rm.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mario.MainComponent;
import mario.rm.Animation.Anim;
import mario.rm.Animation.Tile;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class MemoriaAC {

    private static ArrayList<Anim> enemy = new ArrayList<>();
    private static ArrayList<Anim> player = new ArrayList<>();
    private static ArrayList<Tile> tiles = new ArrayList<>();
    private static ArrayList<Tile> unlockable = new ArrayList<>();
    private static ArrayList<Tile> terreni = new ArrayList<>();
    private static ArrayList<Anim> bullet = new ArrayList<>();
    private static ArrayList<Tile> special = new ArrayList<>();

    private static final String[] audio = {"nsmb_world3-C.mid", "nsmb_bowser_jr_tower.mid"};

    private static int numberOfLevel = 0;
    private static String indirizzo;

    private static Sound level;

    private MemoriaAC() { }

    public static void carica() {
    	if (player == null || player.size() == 0) {
	        Thread[] t = new Thread[6];
	        Log.append("5)CARICO LE ANIMAZIONI IN MEMORIA", DefaultFont.INFORMATION);
	
	        /**
	         *
	         * PLAYER
	         *
	         */
	        t[0] = new Thread(() -> {
	            String path = indirizzo + "player";    //CARICO IN MEMORIA LE IMMAGINI
	            getAnim(path, player);
	        });
	        t[0].start();
	        t[1] = new Thread(() -> {
	            String path = indirizzo + "enemy";    //CARICO IN MEMORIA LE IMMAGINI
	            getAnim(path, enemy);
	        });
	        t[1].start();
	        t[2] = new Thread(() -> {
	            String path = indirizzo + "tile/terrain";    //CARICO IN MEMORIA LE IMMAGINI
	            getAnim(path, terreni);
	        });
	        t[2].start();
	        t[3] = new Thread(() -> {
	            String path = indirizzo + "tile/other";    //CARICO IN MEMORIA LE IMMAGINI
	            getAnim(path, tiles);
	        });
	        t[3].start();
	        t[4] = new Thread(() -> {
	            String path = indirizzo + "tile/unlockable";    //CARICO IN MEMORIA LE IMMAGINI
	            getAnim(path, unlockable);
	        });
	        t[4].start();
	        t[5] = new Thread(() -> {
	            String path = indirizzo + "enemy/missile";    //CARICO IN MEMORIA LE IMMAGINI
	            getAnim(path, bullet);
	        });
	        t[5].start();
	        t[5] = new Thread(() -> {
	            String path = indirizzo + "tile/special";    //CARICO IN MEMORIA LE IMMAGINI
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

    public static Sound getSound() {
        return level;
    }

    public static ArrayList<Object> getAnim(String path, ArrayList list) {
        List<String> files = new ArrayList<>();
        LinkedList<String> dir = new LinkedList<>();
        String pat = MainComponent.filePath + "/Luigi/" + path;
        File f = new File(pat);
        System.out.println("" + f.getAbsolutePath());

        dir.add(f.getAbsolutePath());
        //System.out.println(""+f.getAbsolutePath());
        while (!dir.isEmpty()) {
            //System.out.println("" +f.getAbsolutePath());
            f = new File(dir.pop());
            //Log.append(""+f.getAbsolutePath(), DefaultFont.DEBUG);
            //System.out.println(""+f.getAbsolutePath());
            if (f.isFile())
                files.add(f.getAbsolutePath());
            else {
                String[] arr = f.list();
                try {
                    for (int i = 0; i < arr.length; i++)
                        dir.add(f.getAbsolutePath() + "/" + arr[i]);
                } catch (NullPointerException exp) {
                    dir.remove(f.getAbsolutePath());
                }
            }
        }

        //Print the files
        /*for (int i = 0; i < Files.size(); i++) {
            System.out.println(Files.get(i));
        }*/
        try {
            for (String string : files) {
                Object ob = null;
                FileInputStream fos = new FileInputStream(string);
                ObjectInputStream obi = new ObjectInputStream(fos);
                ob = obi.readObject();
                fos.close();
                obi.close();
                //System.out.println(""+string);
                if (ob instanceof Anim)
                    list.add((Anim) ob);
                else if (ob instanceof Tile)
                    list.add((Tile) ob);
                else
                    System.out.println("Altro elemento");
            }
        } catch (IOException | ClassNotFoundException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }
        return list;
    }

    public static ArrayList<Anim> getEnemy() {
        return enemy;
    }

    public static ArrayList<Anim> getPlayer() {
        return player;
    }

    public static ArrayList<Tile> getTiles() {
        return tiles;
    }

    public static ArrayList<Tile> getUnlockable() {
        return unlockable;
    }

    public static ArrayList<Tile> getTerreni() {
        return terreni;
    }

    public static ArrayList<Anim> getBullet() {
        return bullet;
    }

    public static ArrayList<Tile> getSpecial() {
        return special;
    }

    public static void clean() {
        enemy.clear();
        player.clear();
        tiles.clear();
        terreni.clear();
        unlockable.clear();
        bullet.clear();
        special.clear();
    }
    
    public static void delete(){
        enemy = null;
        player = null;
        tiles = null;
        terreni = null;
        unlockable = null;
        bullet = null;
        special = null;
//        audio = null;
        level = null;
    }

    public static void adaptImage(int width, int height) {
        enemy.stream().forEach((anim) -> {
            anim.adapt(width, height);
        });
        player.stream().forEach((anim) -> {
            anim.adapt(width, height);
        });
        bullet.stream().forEach((anim) -> {
            anim.adapt(width, height);
        });
        tiles.stream().forEach((anim) -> {
            anim.adapt(width, height);
        });
        terreni.stream().forEach((anim) -> {
            anim.adapt(width, height);
        });
        unlockable.stream().forEach((anim) -> {
            anim.adapt(width, height);
        });
        special.stream().forEach((anim) -> {
            anim.adapt(width, height);
        });
    }

	public static void init() {
        indirizzo = "Animation/";
	}
}
