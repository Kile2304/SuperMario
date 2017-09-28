package mario.rm.Menu.sprite_estractor.input;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import mario.rm.Menu.Griglia;
import mario.rm.identifier.Type;
import mario.rm.utility.RGB;

/**
 *
 * @author LENOVO
 */
public class Collegamenti {

    private ArrayList<Punto> temp;
    private ArrayList<Punto[]> zone;
    private String file;

    public Collegamenti() {
        temp = new ArrayList<>();
        zone = new ArrayList<>();
        file = "";
        Griglia.clean();
    }

    public Collegamenti(String file) {
        temp = new ArrayList<>();
        zone = new ArrayList<>();
        this.file = file;
    }

    public void partialClean() {
        temp.clear();
        zone.clear();
        Griglia.clean();
    }

    public Punto[] nuovoPunto(int colonna, int riga) {
        temp.add(new Punto(colonna, riga));
        ArrayList<Punto> nuovi = new ArrayList<>();
        Punto[] t = null;
        if (temp.size() == 2) {
            Punto primo;
            Punto secondo;

            zone.remove(zone.size() - 1);

            if (temp.get(temp.size() - 1).getX() > temp.get(temp.size() - 2).getX()) {
                primo = temp.get(temp.size() - 1);
                secondo = temp.get(temp.size() - 2);
            } else {
                secondo = temp.get(temp.size() - 1);
                primo = temp.get(temp.size() - 2);
            }
            nuovi.add(temp.get(0));
            for (int i = 0; i < primo.getX() - secondo.getX(); i++) {
                nuovi.add(new Punto(primo.getX() - i, primo.getY()));
                nuovi.add(new Punto(secondo.getX() + i, secondo.getY()));
            }

            if (temp.get(temp.size() - 1).getY() > temp.get(temp.size() - 2).getY()) {
                primo = temp.get(temp.size() - 1);
                secondo = temp.get(temp.size() - 2);
            } else {
                secondo = temp.get(temp.size() - 1);
                primo = temp.get(temp.size() - 2);
            }

            for (int i = 0; i <= primo.getY() - secondo.getY(); i++) {
                nuovi.add(new Punto(primo.getX(), primo.getY() - i));
                nuovi.add(new Punto(secondo.getX(), secondo.getY() + i));
            }
            t = new Punto[nuovi.size() + 1];
            for (int i = 0; i < nuovi.size(); i++) {
                t[i] = nuovi.get(i);
            }
            t[nuovi.size()] = temp.get(1);
            zone.add(t);
            temp.clear();
        } else {
            t = new Punto[]{new Punto(colonna, riga)};
            zone.add(t);
        }

        return t;
    }

    public Punto[] rimuoviCollegamento(int colonna, int riga) {
        Punto del = new Punto(colonna, riga);
        Punto[] eliminare = null;
        for (int i = 0; i < zone.size(); i++) {
            for (int j = 0; j < zone.get(i).length; j++) {
                if (zone.get(i)[j].compare(del)) {
                    eliminare = zone.get(i);
                    zone.remove(zone.get(i));
                    return eliminare;
                }
            }
        }

        return eliminare;
    }

    public void estrazione() {  //compila il file che estrarra' le immagini, inoltre modifica i file java per permetterne l'inserimento

        if (!file.equals("")) { //se file e' == a "", vuol dire che non e' stata caricata alcuna immagine, ed estrarre un immagine da nulla non e' possibile

            FileWriter file = null;
            try {
                file = new FileWriter("src/mario/res/Animazioni/list.txt", true);
            } catch (IOException ex) {
                Logger.getLogger(Collegamenti.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedWriter filebuf = new BufferedWriter(file);
            PrintWriter printout = new PrintWriter(filebuf);
            printout.append("+[" + this.file + "]\n");
            printout.append("!" + Pannelli.type.getText().toUpperCase() + "|\n");  //Type
            if (Pannelli.isPlayer.isSelected()) { //queste informazioni, sono scritte sul file solo in caso lo sprite sia un player
                printout.append("{" + Pannelli.move.getSelectedItem().toString() + "}\n");  //scrivo il tipo di movimento
                printout.append("♣" + Pannelli.direction.getSelectedItem().toString() + "♦\n"); //scrivo la direzione di movimento
                printout.append("☼" + Pannelli.transformation.getSelectedItem().toString() + "♪\n");    //scrivo il tipo di trasformazione
            } else {    //queste informazioni, sono scritte solo nel caso sia un tile
                //add tile
                printout.append("§" + Pannelli.tile.getSelectedItem().toString() + "*\n");  //tipo di direzione
                if (Pannelli.isUnlockable.isSelected()) {
                    printout.append("!" + Pannelli.unlockType.getSelectedItem() + "|");
                }
            }
            printout.append("◘" + Pannelli.fileName.getText() + "○\n"); //scrivo il nome del file
            for (Punto[] punto : zone) {    //scrivo i punti che dovranno essere ritagliati
                if(punto[0].getX() == punto[1].getX() && punto[0].getY() == punto[1].getY()){
                    continue;
                }
                printout.append("<" + punto[0].getX() + "><" + punto[0].getY() + "> "); //punto di inizio
                printout.append("<" + punto[punto.length - 1].getX() + "><" + punto[punto.length - 1].getY() + ">");    //punto di fine
                printout.append("\n");
            }
            printout.append("\n");
            printout.close();
            try {
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(Collegamenti.class.getName()).log(Level.SEVERE, null, ex);
            }
            String type = Pannelli.type.getText().toUpperCase() + " (0, 0, 0, 0, 0)";    //questa e' la nuova stringa da aggiungere al file type, nel caso fosse un nuovo type
            try {
                Type t = Type.valueOf(Pannelli.type.getText().toUpperCase());   //controllo se il type inserito e' in elenco
            } catch (IllegalArgumentException e) {
                File f = new File("src/mario/rm/identifier/Type.java");
                overrideJavaFile(f, "src/mario/rm/identifier/Type.java", type, "private", true);    //override file type
            }

        } else {    //non e' stata caricata in memoria nessuna immagine
            JOptionPane.showMessageDialog(null, "NON E' STATA CARICATA NESSUNA IMMAGINE", "ERRORE", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean overrideJavaFile(File in, String nomeFile, String type, String confronto, boolean punteggiatura) {  //scrive sui file java

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(in);
            isr = new InputStreamReader(fis);

            br = new BufferedReader(isr);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Collegamenti.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean overwrite = false;
        String temp = "";
        StringBuilder nuovoType = new StringBuilder();
        try {
            while ((temp = br.readLine()) != null) {
                String word = "";
                for (int i = 0; i < temp.length(); i++) {
                    if (temp.charAt(i) == ';'  && temp.charAt(i-1) == ')' && !overwrite) {
                        //nuovoType.append(",\n\t" + type + "\n");
                        word += (",\n    " + type);
                        overwrite = true;
                    }
                    word += temp.charAt(i);
                }
                /*if (!word.equals("")) {
                    nuovoType.append(word);
                }*/
                nuovoType.append(word+"\n");
            }
            System.out.println("" + nuovoType.toString());

            br.close();
            isr.close();
            fis.close();

            FileWriter file = null;
            try {
                file = new FileWriter(nomeFile);
            } catch (IOException ex) {
                Logger.getLogger(Collegamenti.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedWriter filebuf = new BufferedWriter(file);
            PrintWriter printout = new PrintWriter(filebuf);
            printout.append(nuovoType.toString());

            printout.close();
            filebuf.close();
            file.close();
        } catch (IOException ex) {
            Logger.getLogger(Collegamenti.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

}
