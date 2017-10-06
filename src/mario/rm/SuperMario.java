package mario.rm;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.management.ManagementFactory;
import mario.MainComponent;
import mario.rm.Menu.opzioni.Menu;
import mario.rm.camera.Camera;
import mario.rm.handler.Handler;
import mario.rm.input.Loader;
import mario.rm.input.Movement;
import mario.rm.sprite.Player;

/**
 *
 * @author LENOVO
 */
public final class SuperMario extends Canvas implements Runnable {  //1200 900, 50 50.

    public static int WIDTH;  //LARGHEZZA
    public static int HEIGHT;  //ALTEZZA
    private final static String TITOLO = "SUPER LUIGI"; //TITOLO DELLA FINESTRA

    private boolean gameLoop;

    private GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0]; //VARIABILE PER OTTENERE LA LARGHEZZA E ALTEZZA MASSIMA DEL FRAME

    public static Handler handler;  //CONTIENE E GESTISCE PLAYER E TILES
    private Camera cam; //NUOVA TELECAMERA

    public static int standardWidth; //TEMPORANEA PER WIDTH DEGLI SPRITE
    public static int standardHeight;    //TEMPORANEA PER HEIGHT DEGLI SPRITE

    private BufferedImage bg;    //SFONDO
    private BufferedImage coin; //FOTO DELLA MONETA
    //private BufferedImage life;

    private Image load = null;  //GIF PER IL DISEGNO DELLA GIF DI CARICAMENTO

    private int FPS;

    private static boolean menu;

    private String next;

    private boolean isLoad = false;  //INDICA SE MANDARE IN OUTPUT LA GIF DI LOADING

    private static Frame frame;

    private static boolean video = false;

    private Movement movement;

    //
    public SuperMario() {
        System.out.println("" + ManagementFactory.getRuntimeMXBean().getName());
        System.out.println("1)INIZIO");

        gameLoop = true;

        handler = new Handler(this);    //NUOVI ARRAY DI SPRITE (TILE, E PLAYER) QUESTI SARANNO DEFINITI IN BASE AD UN FILE

        finestra(); //INIZIALIZZA LA FINESTRA
        /*try {
            load = (new ImageIcon(ImageIO.read(MainComponent.class.getResourceAsStream("rm/tenor.gif")))).getImage();   //GIF PER IL LOADING
        } catch (IOException ex) {
            Logger.getLogger(SuperMario.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        coin = new Loader().LoadImage("mario/res/Immagini/tiles.png");
        coin = coin.getSubimage(64 * 7 + 8, 10, 64, 64);

        standardWidth = adaptWidth(50); //CALCOLO LA LARGHEZZA STANDARD DEGLI SPRITE
        standardHeight = adaptHeight(50);   //CALCOLO LA ALTEZZA STANDARD DEGLI SPRITE

        menu = false;

        new Thread(this).start();

        createLV();

    }

    private void finestra() {  //INIZIALIZZA LA FINESTRA
        System.out.println("2)CREO LA FINESTRA");

        frame = new Frame(TITOLO);  // NUOVA FINESRTRA
        device.setFullScreenWindow(frame);
        WIDTH = device.getFullScreenWindow().getWidth();    //OTTENGO LA LARGHEZZA MASSIMA DELLA FINESTRA
        HEIGHT = device.getFullScreenWindow().getHeight();  //OTTENGO LA ALTEZZA MASSIMA DELLA FINESTRA

        frame.inizializza(WIDTH, HEIGHT);

        addKeyListener((movement = new Movement(adaptWidth((int) 5.0), adaptHeight(10.0), handler, this))); //AGGIUNGE UN KEY LISTENER DALLA CLASSE MOVEMENT

        frame.add(this);

        System.out.println("WIDTH: " + WIDTH + " HEIGHT: " + HEIGHT);
    }

    public void createLV() {
        isLoad = false; //SETTO A IN LOADING

        System.out.println("3)ISTANZIO PLAYER, BACKGROUND E LIVELLO");

        if (!handler.newLevel()) {
            System.out.println("asddas");
            stopGame();
            return;
        }
        next = handler.getLevel();
        next = next.substring(0, next.lastIndexOf(".")) + ".png";
        System.out.println("Bg image: " + next);
        bg = new Loader().LoadImage(next); //CARICO IN MEMORIA LO SFONDO

        cam = new Camera(handler.getPlayer().get(0));    //SERVE PER ACCENTRARE SUL PLAYER LA TELECAMERA

        isLoad = true;  //GLI DICO DI AVER CARICATO IN MEMORIA IL LIVELLO
    }

    public void render() {
        //long time = System.currentTimeMillis();
        try{
        if (!menu) {
            BufferStrategy strategy = getBufferStrategy(); //MI INDICA QUANTI BUFFER CI SONO
            if (strategy == null) {   //SE NON CI SONO BUFFER
                createBufferStrategy(3);    //CREA 3 BUFFER
                System.out.println("9)HO CREATO I BUFFER");
                return;
            }
            Graphics g = strategy.getDrawGraphics();    //INIZIALIZZO LA VARIABILE CHE DISEGNA

            g.clearRect(0, 0, WIDTH, HEIGHT);   //PULISCE LO SCHERMO

            if (isLoad) {

                g.translate(cam.getX(), cam.getY());    //SPOSTA IL CENTRAMENTO DELLA FINESTRA

                g.drawImage(bg, 0, 0, bg.getWidth() / 64 * standardWidth, bg.getHeight() / 64 * standardHeight, null);
                handler.render(g);  //DISEGNA TUTTO

                g.drawImage(coin, -cam.getX(), -cam.getY() + (standardHeight / 2), standardWidth, standardHeight, null);
                g.drawString("" + Player.MONETE, -cam.getX() + (standardWidth / 2) - adaptWidth(3), -cam.getY() + adaptHeight(4) + standardHeight); //TIPO COSI, MA DA RIPOSIZIONARE, CENTRARE E OVVIAMENTE MODIFICARE FONT
                g.drawString("PUNTEGGIO: " + Player.PUNTEGGIO, -cam.getX() + adaptWidth(150), -cam.getY() + standardHeight); //TIPO COSI, MA DA RIPOSIZIONARE, CENTRARE E OVVIAMENTE MODIFICARE FONT
                g.drawString("FPS: " + FPS, -cam.getX() + WIDTH - adaptWidth(150), -cam.getY() + standardHeight); //TIPO COSI, MA DA RIPOSIZIONARE, CENTRARE E OVVIAMENTE MODIFICARE FONT
            } else {
                g.translate(0, 0);  //AZZERO LE COORDINATE I DISEGNO DEL FRAME
                //g.drawImage(load, 0, 0, WIDTH, HEIGHT, null); //DISEGNO LA GIF CHE INDICA IL CARICAMENTO
            }
            //g.dispose();
            strategy.show();
        }
        }catch(NullPointerException e){System.out.println("errore");}
        //System.out.println("time: "+(System.currentTimeMillis()-time));
    }

    public void tick() {
        if (isLoad && !menu) {
            handler.tick(); //SI OCCUPA DEL MOVIMENTO DI TUTTI GLI SPRITE E DELLE VARIE OPZIONI DI OGNI PLAYER, TILE ED ENEMIES
            cam.tick(handler.getPlayer().get(0));    //AGGIORNA IL CENTRO DEL FRAME
        }
    }

    @Override
    public void run() { //THREAD
        System.out.println("8)THREAD MAIN");

        long lastTime = System.nanoTime();
        final double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int ticks = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();

        while (gameLoop) {
            if (!menu) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                if (delta >= 1) {
                    tick();
                    ticks++;
                    delta--;
                }
                render();
                frames++;

                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    //System.out.println("FPS : " + frames + " Ticks : " + ticks);
                    FPS = frames;
                    frames = 0;
                    ticks = 0;
                }
            }
        }
    }

    public void addOption() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs != null){
            bs.dispose();
        }
        
        menu = true;

        handler.getSound().stop();
        
        frame.remove(this);
        frame.removeKeyListener(movement);

        frame.add(new Menu(this));

        frame.revalidate();
        frame.repaint();
        System.out.println("asdasdas");
    }

    public void removeOption() {
        /*    frame.remove(option);

        handler.getSound().loop();

        frame.setVisible(true);
        option.setVisible(false);

        option.dispose();

        menu = false;
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);*/

        frame.removeAll();
        
        handler.getSound().loop();
        frame.addKeyListener(movement);
        frame.add(this);
        
        menu = false;
        
        frame.revalidate();
        frame.repaint();
        revalidate();
        repaint();
        new Thread(this).start();
    }

    /*public static void video(){
        video = true;
        mario.removeKeyListener(movement);
    }*/
    public static int adaptWidth(int val) { //RIADATTO LA LARGHEZZA DELLE IMMAGINI IN BASE ALLA GRANDEZZA DELLO SCHERMO
        return (int) ((double) val / 1200 * WIDTH);
    }

    public static int adaptHeight(int val) {    //RIADATTA LA ALTEZZA DELLE IMMAGINI IN BASE ALLA GRANDEZZA DELLO SCHERMO
        return (int) ((double) val / 900 * HEIGHT);
    }

    public static double adaptWidth(double val) {   //UGUALE AD INT
        return ((double) val / 1200 * WIDTH);
    }

    public static double adaptHeight(double val) {  //UGUALE AD INT
        return ((double) val / 900 * HEIGHT);
    }

    public void stopGame() {
        gameLoop = false;
        frame.dispose();
        new MainComponent();
    }

}
