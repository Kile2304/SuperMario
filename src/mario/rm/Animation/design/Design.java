package mario.rm.Animation.design;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mario.rm.identifier.Move;
import mario.rm.utility.Punto;
import mario.rm.identifier.Type;

/**
 *
 * @author LENOVO
 */
public class Design {

    public Design() {
        BufferedReader br = null;
        ArrayList<Estratta> lista = new ArrayList<>();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("file.txt"))));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Design.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String temp = "";
            ArrayList<Integer> punto = new ArrayList<>();
            while ((temp = br.readLine()) != null) {
                String val = "";
                for (int i = 0; i < temp.length(); i++) {
                    if (temp.charAt(i) == '-') {
                        lista.add(new Estratta());
                    } else if (temp.charAt(i) == '[' || temp.charAt(i) == '{' || temp.charAt(i) == '!' || temp.charAt(i) == '<') {
                        val = "";
                    } else if (temp.charAt(i) == ']') {
                        lista.get(lista.size() - 1).setPath(val);
                    } else if (temp.charAt(i) == '|') {
                        lista.get(lista.size() - 1).setType(Type.valueOf(val));
                    } else if (temp.charAt(i) == '}') {
                        lista.get(lista.size() - 1).setMove(Move.valueOf(val));
                    } else if (temp.charAt(i) == '>') {
                        punto.add(Integer.parseInt(val));
                        if (punto.size() == 4) {
                            lista.get(lista.size() - 1).setPunto(new Punto(punto.get(0), punto.get(1)), new Punto(punto.get(2), punto.get(3)));
                            punto.clear();
                        }
                    } else {
                        val += temp.charAt(i);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Design.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Estratta estratta : lista) {
            estratta.output();
        }
    }

    public static void main(String[] args) {
        new Design();
    }

}
