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
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;
import mario.rm.utility.joystick.ControllerListener;

/**
 *
 * @author LENOVO
 */
public final class SuperMario extends Canvas implements Runnable {  //1200 900, 50 50.

    public static int WIDTH;  //LARGHEZZA
    public static int HEIGHT;  //ALTEZZA
    private final static String TITOLO = "SUPER LUIGI"; //TITOLO DELLA FINESTRA

    private static boolean gameLoop;

    private GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0]; //VARIABILE PER OTTENERE LA LARGHEZZA E ALTEZZA MASSIMA DEL FRAME

    public static Handler handler;  //CONTIENE E GESTISCE PLAYER E TILES
    private Camera cam; //NUOVA TELECAMERA

    public static int standardWidth; //TEMPORANEA PER WIDTH DEGLI SPRITE
    public static int standardHeight;    //TEMPORANEA PER HEIGHT DEGLI SPRITE

    private BufferedImage bg;    //SFONDO
    private static final BufferedImage coin = (Loader.LoadImage("mario/res/Immagini/tiles.png").getSubimage(64 * 7 + 8, 10, 64, 64)); //FOTO DELLA MONETA ritagliata
    //private BufferedImage life;

    private Image load = null;  //GIF PER IL DISEGNO DELLA GIF DI CARICAMENTO

    private int FPS;

    private static boolean menu;

    private String next;

    private boolean isLoad = false;  //INDICA SE MANDARE IN OUTPUT LA GIF DI LOADING

    private static Frame frame;

    private static boolean video = false;

    private ControllerListener movement;

    private Menu option;

    //private Thread t;
    //
    public SuperMario() {
        Log.append("" + ManagementFactory.getRuntimeMXBean().getName(), DefaultFont.INFORMATION);
        Log.append("1)INIZIO", DefaultFont.INFORMATION);

        handler = new Handler(this);    //NUOVI ARRAY DI SPRITE (TILE, E PLAYER) QUESTI SARANNO DEFINITI IN BASE AD UN FILE

        finestra(); //INIZIALIZZA LA FINESTRA
        /*try {
            load = (new ImageIcon(ImageIO.read(MainComponent.class.getResourceAsStream("rm/tenor.gif")))).getImage();   //GIF PER IL LOADING
        } catch (IOException ex) {
            Logger.getLogger(SuperMario.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        standardWidth = adaptWidth(50); //CALCOLO LA LARGHEZZA STANDARD DEGLI SPRITE
        standardHeight = adaptHeight(50);   //CALCOLO LA ALTEZZA STANDARD DEGLI SPRITE

        start();

        createLV(false);

    }

    public void start() {
        if (gameLoop) {
            return;
        }
        gameLoop = true;
        menu = false;
        /*t = new Thread(this);
        t.setName("rendering");
        t.start();*/
        new Thread(this).start();
    }

    public void stop() {
        if (!gameLoop) {
            return;
        }
        gameLoop = false;
    }

    private void finestra() {  //INIZIALIZZA LA FINESTRA
        Log.append("2)CREO LA FINESTRA", DefaultFont.INFORMATION);

        frame = new Frame(TITOLO);  // NUOVA FINESRTRA
        //device.setFullScreenWindow(frame);
        //WIDTH = device.getFullScreenWindow().getWidth() / 2;    //OTTENGO LA LARGHEZZA MASSIMA DELLA FINESTRA
        //HEIGHT = device.getFullScreenWindow().getHeight() / 2;  //OTTENGO LA ALTEZZA MASSIMA DELLA FINESTRA
        WIDTH = 1920 / 2;    //OTTENGO LA LARGHEZZA MASSIMA DELLA FINESTRA
        HEIGHT = 1080 / 2;  //OTTENGO LA ALTEZZA MASSIMA DELLA FINESTRA

        frame.inizializza(WIDTH, HEIGHT);

        addKeyListener((movement = new ControllerListener(adaptWidth((int) 5.0), adaptHeight(10.0), handler, this))); //AGGIUNGE UN KEY LISTENER DALLA CLASSE MOVEMENT

        frame.add(this);

        Log.append("WIDTH: " + WIDTH + " HEIGHT: " + HEIGHT, DefaultFont.INFORMATION);
    }

    public void createLV(boolean current) {
        isLoad = false; //SETTO A IN LOADING

        Log.append("3)ISTANZIO PLAYER, BACKGROUND E LIVELLO", DefaultFont.INFORMATION);

        if (!handler.newLevel(current)) {
            stopGame();
            return;
        }
        next = handler.getLevel();
        next = next.substring(0, next.lastIndexOf(".")) + ".png";
        Log.append("Bg image: " + next, DefaultFont.INFORMATION);
        bg = Loader.LoadImage(next); //CARICO IN MEMORIA LO SFONDO

        cam = new Camera(handler.getPlayer().get(0));    //SERVE PER ACCENTRARE SUL PLAYER LA TELECAMERA

        isLoad = true;  //GLI DICO DI AVER CARICATO IN MEMORIA IL LIVELLO
    }

    public void render() {
        //long time = System.currentTimeMillis();
        try {
            if (!menu) {
                BufferStrategy strategy = getBufferStrategy(); //MI INDICA QUANTI BUFFER CI SONO
                if (strategy == null) {   //SE NON CI SONO BUFFER
                    createBufferStrategy(3);    //CREA 3 BUFFER
                    Log.append("9)HO CREATO I BUFFER", DefaultFont.INFORMATION);
                    return;
                }
                Graphics g = strategy.getDrawGraphics();    //INIZIALIZZO LA VARIABILE CHE DISEGNA

                g.clearRect(0, 0, WIDTH, HEIGHT);   //PULISCE LO SCHERMO

                if (isLoad) {

                    g.translate(cam.getX(), cam.getY());    //SPOSTA IL CENTRAMENTO DELLA FINESTRA

                    //g.drawImage(bg, 0, 0, bg.getWidth() / 64 * standardWidth, bg.getHeight() / 64 * standardHeight, null);
                    int dstx1 = handler.getPlayer().get(0).getX() - WIDTH / 2;
                    int dsty1 = handler.getPlayer().get(0).getY() - HEIGHT / 2 + standardHeight;
                    int dstx2 = WIDTH + dstx1;
                    int dsty2 = HEIGHT + dsty1;

                    int srcx1 = dstx1 * 64 / standardWidth;
                    int srcy1 = dsty1 * 64 / standardHeight;
                    int srcx2 = srcx1 + WIDTH * 64 / standardWidth;
                    int srcy2 = srcy1 + HEIGHT * 64 / standardHeight;

                    g.drawImage(bg, dstx1, dsty1, dstx2, dsty2, srcx1, srcy1, srcx2, srcy2, frame);
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
        } catch (NullPointerException e) {
            Log.append("errore", DefaultFont.ERROR);
        }
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
        Log.append("8)THREAD MAIN", DefaultFont.INFORMATION);

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
                //System.out.println(""+delta);
                if(delta > 1){
                    delta = 1.0f;
                }
                //delta = Math.min(delta, 1 / 60);
                lastTime = now;
                if (delta >= 1) {
                    //if(delta > 0.00f)
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

    public synchronized void addOption() {
        if (!menu) {
            this.setVisible(false);

            menu = true;

            handler.getSound().stop();

            frame.add((option = new Menu(this)));
            frame.removeKeyListener(movement);

            frame.revalidate();
            frame.repaint();
        } else {
            removeOption();
        }
    }

    public void removeOption() {
        if (menu) {
            frame.remove(option);

            this.setVisible(true);

            handler.getSound().loop();
            frame.addKeyListener(movement);

            menu = false;

            frame.revalidate();
            frame.repaint();
            revalidate();
            repaint();
            //t.notify();
            new Thread(this).start();
        }
    }

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

    public static void stopGame() {
        gameLoop = false;
        frame.dispose();
        new MainComponent();
    }

    public Frame getFrame() {
        return frame;
    }

    public boolean getMenu(){
        return menu;
    }
    
}
