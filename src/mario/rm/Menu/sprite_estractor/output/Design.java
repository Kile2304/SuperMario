package mario.rm.Menu.sprite_estractor.output;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import mario.rm.Menu.sprite_estractor.union.Union;
import mario.rm.identifier.Direction;
import mario.rm.identifier.Move;
import mario.rm.identifier.Type;
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;
import mario.rm.utility.Punto;

/**
 *
 * @author LENOVO
 */
public class Design {

    public Design(JFrame fr) {
        BufferedReader br = null;
        ArrayList<Estratta> lista = new ArrayList<>();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/mario/res/Animazioni/list.txt"))));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Design.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String temp = "";
            ArrayList<Integer> punto = new ArrayList<>();
            while ((temp = br.readLine()) != null) {
                String val = "";
                for (int i = 0; i < temp.length(); i++) {
                    switch (temp.charAt(i)) {
                        case '+':
                            lista.add(new Estratta());
                            break;
                        case '[':
                        case '{':
                        case '!':
                        case '<':
                        case '♣':
                        case '◘':
                        case '☼':
                        case '§':
                            val = "";
                            break;
                        case ']':
                            lista.get(lista.size() - 1).setPath(val);
                            break;
                        case '|':
                            if (lista.get(lista.size() - 1).getType() == null) {
                                lista.get(lista.size() - 1).setType(Type.valueOf(val));
                            }else{
                                lista.get(lista.size() - 1).setUnlock(Type.valueOf(val));
                            }
                            break;
                        case '}':
                            lista.get(lista.size() - 1).setMove(Move.valueOf(val));
                            break;
                        case '>':
                            punto.add(Integer.parseInt(val));
                            if (punto.size() == 4) {
                                lista.get(lista.size() - 1).setPunto(new Punto(punto.get(0), punto.get(1)), new Punto(punto.get(2), punto.get(3)));
                                punto.clear();
                            }
                            break;
                        case '♦':
                            lista.get(lista.size() - 1).setDirection(Direction.valueOf(val));
                            break;
                        case '○':
                            lista.get(lista.size() - 1).setNomeFile(val);
                            break;
                        case '♪':
                            lista.get(lista.size() - 1).setTransformation(val);
                            break;
                        case '*':
                            lista.get(lista.size() - 1).setTile(val);
                            break;
                        default:
                            val += temp.charAt(i);
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }
        lista.stream().forEach((estratta) -> {
            estratta.output();
        });

        new Union(fr);

    }

    public static void main(String[] args) {
        new Design(null);
    }

}
