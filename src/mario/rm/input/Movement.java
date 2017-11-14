package mario.rm.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import mario.MainComponent;
import mario.rm.SuperMario;
import mario.rm.handler.Handler;
import mario.rm.identifier.Direction;
import mario.rm.identifier.Move;
import mario.rm.sprite.Player;
import mario.rm.sprite.Sprite;
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;
import mario.rm.utility.joystick.PlaystationController;
import net.java.games.input.Controller;

/**
 *
 * @author LENOVO
 */
public class Movement implements KeyListener { //RESPONSABILE DEL MOVIMENTO

    public static int velX;  //COSTANTE PER LA VELOCITA' SULL'ASSE X
    public static double jump; //COSTANTE PER LA DISTANZA DI SALTO

    private static final Sound salto = new Sound("mario/res/Sound/nsmb_jump.wav");

    private final boolean move[];

    private Handler handler;

    protected SuperMario mario;

    //public static boolean cheatJump = false;
    public Movement(int velX, double jump, Handler handler, SuperMario mario) {
        Movement.velX = velX;
        Movement.jump = jump;
        move = new boolean[]{false, false};
        this.handler = handler;
        this.mario = mario;
        Log.append("JUMP: " + jump, DefaultFont.INFORMATION);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    } //DA VERIFICARE IL FUNZIONAMENTO

    @Override
    public void keyPressed(KeyEvent e) {
        if (e == null) {
            return;
        }
        int keyCode = e.getKeyCode();   //NON STRETTAMENTE NECCESSARIO
        for (Player sp : handler.getPlayer()) {
            switch (keyCode) {
                case KeyEvent.VK_LEFT:  //SE VIENE PREMUTO IL TATO SINISTRO
                    if (!sp.isFalling()) {   //SE STA TOCCANDO IL TEERRENO
                        //sp.setDirezione(-1);    //IMPOSTO A STA CORRENDO SPECCHIATO
                        sp.setLastMovement(Direction.LEFT, Move.WALK);
                    } else {
                        //sp.setDirezione(-30);   //SALTO SPECCHIATO
                        sp.setLastMovement(Direction.LEFT, Move.JUMP);
                    }
                    sp.setVelX(-sp.getMoveXIncrease());   //LA VELOCITA X  = -5
                    move[0] = true;
                    sp.setWalking(true);  //STA CAMMINANDO
                    break;
                case KeyEvent.VK_RIGHT: //SE VIENE PREMUTO IL TASTO DESTRO
                    if (!sp.isFalling()) {   //SE STA TOCCANDO IL PAVIMENTO
                        //sp.setDirezione(1); //IMPOSTO A STA' CORRENDO
                        sp.setLastMovement(Direction.RIGHT, Move.WALK);
                    } else {
                        //sp.setDirezione(30);    //IMPOSTO A SALTO NORMALE
                        sp.setLastMovement(Direction.RIGHT, Move.JUMP);
                    }
                    sp.setVelX(sp.getMoveXIncrease());    //VELOCITA X = 5
                    move[1] = true;
                    sp.setWalking(true);  //STA CAMMINANDO
                    break;
                case KeyEvent.VK_UP:
                    if (!sp.isJumping() && !sp.isFalling() || sp.getInfiniteJump()) {    //SE NON STAVA GIA SALTANDO PRIMA
                        if (salto.getCurrentFrame() != 0) {
                            salto.stop();
                        }
                        salto.start();
                        sp.setJumping(true);  //ORA STA SALTANDO
                        sp.setFalling(false);   //IMPOSTO A NON STA' SALTANDO
                        sp.setGravity(sp.getJumpIncrease()); //LA GRAVITA E' UGUALE A 7
                        //sp.setDirezione((sp.getDirezione() / Math.abs(sp.getDirezione())) * 30);    //IMPOSTO IL SALTO NELLA DIREZIONE IN CUI ERA PRIMA
                        sp.setLastMovement(sp.getLastDirection(), Move.JUMP);
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (!sp.isFalling()) {
                        sp.setTeleport();
                    }
                    break;
                case KeyEvent.VK_TAB:
                    if (mario.getFrame().isFocused()) {
                        MainComponent.log.toFront();
                        if (!mario.getMenu()) {
                            mario.addOption();
                        }
                    } else {
                        mario.getFrame().toFront();
                        if (mario.getMenu()) {
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
        int keyCode = e.getKeyCode();   //PALESE
        for (Sprite sp : handler.getPlayer()) {
            switch (keyCode) {
                case KeyEvent.VK_LEFT:  //SE VIENE PREMUTO IL TATO SINISTRO
                    move[0] = false;
                    if (!move[0] && !move[1]) {
                        sp.setWalking(false); //NON STA PIU CAMMINANDO
                    }
                    break;
                case KeyEvent.VK_RIGHT: //SE VIENE PREMUTO IL TASTO DESTRO
                    move[1] = false;
                    if (!move[0] && !move[1]) {
                        sp.setWalking(false);
                    }
                    ; //NON STA PIU CAMMINANDO
                    break;
                case KeyEvent.VK_UP:
                    if (sp.isJumping() && sp.getGravity() <= 1) {
                        sp.setFalling(true);  //STA CADENDO
                        sp.setGravity(-(jump / 3)); //FACCIO IN MODO CHE NON SI FERMI BRUSCAMENTE
                    } else if (sp.isJumping()) {
                        sp.setFalling(true);  //STA CADENDO
                        sp.setGravity(0);   //LO FERMO
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    if (mario.getFrame().isFocused()) {
                        mario.addOption();
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
