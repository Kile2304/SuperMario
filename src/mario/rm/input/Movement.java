package mario.rm.input;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import mario.MainComponent;
import mario.rm.SuperMario;
import mario.rm.handler.Handler;
import mario.rm.identifier.Direction;
import mario.rm.identifier.Move;
import mario.rm.sprite.Player;
import mario.rm.sprite.Sprite;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;

/**
 *
 * @author LENOVO
 */
public class Movement implements KeyListener { //RESPONSABILE DEL MOVIMENTO

    public static int velX;  //COSTANTE PER LA VELOCITA' SULL'ASSE X
    public static double jump; //COSTANTE PER LA DISTANZA DI SALTO

    private static final Sound salto = new Sound("Sound/nsmb_jump.wav");

    private final boolean move[];

    private Handler handler;

    protected SuperMario mario;
    
    private long[] shoot;

    //public static boolean cheatJump = false;
    public Movement(int velX, double jump, Handler handler, SuperMario mario) {
        Movement.velX = velX;
        Movement.jump = jump;
        move = new boolean[]{false, false};
        this.handler = handler;
        this.mario = mario;
        Log.append("JUMP: " + jump, DefaultFont.INFORMATION);
        shoot = new long[SuperMario.playerNumber];
    }

    @Override
    public void keyTyped(KeyEvent e) {
    } //DA VERIFICARE IL FUNZIONAMENTO

