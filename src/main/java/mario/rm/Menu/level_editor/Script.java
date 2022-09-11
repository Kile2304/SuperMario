package mario.rm.Menu.level_editor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import mario.rm.Menu.Griglia;
import mario.rm.utility.Punto;

/**
 *
 * @author LENOVO
 */
public class Script extends JFrame implements ItemListener {

    private JComboBox typeU;
    private JComboBox typeT;
    private JTextField number;

    private ArrayList<JTextField> list;

    private Griglia g;

    private String prew;

    private JPanel tile;

    public Script(Griglia g) {
        super("Script");
        this.g = g;

        typeU = new JComboBox(new String[]{"COIN", "MUSHROOM", "LIFE"});
        typeT = new JComboBox(new String[]{"normal", "collision", "DISAPPEAR", "GravityTile"});

        list = new ArrayList<>();
        JPanel unlockable = setUnlockable();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Unlockable", null, unlockable,
                "Unlockable");
        tile = new JPanel();
        setTileMoveNormal();

        tabbedPane.addTab("Tile", null, tile,
                "Tile Movement");

        add(tabbedPane);

        pack();

        setLocationRelativeTo(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setVisible(true);

    }

    public JPanel setUnlockable() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(3, 2));

        p.add(new JLabel("Indica il tipo di sblocco:"));
        p.add(typeU);
        p.add(new JLabel("Indica il numero di sblocchi:"));
        p.add((number = new JTextField("" + 1)));
        JButton b = new JButton("conferma");
        b.addActionListener((ActionEvent e) -> {
            int val;
            try {
                val = Integer.parseInt(number.getText());
            } catch (Exception ex) {
                System.out.println("Valori non validi");
                return;
            }

            //g.getPreview().getMappa()[col][row].setScript("◄" + typeU.getSelectedItem() + "►" + "←" + val + "→");//17 16 26 27 ascii
            String nuova = "U " + typeU.getSelectedItem() + " " + val;
            addScript(ordina(setNewer(nuova)));

        });
        p.add(b);

        return p;
    }

    private void addScript(String nuova) {
        for (Iterator<Punto> it = Selezione.punto.iterator(); it.hasNext();) {
            Punto punto = it.next();
            g.getPreview().getMappa()[punto.getX()][punto.getY()].setScript(nuova);
        }
        Selezione.punto.clear();
        dispose();
    }

    public String ordina(String[] split) {
        ArrayList<String> list = new ArrayList<>();
        String ordered = "";
        if (split.length == 2) {
            System.out.println("split: " + split[0] + " " + split[1]);
            list.add(split[0]);
            list.add(split[1]);
            if (split[0].split(" ").equals("M")) {
                String remove = list.remove(0);
                list.add(remove);
            }
            ordered = list.get(0) + "/" + list.get(1);
        } else {
            ordered = split[0] + "/";
        }
        System.out.println("nuova: " + ordered);
        return ordered;
    }

    private String[] setNewer(String nuova) {
        String tipo = nuova.split(" ")[0];
        String[] split = null;
        try {
            Punto p = Selezione.punto.get(0);
            String old = g.getPreview().getMappa()[p.getX()][p.getY()].getScript();
            old.substring(old.lastIndexOf("/"));
            split = old.split("/");
            //System.out.println("vecchia: "+g.getPreview().getMappa()[col][row].getScript());
            boolean find = false;

            for (int i = 0; i < split.length; i++) {
                if (split[i].split(" ")[0].equals(tipo)) {
                    split[i] = nuova;
                    find = true;
                    break;
                }
            }
            if (!find) {
                split = new String[]{split[0], nuova};
            }
        } catch (StringIndexOutOfBoundsException e) {
            split = new String[]{nuova};
        }
        return split;
    }

    public void setTileMoveNormal() {
        tile.setLayout(new GridLayout(8, 2));

        tile.add(new JLabel("Indica il tipo di movimento:"));
        tile.add(typeT);
        typeT.addItemListener(this);
        prew = (String) typeT.getSelectedItem();
        final String[] elenco = new String[]{"maxX", "maxY", "velX", "velY", "incrX", "incrY"};
        for (int i = 0; i < elenco.length; i++) {
            tile.add(new JLabel(elenco[i]));
            list.add(new JTextField());
            tile.add(list.get(i));
        }
        JButton b = new JButton("conferma");
        b.addActionListener((ActionEvent e) -> {
            //g.getPreview().getMappa()[col][row].setScript("◄" + typeT.getSelectedItem() + "►");//17 16 ascii

            String nuova = "M " + typeT.getSelectedItem();
            for (int i = 0; i < list.size(); i++) {
                nuova += list.get(i).getText() + (i < list.size() - 1
                        ? " "
                        : "");
            }
            addScript(ordina(setNewer(nuova)));
        });
        tile.add(b);
    }

    public void setTileMoveConfirm() {
        tile.setLayout(new GridLayout(2, 2));

        tile.add(new JLabel("Indica il tipo di movimento:"));
        prew = (String) typeT.getSelectedItem();
        tile.add(typeT);
        JButton b = new JButton("conferma");
        b.addActionListener((ActionEvent e) -> {
            String nuova = "M " + typeT.getSelectedItem();
            addScript(ordina(setNewer(nuova)));
        });
        tile.add(b);
    }

    public void setDisappear() {
        tile.setLayout(new GridLayout(4, 2));

        tile.add(new JLabel("Indica il tipo di movimento:"));
        prew = (String) typeT.getSelectedItem();
        tile.add(typeT);
        final String[] elenco = new String[]{"time 1", "time 2"};
        for (int i = 0; i < elenco.length; i++) {
            tile.add(new JLabel(elenco[i]));
            list.add(new JTextField());
            tile.add(list.get(i));
        }
        JButton b = new JButton("conferma");
        b.addActionListener((ActionEvent e) -> {
            String nuova = "M " + typeT.getSelectedItem().toString().toLowerCase() + " ";
            for (int i = 0; i < list.size(); i++) {
                nuova += list.get(i).getText() + (i < list.size() - 1
                        ? " "
                        : "");
            }
            addScript(ordina(setNewer(nuova)));
        });
        tile.add(b);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            Object item = e.getItem();
            System.out.println("" + item);
            if (prew.equals((String) item)) {
                return;
            }
            list.clear();
            tile.removeAll();
            switch ((String) item) {
                case "normal":
                    setTileMoveNormal();
                    break;
                case "collision":
                    setTileMoveConfirm();
                    break;
                case "DISAPPEAR":
                    setDisappear();
                    break;
                case "Gravity Tile":
                    setTileMoveConfirm();
            }
            repaint();
            prew = (String) item;
        }
    }

}
