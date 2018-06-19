package mario.rm;

import Connessione.Query;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import static java.lang.Thread.sleep;
import java.lang.management.ManagementFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import mario.MainComponent;
import mario.rm.Menu.opzioni.Impostazioni;
import mario.rm.Menu.opzioni.Menu;
import mario.rm.camera.Camera;
import mario.rm.handler.Handler;
import mario.rm.input.Loader;
import mario.rm.sprite.Player;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;
import mario.rm.utility.joystick.ControllerListener;
import static mario.rm.handler.HudHandler.*;
import mario.rm.input.Sound;
import mario.rm.utility.ThreadList;
import mario.rm.utility.Utility;

/**
 *
 * @author LENOVO
 */
public final class SuperMario extends Canvas implements Runnable {  //1200 900, 50 50.

    public static int WIDTH;  //LARGHEZZA
    public static int HEIGHT;  //ALTEZZA
    private final static String TITOLO = "SUPER LUIGI"; //TITOLO DELLA FINESTRA

    private static boolean gameLoop;

    //private GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0]; //VARIABILE PER OTTENERE LA LARGHEZZA E ALTEZZA MASSIMA DEL FRAME
    public static Handler handler;  //CONTIENE E GESTISCE PLAYER E TILES
    private Camera cam; //NUOVA TELECAMERA

    public static int standardWidth; //TEMPORANEA PER WIDTH DEGLI SPRITE
    public static int standardHeight;    //TEMPORANEA PER HEIGHT DEGLI SPRITE

    private BufferedImage coin; //FOTO DELLA MONETA ritagliata
    private BufferedImage face; //FOTO DELLA FACCE PER VEDERE IL NUMEERO DI VITE

    private BufferedImage bg; //SFONDO
    //private BufferedImage life;

    private Image load = null;  //GIF PER IL DISEGNO DELLA GIF DI CARICAMENTO

    private int FPS;

    private String next;

    private boolean isLoad = false;  //INDICA SE MANDARE IN OUTPUT LA GIF DI LOADING

    public static Frame frame;

    public static ControllerListener movement;

    private Impostazioni option;

    public static int playerNumber;

    private long time;
    public static int timeShow;

    public static boolean[] gameOver;
    private boolean allDie;

    private boolean loading;
    private int delay;
    private boolean wait;

    //
    public SuperMario(int player) {
        Log.append("" + ManagementFactory.getRuntimeMXBean().getName(), DefaultFont.INFORMATION);
        Log.append("Il numero di player e' " + player, DefaultFont.INFORMATION);
        Log.append("1)INIZIO", DefaultFont.INFORMATION);
        
        coin = (Loader.LoadImage("Immagini/tiles.png").getSubimage(64 * 7 + 8, 10, 64, 64));
        face = (Loader.LoadImage("Immagini/face.png").getSubimage(31, 449, 30, 37));
        bg = (Loader.LoadImage("Immagini/bg.png"));
        

        loading = true;
        delay = 0;
        playerNumber = player;

        gameOver = new boolean[player];
        for (int i = 0; i < gameOver.length; i++) {
            gameOver[i] = false;
        }
        allDie = false;

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
        Thread loop = new Thread(this);
        loop.setName(ThreadList.SUPERMARIO.threadName);
        loop.setPriority(Thread.NORM_PRIORITY);
        loop.start();
    }

