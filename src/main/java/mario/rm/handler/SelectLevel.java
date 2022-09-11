package mario.rm.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import mario.MainComponent;
import mario.rm.Animation.Memoria;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class SelectLevel {

    private static int index;

    private static final String[] history = {"tramatt2.level", "try.level", "tramatt.level"};    //livelli da cambiare con .level

    private static final String path = "Level\\";

    private static boolean custom = false;

    private static String[] next;

    public SelectLevel(boolean carica) {
        if (carica) {
            setList();
            custom = true;
        } else if (next == null) {
            next = new String[]{ "" };
        }
        index = 0;
    }

    public SelectLevel(String path) {
        custom = true;
        next = new String[]{path};
        index = -1;
        /*if(MainComponent.jar.isFile()){
            path = path.replaceFirst("src/", "");
        }*/
    }

    public String getNext() {
        String s = "";

        if (custom && index < next.length - 1) {
            s = next[++index];
        } else if (!custom && index < history.length - 1) {
            s = history[++index];
        } else {
            s = "";
            index--;
        }
        return s;
    }

    public String getBefore() {
        String s = "";
        
//        if (custom && index == 0) 
//        	index = next.length - 1;
//        else
//        	index--;
        if (index > 0)
        	index--;
        		
    	s = next[index];
    	
        return s;
    }

    public String getCurrent() {
        String s = "";
        
        if (custom) {
            s = next[index];
        } else {
            s = history[index];
        }
        
        return s;
    }

    public static void reset() {
        custom = false;
        next = null;
        index = -1;
    }

    private void setList() {
        String[] temp = Memoria.getFile(MainComponent.filePath + "/Luigi/Level");
        ArrayList<String> file = new ArrayList<>();
        for (String string : temp) {
            System.out.println("livello trovato; "+string);
            try {
                string.substring(0, string.lastIndexOf(".level"));
                file.add(string);
            } catch (StringIndexOutOfBoundsException e) {

            }
        }
        next = new String[file.size()];
        for (int i = 0; i < next.length; i++) {
            next[i] = file.get(i);
            Log.append(next[i], DefaultFont.INFORMATION);
        }
    }

    public String[] getList() {
        return next;
    }

    public int getIndex() {
        return index;
    }

    public static void initLevel() {
        File f = new File(MainComponent.filePath + "/Luigi/Level");
        if (!f.isDirectory()) {
            f.mkdir();
        }
        if (f.list().length <= 0) {
            for (String level : history) {
                try {
                    Memoria.ExportResource("/"+path + level, "/Luigi/Level/" + level);
                } catch (Exception ex) {
                    Logger.getLogger(SelectLevel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
