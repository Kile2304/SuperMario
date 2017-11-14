package mario.rm.handler;

import java.util.ArrayList;
import mario.MainComponent;
import mario.rm.Animation.Memoria;
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class SelectLevel {

    private int index;

    private static final String[] history = {"provetta.level"};    //livelli da cambiare con .level

    private static final String path = "src/mario/res/Immagini/livelli/";

    private static boolean custom = false;

    private static String[] next;

    public SelectLevel(boolean carica) {
        if (carica) {
            setList();
            custom = true;
        } else if (next == null) {
            next = new String[]{""};
        }
        index = -1;
    }

    public SelectLevel(String path) {
        custom = true;
        next = new String[]{path};
        index = -1;
        if(MainComponent.jar.isFile()){
            path = path.replaceFirst("src/", "");
        }
    }

    public String getNext() {
        String s = "";

        index++;
        if (custom && index < next.length) {
            s = next[index];
        } else if (!custom && index < history.length) {
            s = path + history[index];
        } else {
            s = "";
            index--;
        }
        return s;
    }

    public String getBefore() {
        String s = "";
        if (custom && index > 0) {
            if (index >= next.length - 1) {
                index -= 2;
            } else {
                index--;
            }
            s = next[index];
        } else {
            s = "";
        }
        return s;
    }

    public String getCurrent() {
        String s = "";
        if (custom && index < next.length) {
            s = next[index];
        } else if (index < history.length) {
            s = path + history[index];
        } else {
            s = "";
        }
        return s;
    }

    public void reset() {
        custom = false;
        next = null;
        index = -1;
    }

    private void setList() {
        String[] temp = Memoria.getFile(path);
        ArrayList<String> file = new ArrayList<>();
        for (String string : temp) {
            //System.out.println(""+string);
            try{
                string.substring(0, string.lastIndexOf("Compl.level"));
                file.add(string);
            }catch(StringIndexOutOfBoundsException e){
                
            }
        }
        next = new String[file.size()];
        for (int i = 0; i < next.length; i++) {
            next[i] = file.get(i);
            Log.append(next[i], DefaultFont.INFORMATION);
        }
    }
    
    public String[] getList(){
        return next;
    }
    
    public int getIndex(){
        return index;
    }
    
}
