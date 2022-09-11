package mario.rm.utility;

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

/**
 *
 * @author LENOVO
 */
public class Ini {

    private Map<String, String> list = new HashMap();
    private String path;

    public Ini(String path) {
        this.path = path;
        try {
            File f = new File(path);
            if (!f.isFile()) {
                f.createNewFile();
            } else {
                init(f);
            }
        } catch (IOException ex) {
            System.out.println(""+path);
            Logger.getLogger(Ini.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init(File f) throws FileNotFoundException, IOException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(f));
        BufferedReader br = new BufferedReader(isr);

        String temp = "";
        while ((temp = br.readLine()) != null) {
            String[] ini = temp.split("=");
            list.put(ini[0], ini.length > 1 ? ini[1] : "");
        }

    }

    public void modify(String key, String newValue) {
        list.put(key, newValue);
    }

    public void update() {
        FileWriter fw = null;
        try {
            fw = new FileWriter(new File(path));
            BufferedWriter bw = new BufferedWriter(fw);
            for (String key : list.keySet()) {
                bw.append(key + "=" + list.get(key) + "\n");
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Ini.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Ini.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getValue(String index) {
        index = index.toLowerCase();
        return list.get(index);
    }
    
    public boolean isEmpty(){
        return list.isEmpty();
    }

}
