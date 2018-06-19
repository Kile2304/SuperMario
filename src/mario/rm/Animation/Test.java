package mario.rm.Animation;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import mario.MainComponent;
import mario.rm.Menu.sprite_estractor.input.SpriteEstractor;
import mario.rm.identifier.Direction;
import mario.rm.identifier.Move;
import mario.rm.identifier.TilePart;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class Test extends Canvas implements Runnable, ActionListener {

    //private Animazione an;
    private Anim anim;
    private Tile tile;
    int index;

    Thread t;

    JFrame frame;

    JFrame sp;
    
    private int sleep = 150;

    public Test(JFrame sp) throws FileNotFoundException, IOException, ClassNotFoundException {
        frame = new JFrame("");
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        this.sp = sp;

        frame.setSize(d.width / 2, d.height / 2);

        JMenuBar mb = new JMenuBar();

        JMenu file = new JMenu("file");

        JMenuItem load = new JMenuItem("Load");
        load.addActionListener(this);

        file.add(load);

        JMenuItem indietro = new JMenuItem("indietro");
        indietro.addActionListener((ActionEvent e) -> {
            frame.dispose();
            if (t != null) {
                try {
                    t.join();
                } catch (InterruptedException ex) {
                    Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
                }
            }
            sp.setVisible(true);
        });
        file.add(indietro);

        mb.add(file);

        frame.setJMenuBar(mb);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(this);

        frame.setVisible(true);
        boolean toLoad = load();
        if (toLoad) {

            //t = new Thread(this);
            //t.start();
        }

    }

    public boolean load() throws FileNotFoundException, IOException, ClassNotFoundException {
        Cut cut = null;
        //JFileChooser c = new JFileChooser(new File("src/mario/res/Animazioni").getAbsolutePath());
        JFileChooser c = new JFileChooser(MainComponent.filePath + "/Luigi/Animation");

        int valid = c.showOpenDialog(null);

        if (valid == JFileChooser.APPROVE_OPTION) {
            FileInputStream fis = new FileInputStream(c.getSelectedFile().getAbsolutePath());
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object ob = ois.readObject();
            if (ob instanceof Anim) {    //DA PERFEZIONARE
                anim = ((Anim) ob);
                opzioneA();
            } else if (ob instanceof Tile) {
                tile = (Tile) ob;
                opzioneB();
            } else {
                //System.out.println("Altro elemento");
                Log.append("Il file scelto non fa parte dei file supportati");
                return false;
            }
            //an = new Animazione(cut);
        } else {
            //System.exit(0);
            frame.dispose();
            if (t != null) {
                try {
                    t.join();
                } catch (InterruptedException ex) {
                    Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
                }
            }
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            new Test(null);
        } catch (FileNotFoundException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        } catch (IOException | ClassNotFoundException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.clearRect(0, 0, 1920, 1080);

        g.fillRect(0, 0, getWidth(), getHeight());

        //g.drawImage(an.nextNormal(), 100, 100, 100, 100, null);
        if (anim != null) {
            BufferedImage temp = anim.getImage((Move) box.getSelectedItem(), Direction.valueOf((String) direction.getSelectedItem()), (Move) box.getSelectedItem(), Direction.valueOf((String) direction.getSelectedItem()));
            g.drawImage(temp, 0, 0, 64, 64, this);
        } else if (tile != null) {
            //g.drawImage(tile.getImage(TilePart.UPLEFT)[0], 0, 0, 64, 64, this);
            BufferedImage[] img = tile.getImage(TilePart.UPLEFT);
            if (img != null) {
                g.drawImage(img[index], 0, 0, 64, 64, null);
                if (index < img.length - 1) {
                    index++;
                } else {
                    index = 0;
                }
            } else {
                //System.out.println("WHY?");
            }
        } else {
            Log.append("Errore", DefaultFont.ERROR);
        }

        bs.show();
    }

    @Override
    public void run() {
        while (true) {
            render();
            try {
                sleep(sleep);
            } catch (InterruptedException ex) {
                Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            load();
        } catch (IOException | ClassNotFoundException ex) {
            Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
        }
    }

    JComboBox box;
    JComboBox direction;

    private void opzioneB() {
        /*box = new JComboBox(Move.values());
        frame.add(box);
        direction = new JComboBox(new String[]{"LEFT", "RIGHT"});
        frame.add(direction);
        JButton avanti = new JButton("AVANTI");
        avanti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                avanti();
            }

        });*/
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    private void avanti() {
        frame.remove(box);
        frame.remove(direction);
        frame.remove(avanti);
        revalidate();

//        this.setVisible(true);
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    JButton avanti;

    private void opzioneA() {
        frame.setLayout(new GridLayout(1, 3));
        //this.setVisible(false);
        box = new JComboBox(Move.values());
        frame.add(box);
        direction = new JComboBox(new String[]{"LEFT", "RIGHT"});
        frame.add(direction);
        avanti = new JButton("AVANTI");
        avanti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                avanti();
            }

        });
        frame.add(avanti);
        repaint();
        revalidate();
    }

}
