package mario.rm.Menu.sprite_estractor.union;

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
import mario.rm.utility.DefaultFont;
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

        ArrayList<Anim> anim = new ArrayList<>();
        ArrayList<Tile> tile = new ArrayList<>();

        p.stream().forEach((cut) -> {
            //System.out.println(""+cut.getNormal().length);
            boolean nuovo = true;
            if (cut.getTile() != null && !cut.getTile().equals("")) {
                boolean isJustAdded = false;
                for (Tile tile1 : tile) {
                    if (cut.getType() == tile1.getType()) {
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
                    if (cut.getType() == a.getType()) {
                        nuovo = false;
                        if (cut.getTransformation().equals("normal")) {
                            a.addAnimation(cut.getNormal(), cut.getMove(), cut.getDirection());
                            a.addAnimation(cut.getMirror(), cut.getMove(), cut.getDirection() == Direction.RIGHT ? Direction.LEFT : Direction.RIGHT);
                        }
                    } else {
                        a.getSuper().addAnimation(cut.getNormal(), cut.getMove(), cut.getDirection());
                        a.getSuper().addAnimation(cut.getMirror(), cut.getMove(), cut.getDirection() == Direction.RIGHT ? Direction.LEFT : Direction.RIGHT);
                    }
                }
                if (nuovo) {
                    System.out.println("" + cut.getPath());
                    anim.add(new Anim(cut.getType(), cut.getPath()));
                    if (cut.getTransformation() != null && cut.getTransformation().equals("normal")) {
                        anim.get(anim.size() - 1).addAnimation(cut.getNormal(), cut.getMove(), cut.getDirection());
                        anim.get(anim.size() - 1).addAnimation(cut.getMirror(), cut.getMove(), cut.getDirection() == Direction.RIGHT ? Direction.LEFT : Direction.RIGHT);
                    } else {
                        anim.get(anim.size() - 1).getSuper().addAnimation(cut.getNormal(), cut.getMove(), cut.getDirection());
                        anim.get(anim.size() - 1).getSuper().addAnimation(cut.getMirror(), cut.getMove(), cut.getDirection() == Direction.RIGHT ? Direction.LEFT : Direction.RIGHT);
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
            Runtime.getRuntime().exec("cmd /c start " + new File("res/Animazioni/eliminazioneAnim.bat") + " " + new File("res/Animazioni/").getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(Union.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (fr != null) {
            JOptionPane.showMessageDialog(fr, "Conversione dei file in AC e TI completata", "info", JOptionPane.DEFAULT_OPTION);
        }

    }

    /*private void tempFile() {
        try {
            InputStreamReader isr = new InputStreamReader(MainComponent.class.getClassLoader().getResourceAsStream("Animazioni\\eliminazioneAnim.bat"));
            BufferedReader br = new BufferedReader(isr);

            File f = new File(System.getProperty("user.dir") + "\\" + "eliminazioneFileAc.bat");
            f.createNewFile();

            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            String temp = "";
            while ((temp = br.readLine()) != null) {
                bw.append(temp);
            }
            bw.close();
            br.close();
            
            try {
                Runtime.getRuntime().exec("cmd /c start " + System.getProperty("user.dir") + "\\" + "eliminazioneFileAc.bat"+" "+MainComponent.class.getClassLoader().getResource("Animazioni\\eliminazioneAnim.bat"));
            } catch (IOException ex) {
                Logger.getLogger(Union.class.getName()).log(Level.SEVERE, null, ex);
            }
            f.delete();

        } catch (IOException ex) {
            Logger.getLogger(Union.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    private void toObject(Object anim1, String path, String tile) {
        FileOutputStream fos = null;
        try {
            String newFile = "";
            try {
                newFile = path.substring(path.lastIndexOf("\\"));
            } catch (StringIndexOutOfBoundsException e) {
            }

            //File directory = new File("src\\mario\\res\\Animazioni\\" + path);
            //System.out.println("attuale: "+path);
            File directory = new File("res\\Animazioni\\" + path);
            Log.append("checker: " + directory.getAbsolutePath(), DefaultFont.INFORMATION);

            File[] files = directory.listFiles();
            System.out.println("" + directory.length());

            for (File f : files) {
                Log.append("" + f.getAbsolutePath(), DefaultFont.INFORMATION);
                String s = f.getAbsolutePath();
                s = s.substring(s.lastIndexOf("."));
                /*if (s.equals(".anim")) {
                    f.deleteOnExit();
                }*/
            }

            if (anim1 instanceof Anim) {
                fos = new FileOutputStream("res\\Animazioni\\" + path + "\\" + newFile + ".ac");
            } else {
                fos = new FileOutputStream("res\\Animazioni\\" + path + "\\" + newFile + "-" + tile + ".ti");
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
