package mario.rm.Animation;

import java.awt.Canvas;
import java.awt.Graphics;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import mario.rm.identifier.Direction;
import mario.rm.identifier.Move;
import mario.rm.identifier.TilePart;

/**
 *
 * @author LENOVO
 */
public class Test extends Canvas implements Runnable {

    //private Animazione an;
    private Anim anim;
    private Tile tile;
    int index;

    public Test() throws FileNotFoundException, IOException, ClassNotFoundException {
        JFrame frame = new JFrame("");
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(this);
        Cut cut = null;
        JFileChooser c = new JFileChooser(new File("src/mario/res/Animazioni").getAbsolutePath());

        int valid = c.showOpenDialog(null);

        if (valid == JFileChooser.APPROVE_OPTION) {
            FileInputStream fis = new FileInputStream(c.getSelectedFile().getAbsolutePath());
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object ob = ois.readObject();
            if (ob instanceof Anim) {    //DA PERFEZIONARE
                anim = ((Anim) ob);
            } else if(ob instanceof Tile){
                tile = (Tile) ob;
            }else{
                System.out.println("Altro elemento");
            }
            //an = new Animazione(cut);
        } else {
            System.exit(0);
        }

        frame.setVisible(true);

        new Thread(this).start();

    }

    public static void main(String[] args) {
        try {
            new Test();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
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

        //g.drawImage(an.nextNormal(), 100, 100, 100, 100, null);
        if(anim != null){
            g.drawImage(anim.getImage(Move.WALK, Direction.RIGHT), 0, 0, 64, 64, this);
        }else if(tile != null){
            //g.drawImage(tile.getImage(TilePart.UPLEFT)[0], 0, 0, 64, 64, this);
                BufferedImage[] img = tile.getImage(TilePart.UPLEFT);
                if(img != null){
                    g.drawImage(img[index], 0, 0, 64, 64, null);
                    if(index < img.length - 1){
                        index++;
                    } else {
                        index = 0;
                    }
                }else{
                    //System.out.println("WHY?");
                }
        }else{
            System.out.println("Errore");
        }

        bs.show();
    }

    @Override
    public void run() {
        while (true) {
            render();
            try {
                sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
