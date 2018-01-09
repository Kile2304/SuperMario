package mario.rm.Menu.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import mario.rm.Menu.Specifiche;
import mario.rm.SuperMario;
import mario.rm.identifier.TilePart;
import mario.rm.identifier.Type;
import mario.rm.input.MemoriaAC;
import mario.rm.utility.DefaultFont;
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
    private Insets secondo;
    
    private int HEIGHT;

    public Pannelli(Editor ed, MemoriaAC memoria, int height) {
        super();
        this.ed = ed;
        
        this.HEIGHT = height;
        
        this.memoria = memoria;

        collider = false;
        
        setLayout(new GridBagLayout());
        
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        
        gbc.weightx = 1;
        gbc.weighty = 0.9;
        
        gbc.fill = GridBagConstraints.BOTH;

        int w = (int) (5.0 / 960.0 * Editor.adaptedWidth * 2);
        int h = (int) (5.0 / 540.0 * height);
        
        // System.out.println(""+w+" "+h);
        primo = new Insets(h, w, h, w); 
        gbc.insets = primo;    //gli dico di mettermi uno spazio fra ogni bottone
        
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        JButton indietro = initColor();
        indietro.setText("INDIETRO");
        //indietro.setMaximumSize(new Dimension(100, 100));
        indietro.addActionListener((ActionEvent e) -> {
            pulisci();
            base();
            ed.getSelezione().checkEraser();
        });
        add(indietro);
        JButton gomma = initColor();
        gomma.setText("GOMMA");
        //gomma.setMaximumSize(new Dimension(100, 100));
        gomma.addActionListener((ActionEvent e) -> {
            elenco.stream().forEach((specifiche) -> {
                specifiche.unCheck();
            });
            ed.getSelezione().action(gomma.getActionCommand());
        });
        add(gomma);

        JButton coll = initColor();
        coll.setText("COLLIDER TILE");
        coll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Selezione.changeCollider();
                collider();
            }

        });
        add(coll);
        JButton zoomP = initColor();
        zoomP.setText("+");
        zoomP.addActionListener(this);
        add(zoomP);
        JButton zoomM = initColor();
        zoomM.setText("-");
        zoomM.addActionListener(this);
        add(zoomM);

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

    private void base() {

        gbc.gridx = 1;
        gbc.gridy = 2;
        
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
            elenco.add(new Specifiche(Type.VOID, img, ""));
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
        System.out.println(""+2 + elenco.size() / 2+" "+2 + (elenco.size() / 2)+" "+elenco.size());
        resize((4 + elenco.size()) / 2 + elenco.size() % 2);

        System.out.println(""+getPreferredSize().toString());
        
        ed.repaint();
        ed.revalidate();
    }

    private void choseTerrain() {   //Ottiene in input l'elenco dei terreni e aggiunge un bottone per ogni terreno
        gbc.gridx = 1;
        pulisci();
        String[] temp = null;
        temp = Memoria.getDirectory("Animazioni/tile/terrain");

        for (int i = 0; i < temp.length; i++) {
            String a = null;
            if(MainComponent.jar.isFile()){
                StringBuilder s = new StringBuilder(temp[i]);
                s.deleteCharAt(s.length() - 1);
                a = s.substring(s.lastIndexOf("/") + 1, s.length());
            }else {
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
        gbc.gridx = 1;
        gbc.gridy = 2;
        ArrayList temp = new ArrayList<>();
        temp = memoria.getAnim("Animazioni" + path, temp);
        addButton(temp);
        if (terr) {
            elenco.stream().filter((spec) -> (spec.getButton().getText().equals(""))).forEach((spec) -> {
                spec.setTerrain(true);
            });
        }
        System.out.println("asdasd"+elenco.size());
        System.out.println(""+1 + elenco.size() / 2+" "+1 + (elenco.size() / 2));
        resize((4 + elenco.size()) / 2 + elenco.size() % 2);
        System.out.println(""+getPreferredSize().toString());
    }

    private void item() {
        pulisci();
        gbc.gridx = 1;
        gbc.gridy = 2;
        ArrayList temp = memoria.getUnlockable();
        //System.out.println(""+temp.size());
        addButton(temp);
        System.out.println(""+1 + elenco.size() / 2+" "+1 + (elenco.size() / 2));
        resize((4 + elenco.size()) / 2 + elenco.size() % 2);
        System.out.println(""+getPreferredSize().toString());
    }

    private void nemici() {
        pulisci();
        gbc.gridx = 1;
        gbc.gridy = 2;
        ArrayList temp = memoria.getEnemy();
        addButton(temp);
        System.out.println(""+1 + elenco.size() / 2+" "+1 + (elenco.size() / 2));
        resize((4 + elenco.size()) / 2 + elenco.size() % 2);
    }

    private void player() {
        pulisci();
        gbc.gridx = 1;
        gbc.gridy = 2;
        ArrayList temp = memoria.getPlayer();
        addButton(temp);
        System.out.println(""+1 + elenco.size() / 2+" "+1 + (elenco.size() / 2));
        resize((4 + elenco.size()) / 2 + elenco.size() % 2);
    }

    private void addButton(ArrayList<Animated> temp) {  //aggiunge i bottoni partendo avendo in input Tile ed Anim
        Type[] t = Type.values();
        if (temp != null) {
            for (int i = 0; i < temp.size(); i++) {
                for (int j = 0; j < t.length; j++) {
                    if (temp.get(i).getType() == t[j]) {
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
                                s.setTerrain(false);
                                s.getButton().addActionListener(this);
                                elenco.add(s);
                                add(elenco.get(elenco.size() - 1).getButton());
                            }
                        }
                        break;
                    }
                    //}
                }

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
        System.out.println("heightRefact: "+height);
        ed.getPanelScroll().setPreferredSize(new Dimension(d.width, d.height + adaptHeight(50)));
    }

    public void pulisci() {
        for (int i = 0; i < elenco.size(); i++) {
            remove(elenco.get(i).getButton());
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
    public Component add(Component c){
        super.add(c, gbc);
        gbc.gridx^=1;
        gbc.gridy+=(gbc.gridx^1);
        return null;
    }
    public int adaptHeight(int val){
        return (int) ((double)val / 900 * HEIGHT);
    }
}