    public void stop() {
        if (!gameLoop) {
            return;
        }
        gameLoop = false;
        try {
            Thread tSu = Utility.getThread(ThreadList.SUPERMARIO.threadName);
            if (tSu != null) {
                tSu.join();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(SuperMario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void finestra() {  //INIZIALIZZA LA FINESTRA
        Log.append("2)CREO LA FINESTRA", DefaultFont.INFORMATION);

        frame = new Frame(TITOLO);  // NUOVA FINESRTRA
        //WIDTH = device.getFullScreenWindow().getWidth() / 2;    //OTTENGO LA LARGHEZZA MASSIMA DELLA FINESTRA
        //HEIGHT = device.getFullScreenWindow().getHeight() / 2;  //OTTENGO LA ALTEZZA MASSIMA DELLA FINESTRA
        final boolean fullscreen = Boolean.parseBoolean(MainComponent.settings.getValue("fullscreen"));
        int scale = Integer.parseInt(MainComponent.settings.getValue("scale"));
        if (fullscreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            scale = 1;
        }
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        WIDTH = dim.width / scale;    //OTTENGO LA LARGHEZZA MASSIMA DELLA FINESTRA
        HEIGHT = dim.height / scale;  //OTTENGO LA ALTEZZA MASSIMA DELLA FINESTRA

        frame.inizializza(WIDTH, HEIGHT);

        addKeyListener((movement = new ControllerListener(adaptWidth((int) 5.0), adaptHeight(10.0), handler, this))); //AGGIUNGE UN KEY LISTENER DALLA CLASSE MOVEMENT

        frame.add(this);

        Log.append("WIDTH: " + WIDTH + " HEIGHT: " + HEIGHT, DefaultFont.INFORMATION);
    }

    public void createLV(boolean current) {
        isLoad = false; //SETTO A IN LOADING
        wait = false;
        timeShow = 346;

        Log.append("3)ISTANZIO PLAYER, BACKGROUND E LIVELLO", DefaultFont.INFORMATION);
        if (!handler.newLevel(current)) {
            stopGame();
            return;
        }
        next = handler.getLevel();
        next = next.substring(0, next.lastIndexOf(".")) + ".png";
        Log.append("Bg image: " + next, DefaultFont.INFORMATION);
        //bg = Loader.LoadImage(next); //CARICO IN MEMORIA LO SFONDO

        cam = new Camera(handler.getPlayer().get(0));    //SERVE PER ACCENTRARE SUL PLAYER LA TELECAMERA

        isLoad = true;  //GLI DICO DI AVER CARICATO IN MEMORIA IL LIVELLO
        loading = false;
    }

    public void render() {
        //long time = System.currentTimeMillis();
        try {
            BufferStrategy strategy = getBufferStrategy(); //MI INDICA QUANTI BUFFER CI SONO
            if (strategy == null) {   //SE NON CI SONO BUFFER
                createBufferStrategy(4);    //CREA 3 BUFFER
                Log.append("9)HO CREATO I BUFFER", DefaultFont.INFORMATION);
                return;
            }
            Graphics g = strategy.getDrawGraphics();    //INIZIALIZZO LA VARIABILE CHE DISEGNA

            g.clearRect(0, 0, WIDTH, HEIGHT);   //PULISCE LO SCHERMO

            if (isLoad && !allDie && !loading && !wait) {

                g.translate(cam.getX(), cam.getY());    //SPOSTA IL CENTRAMENTO DELLA FINESTRA

                g.drawImage(bg, -cam.getX(), -cam.getY(), WIDTH, HEIGHT, null);

                //g.drawImage(bg, 0, 0, bg.getWidth() / 64 * standardWidth, bg.getHeight() / 64 * standardHeight, null);
                /*int pix = 64;
                    int dstx1 = handler.getPlayer().get(0).getX() - WIDTH / 2;
                    int dsty1 = handler.getPlayer().get(0).getY() - HEIGHT / 2 + standardHeight;
                    int dstx2 = WIDTH + dstx1;
                    int dsty2 = HEIGHT + dsty1;

                    int srcx1 = dstx1 * pix / standardWidth;
                    int srcy1 = dsty1 * pix / standardHeight;
                    int srcx2 = srcx1 + WIDTH * pix / standardWidth;
                    int srcy2 = srcy1 + HEIGHT * pix / standardHeight;*/
                //g.drawImage(bgB, -cam.getX(), -cam.getY(), WIDTH, HEIGHT, frame);
                //g.drawImage(bg, dstx1, dsty1, dstx2, dsty2, srcx1, srcy1, srcx2, srcy2, frame);
                handler.render(g);  //DISEGNA TUTTO

                BufferedImage[] toHud = toHud(Player.PUNTEGGIO);
                for (int i = 0; i < toHud.length; i++) {
                    g.drawImage(toHud[i], -cam.getX() - adaptWidth(200 + i * 16) + WIDTH, -cam.getY() + standardHeight / 4 * 3, adaptWidth(10), adaptHeight(25), null); //TIPO COSI, MA DA RIPOSIZIONARE, CENTRARE E OVVIAMENTE MODIFICARE FONT
                }

                toHud = toHud(timeShow);    //tempo rimanente
                for (int i = 0; i < toHud.length; i++) {
                    g.drawImage(toHud[i],
                            -cam.getX() - adaptWidth(30 + i * 16) + WIDTH,
                            -cam.getY() + standardHeight / 4 * 3, adaptWidth(10),
                            adaptHeight(25),
                            null); //TIPO COSI, MA DA RIPOSIZIONARE, CENTRARE E OVVIAMENTE MODIFICARE FONT
                }
                int x = -cam.getX();
                for (int j = 0; j < handler.getPlayer().size(); j++) {  //life

                    g.drawImage(face, x, -cam.getY() + (standardHeight / 2), standardWidth, standardHeight, null);    //icona volto
                    x += standardWidth;
                    g.drawImage(hud.getSubimage(91, 10, 7, 7), //x
                            x,
                            -cam.getY() + (standardHeight + adaptHeight(5)),
                            standardWidth / 4,
                            standardHeight / 3, null);
                    toHud = toHud(handler.getPlayer().get(j).getLife());
                    //System.out.println(""+handler.getPlayer().get(j).getLife()+" "+toHud.length);
                    for (int i = toHud.length - 1; i > -1; i--) {    //numero di vite
                        x += standardWidth / 3;
                        g.drawImage(toHud[i],
                                x,
                                -cam.getY() + (standardHeight / 2) + standardWidth / 3,
                                standardWidth / 3,
                                standardHeight / 2,
                                null); //TIPO COSI, MA DA RIPOSIZIONARE, CENTRARE E OVVIAMENTE MODIFICARE FONT
                    }
                    x += standardWidth / 3;
                }

                g.drawImage(coin, -cam.getX(), -cam.getY() + (standardHeight + standardHeight / 2), standardWidth, standardHeight, null);
                toHud = toHud(Player.MONETE);
                x = -cam.getX() + standardWidth;
                for (int i = toHud.length - 1; i > -1; i--) {
                    g.drawImage(toHud[i],
                            x,
                            -cam.getY() + (standardHeight * 2),
                            standardWidth / 3,
                            standardHeight / 3,
                            null);
                    x += standardWidth / 3;
                }
                //g.drawString("" + Player.MONETE, -cam.getX() + (standardWidth / 2) - adaptWidth(3), -cam.getY() + adaptHeight(4) + standardHeight); //TIPO COSI, MA DA RIPOSIZIONARE, CENTRARE E OVVIAMENTE MODIFICARE FONT
                //g.drawString("PUNTEGGIO: " + Player.PUNTEGGIO, -cam.getX() + adaptWidth(150), -cam.getY() + standardHeight); //TIPO COSI, MA DA RIPOSIZIONARE, CENTRARE E OVVIAMENTE MODIFICARE FONT
                g.drawString("FPS: " + FPS, -cam.getX() + WIDTH - adaptWidth(150), -cam.getY() + standardHeight); //TIPO COSI, MA DA RIPOSIZIONARE, CENTRARE E OVVIAMENTE MODIFICARE FONT
            } else if (loading) {

            } else if (!isLoad || wait) {
                g.translate(0, 0);
                g.clearRect(0, 0, WIDTH, HEIGHT);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
                //g.fillRect(0, 0, WIDTH, HEIGHT);
                int index = Query.score.getValue().length;
                for (String[] s : Query.score.getValue()) {
                    String toWrite = "";
                    for (String temp : s) {
                        toWrite += temp + " ";
                    }

                    BufferedImage[] toHud = toHud(s[0]);
                    drawHud(g,
                            toHud,
                            WIDTH / 2 - (toHud.length + 1) * adaptWidth(25),
                            HEIGHT / 2 - (s.length + index) * adaptHeight(25),
                            adaptWidth(25),
                            adaptHeight(25),
                            true,
                            false);
                    inverseDrawHud(g,
                            toHud(Integer.parseInt(s[1])),
                            WIDTH / 2 + adaptWidth(25),
                            HEIGHT / 2 - (s.length + index) * adaptHeight(25),
                            adaptWidth(25),
                            adaptHeight(25),
                            true,
                            false);
                    index--;
                    //g.drawString(toWrite, WIDTH / 2 - 100, HEIGHT / 2 - (s.length / 2 - index) * 50);
                    
                }
                wait = true;
                delay = delay == 0 ? timeShow : delay;
                //System.out.println(""+(delay-timeShow));
                if (delay - timeShow >= 15) {
                    wait = false;
                    delay = 0;
                    timeShow = 346;
                    Query.score = null;
                }
            } else {
                g.translate(0, 0);  //AZZERO LE COORDINATE I DISEGNO DEL FRAME
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
                g.setColor(Color.YELLOW);
                g.drawString("GAME OVER", WIDTH / 2, HEIGHT / 2);
                if (timeShow == -6) {
                    stopGame();
                }
                //g.drawImage(load, 0, 0, WIDTH, HEIGHT, null); //DISEGNO LA GIF CHE INDICA IL CARICAMENTO
            }
            //g.dispose();
            strategy.show();
        } catch (NullPointerException e) {
            Log.append("errore", DefaultFont.ERROR);
        }
        //System.out.println("time: "+(System.currentTimeMillis()-time));
    }

    public void tick() {
        if (isLoad && gameLoop && !allDie && !wait) {
            handler.tick(); //SI OCCUPA DEL MOVIMENTO DI TUTTI GLI SPRITE E DELLE VARIE OPZIONI DI OGNI PLAYER, TILE ED ENEMIES
            try {
                cam.tick(handler.getPlayer().get(0));    //AGGIORNA IL CENTRO DEL FRAME
            } catch (java.lang.IndexOutOfBoundsException ex) {
            }
        }
    }

    @Override
    public void run() { //THREAD
        Log.append("8)THREAD MAIN", DefaultFont.INFORMATION);

        long lastTime = System.nanoTime();
        final double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        //System.out.println(""+ns);
        double delta = 0;
        int ticks = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();

        while (gameLoop) {
            long now = System.nanoTime();
            if (time + 1000000000.0 < now) {
                timeShow -= time != 0 ? ((int) (((now - time) / 1000000000.0))) : 0;
                //System.out.println(""+((int) ((now - time) / 1000000000.0)));
                time = now;
                if (timeShow == -1) {
                    for (int i = 0; i < gameOver.length; i++) {
                        gameOver[i] = true;
                    }
                    allDie = true;
                }
            }
            delta += (now - lastTime) / ns;
            //System.out.println(""+delta);
            /*if(delta > 1){
                    delta = 1.0f;
                }*/
            //delta = Math.min(delta, 1 / 60);
            lastTime = now;
            if (delta >= 1 && ticks < amountOfTicks && !wait) {
                //if(delta > 0.00f)
                tick();
                ticks++;
                delta--;
            }
            //long temp = System.currentTimeMillis();
            render();
            //System.out.println("Total time: "+(System.currentTimeMillis() - temp));
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                //System.out.println("FPS : " + frames + " Ticks : " + ticks);
                FPS = ticks;
                frames = 0;
                ticks = 0;
                System.gc();
            }
            try {
                sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(SuperMario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public synchronized void addOption() {
        this.setVisible(false);
        gameLoop = false;

        handler.getSound().stop();

        frame.add((option = new Menu(this)));
        frame.removeKeyListener(movement);

        frame.revalidate();
        frame.repaint();

        System.gc();
    }

    public void removeOption() {
        frame.remove(option);
        gameLoop = true;

        this.setVisible(true);

        handler.getSound().loop();
        frame.addKeyListener(movement);

        frame.revalidate();
        frame.repaint();
        System.gc();
        new Thread(this).start();
    }

    public static int adaptWidth(int val) { //RIADATTO LA LARGHEZZA DELLE IMMAGINI IN BASE ALLA GRANDEZZA DELLO SCHERMO
        return (int) ((double) val / 1200.0 * WIDTH);
    }

    public static int adaptHeight(int val) {    //RIADATTA LA ALTEZZA DELLE IMMAGINI IN BASE ALLA GRANDEZZA DELLO SCHERMO
        return (int) ((double) val / 900.0 * HEIGHT);
    }

    public static double adaptWidth(double val) {   //UGUALE AD INT
        return (double) ((double) val / 1200.0 * WIDTH);
    }

    public static double adaptHeight(double val) {  //UGUALE AD INT
        return (double) ((double) val / 900.0 * HEIGHT);
    }

    public void stopGame() {
        stop();
        frame.dispose();

        handler.clean();
        handler = null;
        gameOver = null;
        movement.clean();
        movement = null;
        cam = null;
        
        bg = null;
        coin = null;
        face = null;
        
        Sound.clean();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainComponent();
            }
        });
    }

    public Frame getFrame() {
        return frame;
    }

    public Handler getHandler() {
        return handler;
    }

    public boolean getGameLoop() {
        return gameLoop;
    }

    public void setIsWait() {
        wait = true;
    }

}
