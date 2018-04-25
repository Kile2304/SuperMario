package mario.rm.Menu.sprite_estractor.output.union;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mario.MainComponent;
import mario.rm.Animation.Anim;
import mario.rm.Animation.Cut;
import mario.rm.Animation.Memoria;
import mario.rm.Animation.Tile;
import mario.rm.identifier.Direction;
import mario.rm.identifier.TilePart;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class Union {

    public Union(JFrame fr) {
        Memoria m = new Memoria();
        m.carica();

        ArrayList<Cut> p = m.getPlayer();
        ArrayList<Cut> temp = m.getTiles();
        temp.stream().forEach((cut) -> {
            p.add(cut);
        });
        temp = m.getTerreni();
        temp.stream().forEach((cut) -> {
            p.add(cut);
        });
        m.getEnemy().stream().forEach((enemy) -> {
            p.add(enemy);
        });

        LinkedList<Anim> anim = new LinkedList<>();
        LinkedList<Tile> tile = new LinkedList<>();
        p.stream().forEach((cut) -> {
            boolean nuovo = true;
            if (cut.getTile() != null && !cut.getTile().equals("")) {
                boolean isJustAdded = false;
                for (Tile tile1 : tile) {
                    if (cut.getType().equals(tile1.getType())) {
                        tile1.addAnimation(cut.getNormal(), TilePart.valueOf(cut.getTile()));
                        isJustAdded = true;
                    }
                }
                if (!isJustAdded) {
                    if (cut.getUnlockable() == null) {
                        tile.add(new Tile(cut.getNormal(), cut.getTile(), cut.getPath(), cut.getType()));
                    } else {
                        tile.add(new Tile(cut.getNormal(), cut.getTile(), cut.getPath(), cut.getType(), cut.getUnlockable()));
                    }
                }
            } else {
                for (Anim a : anim) {
                    if (cut.getType().equals(a.getType())) {
                        nuovo = false;
                        if (cut.getTransformation() == null || cut.getTransformation().equals("normal")) {
                            a.addAnimation(cut.getNormal(), cut.getMove(), cut.getDirection());
                            a.addAnimation(cut.getMirror(), cut.getMove(), cut.getDirection() == Direction.RIGHT ? Direction.LEFT : Direction.RIGHT);
                        }
                    } else {
                        a.getSuper().addAnimation(cut.getNormal(), cut.getMove(), cut.getDirection());
                        a.getSuper().addAnimation(cut.getMirror(), cut.getMove(), cut.getDirection() == Direction.RIGHT ? Direction.LEFT : Direction.RIGHT);
                    }
                }
                if (nuovo) {
                    //System.out.println("" + cut.getPath());
                    anim.add(new Anim(cut.getType(), cut.getPath()));
                    if (cut.getTransformation() == null || cut.getTransformation().equals("normal")) {
                        System.out.println(""+cut.getMove()+" "+cut.getDirection()+" "+cut.getType());
                        anim.getLast().addAnimation(cut.getNormal(), cut.getMove(), cut.getDirection());
                        anim.getLast().addAnimation(cut.getMirror(), cut.getMove(), cut.getDirection() == Direction.RIGHT ? Direction.LEFT : Direction.RIGHT);
                    } else {
                        anim.getLast().getSuper().addAnimation(cut.getNormal(), cut.getMove(), cut.getDirection());
                        anim.getLast().getSuper().addAnimation(cut.getMirror(), cut.getMove(), cut.getDirection() == Direction.RIGHT ? Direction.LEFT : Direction.RIGHT);
                    }
                }
            }
        });
        m.clean();

        anim.stream()
                .forEach((anim1) -> {
                    toObject(anim1, anim1.getPath(), "");
                }
                );
        tile.stream()
                .forEach((tile1) -> {
                    toObject(tile1, tile1.getPath(), tile1.getTile());
                }
                );

        try {   //solo da build
            m = null;
            p.clear();
            temp.clear();

            Runtime.getRuntime().exec("cmd /c start " + new File(MainComponent.filePath + "/Luigi/Animation/eliminazioneAnim.bat") + " " + new File(MainComponent.filePath + "/Luigi/Animation").getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(Union.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (fr != null) {
            JOptionPane.showMessageDialog(fr, "Conversione dei file in AC e TI completata", "info", JOptionPane.DEFAULT_OPTION);
        }

    }

    public static void checkBatFile() throws IOException {
        File f = new File(MainComponent.filePath + "/Luigi/Animation/eliminazioneAnim.bat");
        if (!f.isFile()) {
            f.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.append("title first batch program\n"
                + "\n"
                + "ECHO \"Script per la cancellazione deglle animazioni\"\n"
                + "\n"
                + "del /s /Q %1\\\\tile\\\\*.anim\n"
                + "del /s /Q %1\\\\player\\\\*.anim\n"
                + "del /s /Q %1\\\\enemy\\\\*.anim\n"
                + "\n"
                + "ECHO \"Eliminazione completata con successo\"");
        bw.close();
    }


    private void toObject(Object anim1, String path, String tile) {
        FileOutputStream fos = null;
        try {
            String newFile = "";
            try {
                newFile = path.substring(path.lastIndexOf("/"));
            } catch (StringIndexOutOfBoundsException e) {
            }
            System.out.println("percorso"+path);
            System.out.println("acaca "+newFile);

            //File directory = new File("src\\mario\\res\\Animazioni\\" + path);
            //System.out.println("attuale: "+path);
            File directory = new File(MainComponent.filePath + "/Luigi/Animation/" + path);
            Log.append("checker: " + directory.getAbsolutePath(), DefaultFont.INFORMATION);

            File[] files = directory.listFiles();
            //System.out.println("" + directory.length());

            for (File f : files) {
                Log.append("" + f.getAbsolutePath(), DefaultFont.INFORMATION);
                String s = f.getAbsolutePath();
                s = s.substring(s.lastIndexOf("."));
                /*if (s.equals(".anim")) {
                    f.deleteOnExit();
                }*/
            }

            if (anim1 instanceof Anim) {
                fos = new FileOutputStream(directory.getAbsolutePath() + "\\" + newFile + ".ac");
            } else {
                fos = new FileOutputStream(MainComponent.filePath + "/Luigi/Animation/" + path + "/" + newFile + "-" + tile + ".ti");
            }
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(anim1);

        } catch (FileNotFoundException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        } catch (IOException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
            }
        }
    }

    public static void main(String[] args) {
        new Union(null);
    }

}
