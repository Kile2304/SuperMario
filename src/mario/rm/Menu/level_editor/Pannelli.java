package mario.rm.Menu.level_editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JPanel;
import mario.MainComponent;
import mario.rm.Animation.Animated;
import mario.rm.Animation.Memoria;
import mario.rm.identifier.Move;
import mario.rm.Menu.Componenti.Checkable;
import static mario.rm.Menu.Componenti.Checkable.elenco;
import mario.rm.Menu.Componenti.bottoni.TranslucentButton;
import mario.rm.Menu.Componenti.bottoni.Specifiche;
import mario.rm.identifier.TilePart;
import mario.rm.identifier.Tipologia;
import mario.rm.input.MemoriaAC;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class Pannelli extends JPanel implements ActionListener, Checkable {

    private MemoriaAC memoria;
    private Editor ed;

    private static boolean collider;

    private GridBagConstraints gbc;

    private Insets primo;

    private int HEIGHT;

    private JPanel content;
    private JPanel standard;

    private int gbcX;
    private int gbcY;

    private boolean attach;

    private int row;
    private int columnMax;

    public Pannelli(Editor ed, MemoriaAC memoria, int height) {
        super();
        this.ed = ed;

        row = 0;

        this.HEIGHT = height;

        this.memoria = memoria;

        setLayout(new BorderLayout());

        content = new JPanel();
        standard = new JPanel();

        super.add(content, BorderLayout.CENTER);
        super.add(standard, BorderLayout.NORTH);

        collider = false;

        content.setLayout(new GridBagLayout());
        standard.setLayout(new GridLayout(3, 2, 10, 10));
        standard.setPreferredSize(new Dimension(Editor.adaptedWidth, height / 4));

        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;

        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        gbc.weightx = 1;
        gbc.weighty = 0.5;

        gbc.fill = GridBagConstraints.BOTH;

        int w = (int) (5.0 / 960.0 * Editor.adaptedWidth * 2);
        int h = (int) (5.0 / 540.0 * height);

        // System.out.println(""+w+" "+h);
        primo = new Insets(h, w, h, w);
        gbc.insets = primo;    //gli dico di mettermi uno spazio fra ogni bottone

        gbc.gridx = 0;
        gbc.gridy = 0;

        attach = false;

        JButton indietro = initColor();
        indietro.setText("INDIETRO");
        //indietro.setMaximumSize(new Dimension(100, 100));
        indietro.addActionListener((ActionEvent e) -> {
            pulisci();
            collider = false;
            attach = false;
            ed.getSelezione().normal();
            base();
            ed.getSelezione().checkEraser();
        });
        standard.add(indietro);
        JButton gomma = initColor();
        gomma.setText("GOMMA");
        //gomma.setMaximumSize(new Dimension(100, 100));
        gomma.addActionListener((ActionEvent e) -> {
            elenco.stream().forEach((specifiche) -> {
                specifiche.unCheck();
            });
            ed.getSelezione().action(gomma.getActionCommand());
        });
        standard.add(gomma);

        JButton coll = initColor();
        coll.setText("COLLIDER TILE");
        coll.addActionListener((ActionEvent e) -> {
            Selezione.changeCollider();
            collider();
        });
        standard.add(coll);
        JButton scripta = initColor();
        scripta.setText("SCRIPT");
        scripta.addActionListener((ActionEvent e) -> {
            Selezione.changeAttach();
            attacher();
        });
        standard.add(scripta);
        JButton zoomP = initColor();
        zoomP.setText("+");
        zoomP.addActionListener(this);
        standard.add(zoomP);
        JButton zoomM = initColor();
        zoomM.setText("-");
        zoomM.addActionListener(this);
        standard.add(zoomM);

        pulisci();
        base();
    }

    private TranslucentButton initColor() {
        TranslucentButton button = new TranslucentButton();
        button.setBgCol(Color.gray);
        button.setBgColro(Color.LIGHT_GRAY);
        button.setFgCol(Color.BLACK);
        button.setFgColsel(Color.BLACK);

        return button;
    }

    public void collider() {
        collider = Boolean.logicalXor(collider, true);
        if (collider) {
            pulisci();
            ed.repaint();
            ed.revalidate();
        } else {
            base();
            ed.repaint();
            ed.revalidate();
        }

    }

    public void attacher() {
        attach = Boolean.logicalXor(attach, true);
        if (attach) {
            pulisci();
            ed.repaint();
            ed.revalidate();
        } else {
            base();
            ed.repaint();
            ed.revalidate();
        }
    }

    private void base() {

        columnMax = 2;

        gbc.gridx = 0;
        gbc.gridy = 0;

        Specifiche enemy = new Specifiche("NEMICI");
        enemy.getButton().addActionListener((ActionEvent e) -> {
            nemici();
        });
        Specifiche item = new Specifiche("ITEM");
        item.getButton().addActionListener((ActionEvent e) -> {
            item();
        });
        Specifiche player = new Specifiche("PLAYER");
        player.getButton().addActionListener((ActionEvent e) -> {
            player();
        });
        Specifiche terreni = new Specifiche("TERRENI");
        terreni.getButton().addActionListener((ActionEvent e) -> {
            choseTerrain();
        });

        Specifiche tile = new Specifiche("TILE");
        tile.getButton().addActionListener((ActionEvent e) -> {
            terreni("/tile/other", false);

            BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    img.setRGB(j, i, (255 & 0xFF));
                }
            }
            elenco.add(new Specifiche("VOID", img, ""));
            add(elenco.get(elenco.size() - 1).getButton());
            elenco.get(elenco.size() - 1).getButton().addActionListener(this);

            resize(1 + elenco.size() / 2);

            ed.repaint();
            ed.revalidate();
        });
        elenco.add(enemy);
        elenco.add(item);
        elenco.add(player);
        elenco.add(terreni);
        elenco.add(tile);
        elenco.stream().forEach((specifiche) -> {
            add(specifiche.getButton());
        });
        //System.out.println("" + 2 + elenco.size() / 2 + " " + 2 + (elenco.size() / 2) + " " + elenco.size());
        resize((elenco.size()) / 2 + elenco.size() % 2);

        //System.out.println("" + getPreferredSize().toString());
        ed.repaint();
        ed.revalidate();
    }

    private void choseTerrain() {   //Ottiene in input l'elenco dei terreni e aggiunge un bottone per ogni terreno
        gbc.gridx = 0;
        pulisci();
        String[] temp = null;
        temp = Memoria.getDirectory("Animazioni/tile/terrain");

        for (int i = 0; i < temp.length; i++) {
            String a = null;
            if (MainComponent.jar.isFile()) {
                StringBuilder s = new StringBuilder(temp[i]);
                s.deleteCharAt(s.length() - 1);
                a = s.substring(s.lastIndexOf("/") + 1, s.length());
            } else {
                a = temp[i].substring(temp[i].lastIndexOf("\\") + 1, temp[i].length());
            }
            String t1 = a;  //il listener sotto vuole solo valori final e questo e' l'unico modo
            Specifiche spec = new Specifiche(a.toUpperCase());

            spec.getButton().addActionListener((ActionEvent e) -> {
                terreni("/tile/terrain/" + t1, true);
            });
            elenco.add(spec);
        }

        elenco.stream().forEach((specifiche) -> {
            specifiche.setTerrain(true);
            add(specifiche.getButton());
        });

        ed.repaint();
        ed.revalidate();
    }

    private void terreni(String path, boolean terr) {
        pulisci();
        if (path.endsWith("_col")) {
            columnMax = 1;
        } else {
            columnMax = 3;
        }
        //columnMax = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        ArrayList temp = new ArrayList<>();
        temp = memoria.getAnim("Animazioni" + path, temp);
        //System.out.println("" + path);
        addButton(temp);
        if (terr) {
            elenco.stream().filter((spec) -> (spec.getButton().getText().equals(""))).forEach((spec) -> {
                spec.setTerrain(true);
            });
        }
        //System.out.println("asdasd" + elenco.size());
        //System.out.println("" + 1 + elenco.size() / 2 + " " + 1 + (elenco.size() / 2));
        resize((elenco.size()) / 2 + elenco.size() % 2);
        //System.out.println("" + getPreferredSize().toString());
    }

    private void item() {
        pulisci();
        columnMax = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        ArrayList temp = memoria.getUnlockable();
        //System.out.println(""+temp.size());
        addButton(temp);
        System.out.println("" + 1 + elenco.size() / 2 + " " + 1 + (elenco.size() / 2));
        resize((elenco.size()) / 2 + elenco.size() % 2);
        System.out.println("" + getPreferredSize().toString());
    }

    private void nemici() {
        pulisci();
        columnMax = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        ArrayList temp = memoria.getEnemy();
        addButton(temp);
        System.out.println("" + 1 + elenco.size() / 2 + " " + 1 + (elenco.size() / 2));
        resize((elenco.size()) / 2 + elenco.size() % 2);
    }

    private void player() {
        pulisci();
        columnMax = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        ArrayList temp = memoria.getPlayer();
        addButton(temp);
        System.out.println("" + 1 + elenco.size() / 2 + " " + 1 + (elenco.size() / 2));
        resize((elenco.size()) / 2 + elenco.size() % 2);
    }

    private void addButton(ArrayList<Animated> temp) {  //aggiunge i bottoni partendo avendo in input Tile ed Anim
        if (temp != null) {
            for (int i = 0; i < temp.size(); i++) {
                if (Tipologia.getValue(temp.get(i).getType())) {
                    int repeat = 1;
                    TilePart[] part = null;
                    try {
                        Move.valueOf(temp.get(i).getTile());
                    } catch (IllegalArgumentException e) {
                        part = TilePart.values();
                        repeat = part.length;
                    }

                    for (int h = 0; h < repeat; h++) {
                        String til = "";
                        if (repeat != 1) {
                            til = part[h].name();
                        }
                        //System.out.println(""+til);
                        BufferedImage img = temp.get(i).getBgImage(til);

                        if (img != null) {
                            Specifiche s = new Specifiche(temp.get(i).getType(), img, til);
                            System.out.println("" + temp.get(i).getType());
                            s.setTerrain(false);
                            s.getButton().addActionListener(this);
                            elenco.add(s);
                            add(elenco.get(elenco.size() - 1).getButton());
                        }
                    }
                }
                //}

            }
            //System.out.println(""+(4 +elenco.size()/2));
            //setLayout(new GridLayout(3 + elenco.size(), 2, 25, 25));
        } else {
            Log.append("Nessuno sprite trovato\n" + (Pannelli.class).getName() + "\n", DefaultFont.ERROR);
        }
        ed.repaint();
        ed.revalidate();
    }

    private void resize(int numBottoni) {
        final int height = numBottoni * adaptHeight(170);

        Dimension d = new Dimension(Editor.adaptedWidth, height);

        setPreferredSize(d);
        System.out.println("heightRefact: " + height);
        ed.getPanelScroll().setPreferredSize(new Dimension(d.width, d.height + adaptHeight(50)));
        //revalidate();
        //repaint();
    }

    public void pulisci() {
        gbc.gridx = 0;
        gbc.gridy = 0;
        row = 1;
        for (int i = 0; i < elenco.size(); i++) {
            content.remove(elenco.get(i).getButton());
        }
        elenco.clear();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Specifiche s = new Specifiche();
        if ((e.getSource() instanceof JButton)) {
            s.setButton((JButton) e.getSource());

            elenco.stream().forEach((specifiche) -> {
                if (s.getButton().toString().equals(specifiche.getButton().toString())) {
                    specifiche.setCheck();
                } else {
                    specifiche.unCheck();
                }
            });
            s.setCheck();
        }
        ed.getSelezione().action(e.getActionCommand());
    }

    @Override
    public Specifiche getChecked() {
        for (Iterator<Specifiche> it = elenco.iterator(); it.hasNext();) {
            Specifiche specifiche = it.next();
            if (specifiche.isCheck()) {
                return specifiche;
            }
        }
        return null;
    }

    @Override
    public Component add(Component c) {
        content.add(c, gbc);
        gbc.gridx = row % columnMax;
        gbc.gridy += (row / columnMax);
        //System.out.println("X: " + gbc.gridx + " Y: " + gbc.gridy);
        row %= (columnMax);
        row++;
        return null;
    }

    public int adaptHeight(int val) {
        return (int) ((double) val / 900 * HEIGHT);
    }
}
