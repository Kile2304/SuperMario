package mario.rm.Menu.sprite_estractor.union;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mario.rm.Animation.Anim;
import mario.rm.Animation.Cut;
import mario.rm.Animation.Tile;
import mario.rm.identifier.Direction;
import mario.rm.identifier.TilePart;
import mario.rm.input.MemoriaAnim;

/**
 *
 * @author LENOVO
 */
public class Union {

    public Union(JFrame fr) {
        MemoriaAnim m = new MemoriaAnim();
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
                            if (cut.getDirection() == Direction.RIGHT) {
                                anim.get(anim.size() - 1).addAnimation(cut.getMirror(), cut.getMove(), Direction.LEFT);
                            }
                        } else {
                            a.getSuper().addAnimation(cut.getNormal(), cut.getMove(), cut.getDirection());
                            if (cut.getDirection() == Direction.RIGHT) {
                                anim.get(anim.size() - 1).getSuper().addAnimation(cut.getMirror(), cut.getMove(), Direction.LEFT);
                            }
                        }
                    }
                }
                if (nuovo) {
                    System.out.println("" + cut.getPath());
                    anim.add(new Anim(cut.getType(), cut.getPath()));
                    if (cut.getTransformation().equals("normal")) {
                        anim.get(anim.size() - 1).addAnimation(cut.getNormal(), cut.getMove(), cut.getDirection());
                        if (cut.getDirection() == Direction.RIGHT) {
                            anim.get(anim.size() - 1).addAnimation(cut.getMirror(), cut.getMove(), Direction.LEFT);
                        }
                    } else {
                        anim.get(anim.size() - 1).getSuper().addAnimation(cut.getNormal(), cut.getMove(), cut.getDirection());
                        if (cut.getDirection() == Direction.RIGHT) {
                            anim.get(anim.size() - 1).getSuper().addAnimation(cut.getMirror(), cut.getMove(), Direction.LEFT);
                        }
                    }
                }
            }
        });
        m.clean();
        anim.stream().forEach((anim1) -> {
            toObject(anim1, anim1.getPath(), "");
        });
        tile.stream().forEach((tile1) -> {
            toObject(tile1, tile1.getPath(), tile1.getTile());
        });
        
        if(fr != null){
            JOptionPane.showMessageDialog(fr, "Conversione dei file in AC e TI completata", "info", JOptionPane.DEFAULT_OPTION);
        }
        
    }

    private void toObject(Object anim1, String path, String tile) {
        FileOutputStream fos = null;
        try {
            String newFile = "";
            try {
                newFile = path.substring(path.lastIndexOf("\\"));
            } catch (StringIndexOutOfBoundsException e) {
            }

            File directory = new File("src\\mario\\res\\Animazioni\\" + path);
            System.out.println("checker: " + directory.getAbsolutePath());

            File[] files = directory.listFiles();

            for (File f : files) {
                System.out.println("" + f.getAbsolutePath());
                String s = f.getAbsolutePath();
                s = s.substring(s.lastIndexOf("."));
                if (s.equals(".anim")) {
                    f.deleteOnExit();
                }
            }

            if (anim1 instanceof Anim) {
                fos = new FileOutputStream("src\\mario\\res\\Animazioni\\" + path + "\\" + newFile + ".ac");
            } else {
                fos = new FileOutputStream("src\\mario\\res\\Animazioni\\" + path + "\\" + newFile + "-" + tile + ".ti");
            }
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(anim1);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Union.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Union.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(Union.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        new Union(null);
    }

}
