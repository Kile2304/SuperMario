package mario.rm.identifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mario.MainComponent;

/**
 *
 * @author LENOVO
 */
public class Tipologia {

    public static Map<String, int[]> list = new HashMap<>();
    private static final Map<String, Integer> index = new HashMap<>();

    public static void init() {
        index();
        String path = System.getProperty("user.home") + "/Luigi/type.dat";
        File f = new File(path);
        if (!f.isFile()) {
            create(path);
        }
        read(f);
    }

    private static void read(File f) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String temp = "";
            while ((temp = br.readLine()) != null) {
                String[] values = temp.split(" ");
                int[] valueList = new int[values.length - 1];
                for (int i = 1; i < values.length; i++) {
                    valueList[i - 1] = Integer.parseInt(values[i]);
                }
                list.put(values[0], valueList);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tipologia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tipologia.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(Tipologia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void createNewType(String name) {
        try {
            Map.Entry<String, int[]> entry = list.entrySet().iterator().next();
            String key = entry.getKey();
            
            int[] temp = new int[list.get(key).length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = 0;
            }
            list.put(name, temp);
            FileWriter fw = new FileWriter(new File(System.getProperty("user.home") + "/Luigi/type.dat"), true);
            BufferedWriter bw = new BufferedWriter(fw);

            String newLine = name + " ";
            for (int i = 0; i < temp.length; i++) {
                newLine += temp[i] + ((i < temp.length - 1) ? " " : "");
            }

            bw.append(newLine);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Tipologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void index() {
        index.put("time", 0);
        index.put("delay", 1);
        index.put("immNumber", 2);
        index.put("velX", 3);
        index.put("velY", 4);
    }

    private static void create(String path) {
        FileWriter fw = null;
        try {
            InputStreamReader isr = new InputStreamReader(MainComponent.class.getClassLoader().getResourceAsStream("type.txt"));
            BufferedReader br = new BufferedReader(isr);

            fw = new FileWriter(path, true);
            BufferedWriter bw = new BufferedWriter(fw);

            String temp = "";
            while ((temp = br.readLine()) != null) {
                bw.append(temp + "\n");
            }
            bw.close();
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(Tipologia.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Tipologia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static int getValue(String type, String var) {
        return list.get(type)[index.get(var)];
    }

    public static boolean getValue(String type) {
        return list.get(type) != null;
    }

}
