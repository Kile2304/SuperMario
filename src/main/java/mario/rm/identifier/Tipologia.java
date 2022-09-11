package mario.rm.identifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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

    public static void initTipologia() {
        index();
        String path = MainComponent.filePath + "/Luigi/type.dat";
        File f = new File(path);
        if (!f.isFile()) {
            create(path);
        }
        read(f);
    }

    private static void read(File f) {
        try (BufferedReader br = Files.newBufferedReader(f.toPath())) {

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
        }
    }

    public static void createNewType(String name) {
        Map.Entry<String, int[]> entry = list.entrySet().iterator().next();
        String key = entry.getKey();
        
        int[] temp = new int[list.get(key).length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = 0;
        }
        list.put(name, temp);
        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(MainComponent.filePath + "/Luigi/type.dat"), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
        	StringBuilder newLine = new StringBuilder(name + " ");
        	for (int i = 0; i < temp.length; i++)
        		newLine.append(temp[i] + ((i < temp.length - 1) ? " " : ""));
        	
        	bw.append(newLine.toString());
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
        try (BufferedReader br = new BufferedReader(
        			new InputStreamReader(MainComponent.class.getResourceAsStream("/type.txt")));
        		BufferedWriter bw = Files.newBufferedWriter(Path.of(path), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
	        String line = "";
	        while ((line = br.readLine()) != null)
	            bw.append(line + System.lineSeparator());
        } catch (IOException ex) {
            Logger.getLogger(Tipologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int getValue(String type, String var) {
        return list.get(type)[index.get(var)];
    }

    public static boolean getValue(String type) {
        return list.get(type) != null;
    }

}