    @Override
    public void keyPressed(KeyEvent e) {
        if (e == null) {
            return;
        }
        Log.append("" + e.getID());
        int id = e.getID() == 401 ? 0 : e.getID();

        int keyCode = e.getKeyCode();   //NON STRETTAMENTE NECCESSARIO
        for (Player sp : handler.getPlayer()) {
            switch (keyCode) {
                case KeyEvent.VK_A:id = SuperMario.playerNumber-1;
                case KeyEvent.VK_LEFT:  //SE VIENE PREMUTO IL TATO SINISTRO
                    if (!handler.getPlayer().get(id).isFalling()) {   //SE STA TOCCANDO IL TEERRENO
                        //sp.setDirezione(-1);    //IMPOSTO A STA CORRENDO SPECCHIATO
                        handler.getPlayer().get(id).setLastMovement(Direction.LEFT, Move.WALK);
                    } else {
                        //sp.setDirezione(-30);   //SALTO SPECCHIATO
                        handler.getPlayer().get(id).setLastMovement(Direction.LEFT, Move.JUMP);
                    }
                    handler.getPlayer().get(id).setVelX(-sp.getMoveXIncrease());   //LA VELOCITA X  = -5
                    move[0] = true;
                    handler.getPlayer().get(id).setWalking(true);  //STA CAMMINANDO
                    break;
                case KeyEvent.VK_D:id = SuperMario.playerNumber-1;
                case KeyEvent.VK_RIGHT: //SE VIENE PREMUTO IL TASTO DESTRO
                    if (!handler.getPlayer().get(id).isFalling()) {   //SE STA TOCCANDO IL PAVIMENTO
                        //sp.setDirezione(1); //IMPOSTO A STA' CORRENDO
                        handler.getPlayer().get(id).setLastMovement(Direction.RIGHT, Move.WALK);
                    } else {
                        //sp.setDirezione(30);    //IMPOSTO A SALTO NORMALE
                        handler.getPlayer().get(id).setLastMovement(Direction.RIGHT, Move.JUMP);
                    }
                    handler.getPlayer().get(id).setVelX(sp.getMoveXIncrease());    //VELOCITA X = 5
                    move[1] = true;
                    handler.getPlayer().get(id).setWalking(true);  //STA CAMMINANDO
                    break;
                case KeyEvent.VK_W:id = SuperMario.playerNumber-1;
                case KeyEvent.VK_UP:
                    if (!handler.getPlayer().get(id).isJumping() && !handler.getPlayer().get(id).isFalling() || handler.getPlayer().get(id).getInfiniteJump()) {    //SE NON STAVA GIA SALTANDO PRIMA
                        if (salto.getCurrentFrame() != 0) {
                            salto.stop();
                        }
                        salto.start();
                        handler.getPlayer().get(id).setJumping(true);  //ORA STA SALTANDO
                        handler.getPlayer().get(id).setFalling(false);   //IMPOSTO A NON STA' SALTANDO
                        handler.getPlayer().get(id).setGravity(sp.getJumpIncrease()); //LA GRAVITA E' UGUALE A 7
                        //sp.setDirezione((sp.getDirezione() / Math.abs(sp.getDirezione())) * 30);    //IMPOSTO IL SALTO NELLA DIREZIONE IN CUI ERA PRIMA
                        handler.getPlayer().get(id).setLastMovement(handler.getPlayer().get(id).getLastDirection(), Move.JUMP);
                    }
                    break;
                case KeyEvent.VK_S:id = SuperMario.playerNumber-1;
                case KeyEvent.VK_DOWN:
                    if (!handler.getPlayer().get(id).isFalling()) {
                        handler.getPlayer().get(id).setTeleport();
                    }
                    break;
                case KeyEvent.VK_TAB:
                    if (mario.getFrame().isFocused()) {
                        MainComponent.log.toFront();
                        if (mario.getGameLoop()) {
                            mario.addOption();
                        }
                    } else {
                        mario.getFrame().toFront();
                        if (!mario.getGameLoop()) {
                            mario.removeOption();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e == null) {
            return;
        }

        int id = e.getID() == 402 ? 0 : e.getID();

        int keyCode = e.getKeyCode();   //PALESE
        for (Sprite sp : handler.getPlayer()) {
            switch (keyCode) {
                case KeyEvent.VK_A: id = SuperMario.playerNumber-1;  //SE VIENE PREMUTO IL TATO SINISTRO
                case KeyEvent.VK_LEFT:  //SE VIENE PREMUTO IL TATO SINISTRO
                    move[0] = false;
                    if (!move[0] && !move[1]) {
                        handler.getPlayer().get(id).setWalking(false); //NON STA PIU CAMMINANDO
                    }
                    break;
                case KeyEvent.VK_D: id = SuperMario.playerNumber-1; //SE VIENE PREMUTO IL TASTO DESTRO
                case KeyEvent.VK_RIGHT: //SE VIENE PREMUTO IL TASTO DESTRO
                    move[1] = false;
                    if (!move[0] && !move[1]) {
                        handler.getPlayer().get(id).setWalking(false);
                    }
                    //NON STA PIU CAMMINANDO
                    break;
                case KeyEvent.VK_W:id = SuperMario.playerNumber-1;
                case KeyEvent.VK_UP:
                    if (handler.getPlayer().get(id).isJumping() && handler.getPlayer().get(id).getGravity() <= 1) {
                        handler.getPlayer().get(id).setFalling(true);  //STA CADENDO
                        handler.getPlayer().get(id).setGravity(-(jump / 3)); //FACCIO IN MODO CHE NON SI FERMI BRUSCAMENTE
                    } else if (sp.isJumping()) {
                        handler.getPlayer().get(id).setFalling(true);  //STA CADENDO
                        handler.getPlayer().get(id).setGravity(0);   //LO FERMO
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    if (mario.getFrame().isFocused()) {
                        mario.addOption();
                    }
                    break;

                /*case KeyEvent.VK_Z:
                    BufferedImage image;
                    try {
                        Toolkit tk = Toolkit.getDefaultToolkit(); //Toolkit class                         returns the default toolkit
                        Dimension d = tk.getScreenSize();

//Dimension class object stores width & height of the toolkit screen
// toolkit.getScreenSize() determines the size of the screen
                        Rectangle rec = new Rectangle(SuperMario.frame.getX(), SuperMario.frame.getY(), SuperMario.frame.getWidth(), SuperMario.frame.getHeight());
//Creates a Rectangle with screen dimensions,         

                        Robot ro = new Robot(); //to capture the screen image
                        BufferedImage img = ro.createScreenCapture(rec);

                        File f;
                        f = new File("myimage.png"); // File class is used to write the above generated buffered image to a file
                        if(f.exists()) f.createNewFile();
                        ImageIO.write(img, "png", f);
                    } catch (AWTException | IOException ex) {
                        Logger.getLogger(Movement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;*/
                case KeyEvent.VK_V:id = SuperMario.playerNumber-1;
                case KeyEvent.VK_CONTROL: //shoot if he have a power-up
                    if (handler.getPlayer().get(id).isPowerUp() && shoot[id] + 1000 < System.currentTimeMillis()) {
                        handler.getPlayer().get(id).shoot();
                        shoot[id] = System.currentTimeMillis();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void setVelX(int velX) {
        Movement.velX = velX;
    }

    public void setJump(int jump) {
        Movement.jump = jump;
    }

    public int getVelX() {
        return velX;
    }

    public double getJump() {
        return jump;
    }

}
