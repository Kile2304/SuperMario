package mario.rm.utility.joystick;

import java.awt.Button;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static java.lang.Thread.sleep;
import java.util.LinkedList;
import mario.MainComponent;
import mario.rm.SuperMario;
import mario.rm.handler.Handler;
import mario.rm.input.Movement;
import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;
import net.java.games.input.Component;
import net.java.games.input.Controller;

/**
 *
 * @author LENOVO
 */
public class ControllerListener extends Movement implements Runnable, KeyListener {

    public static LinkedList<PlaystationController> controller = new LinkedList<>();
    private float lastPov;

    public ControllerListener(int velX, double jump, Handler handler, SuperMario mario) {
        super(velX, jump, handler, mario);
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < controller.size(); i++) {

                //System.out.println("" + controller.size());
                
                if (controller.size() > i && controller.get(i) != null) {
                    String[] listener = controller.get(i).listener();
                    if (listener != null) {
                        Log.append(listener[0], DefaultFont.DEBUG);
                        if (listener[0].equals("pov")) {
                            if (Float.parseFloat(listener[1]) == 0.0f) {
                                keyReleased(lastPov);
                            } else {
                                lastPov = Float.parseFloat(listener[1]);
                                keyPressed(lastPov);
                            }
                        } else if (Float.parseFloat(listener[1]) == 0.0f) {
                            keyReleased(Float.parseFloat(listener[0]) + 2.0f);
                        } else {
                            keyPressed(Float.parseFloat(listener[0]) + 2.0f);
                        }
                    }
                }
            }
            try {
                sleep(10);
            } catch (InterruptedException ex) {
                Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
            }
        }
    }

    private void keyPressed(float keyPressed) {
        KeyEvent[] event = event(keyPressed);
        for (int i = 0; i < event.length; i++) {
            super.keyPressed(event[i]);
        }
    }

    public KeyEvent[] event(float keyPressed) {
        Button a = new Button("click");
        //Log.append(""+keyPressed, DefaultFont.DEBUG);
        KeyEvent[] e = new KeyEvent[1];
        //System.out.println("" + e.getKeyChar());
        //System.out.println("" + e.getKeyCode());
        if (keyPressed == Component.POV.DOWN) {   //i pov sono gli analogici

        } else if (keyPressed == Component.POV.LEFT) {
            e[0] = new KeyEvent(a, KeyEvent.VK_LEFT, 1, KeyEvent.VK_LEFT, KeyEvent.VK_LEFT, 'l');
        } else if (keyPressed == Component.POV.RIGHT) {
            e[0] = new KeyEvent(a, KeyEvent.VK_RIGHT, 1, KeyEvent.VK_RIGHT, KeyEvent.VK_RIGHT, 'l');
        } else if (keyPressed == Component.POV.DOWN) {
            e[0] = new KeyEvent(a, KeyEvent.VK_DOWN, 1, KeyEvent.VK_DOWN, KeyEvent.VK_DOWN, 'd');

        } else if (keyPressed == Component.POV.UP) {
            e[0] = new KeyEvent(a, KeyEvent.VK_UP, 1, KeyEvent.VK_UP, KeyEvent.VK_UP, 'u');

        } else if (keyPressed == Component.POV.UP_RIGHT) {
            e = new KeyEvent[2];
            e[0] = new KeyEvent(a, KeyEvent.VK_RIGHT, 1, KeyEvent.VK_RIGHT, KeyEvent.VK_RIGHT, 'r');
            e[1] = new KeyEvent(a, KeyEvent.VK_UP, 1, KeyEvent.VK_UP, KeyEvent.VK_UP, 'u');

        } else if (keyPressed == Component.POV.UP_LEFT) {
            e[0] = new KeyEvent(a, KeyEvent.VK_LEFT, 1, KeyEvent.VK_LEFT, KeyEvent.VK_LEFT, 'l');

        } else if (keyPressed == 3.0f) {  //X
            e[0] = new KeyEvent(a, KeyEvent.VK_UP, 1, KeyEvent.VK_UP, KeyEvent.VK_UP, 'u');

        } else if (keyPressed == 11.0f) {  //OPZIONI
            e[0] = new KeyEvent(a, KeyEvent.VK_ESCAPE, 1, KeyEvent.VK_ESCAPE, KeyEvent.VK_ESCAPE, 'e');

        } else if (keyPressed == 10.0f) {  //OPZIONI
            e[0] = new KeyEvent(a, KeyEvent.VK_TAB, 1, KeyEvent.VK_TAB, KeyEvent.VK_TAB, 't');
            //Log.append(""+MainComponent.log.isFocused(), DefaultFont.DEBUG);
            
        }

        return e;
    }

    private void keyReleased(float keyPressed) {
        KeyEvent[] event = event(keyPressed);
        for (int i = 0; i < event.length; i++) {
            super.keyReleased(event[i]);
        }
    }

    public synchronized static void removeController(LinkedList<PlaystationController> get) {
        for (int i = 0; i < get.size(); i++) {
            //Log.append("Rimosso il controller: "+ get.get(i).getController().getName(), DefaultFont.DEBUG);
            controller.remove(get.get(i));
        }
    }

    public synchronized static void addController(LinkedList<Controller> get) {
        for (int i = 0; i < get.size(); i++) {
            controller.add(new PlaystationController(get.get(i)));
            //Log.append("Aggiunto il controller: "+ get.get(i).getName(), DefaultFont.DEBUG);
        }
    }

}
