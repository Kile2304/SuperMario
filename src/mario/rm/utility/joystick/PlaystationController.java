package mario.rm.utility.joystick;

import mario.rm.utility.DefaultFont;
import mario.rm.utility.Log;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

/**
 *
 * @author LENOVO
 */
public class PlaystationController {    //fino a 13, non so le frecce e le leve

    private Controller controller;

    /*private static final String[] name = new String[]{"quadrato", "x", "cerchio", "triangolo", "l1", "r1", "l2", "r2", "share",
        "options", "l3", "r3", "home", "centro"};*/

    public PlaystationController(Controller controller) {
        this.controller = controller;
    }

    public String[] listener() {

        //Rotazione Z Asse Z Asse Y Asse X Hat Switch Pulsante 0 Pulsante 1 Pulsante 2 Pulsante 3 Pulsante 4 Pulsante 5 Pulsante 6 Pulsante 7 Pulsante 8 Pulsante 9 Pulsante 10 Pulsante 11 Pulsante 12 Pulsante 13 Rotazione Y Rotazione X
        String[] key = null;

        controller.poll();
        for (Component c : controller.getComponents()) {
            //System.out.print(""+c.getName()+" ");

            EventQueue queue = controller.getEventQueue();
            Event event = new Event();
            while (queue.getNextEvent(event)) {
                StringBuffer buffer = new StringBuffer(controller.getName());    //WIRELESS CONTROLLER
                if (buffer.toString().equals("Wireless Controller")) {

                    //buffer.append(" at ");
                    //buffer.append(event.getNanos()).append(", ");
                    Component comp = event.getComponent();
                    if (comp.isAnalog()) {
                        //System.out.println(comp.getIdentifier() + " " + comp.getPollData() + " ");

                    } else if (comp.isRelative()) {
                        float valore = c.getPollData();
                        System.out.println("" + comp.getIdentifier() + " ");
                        if (valore == 1.0f) {
                            System.out.println("On [" + valore + "]");
                        } else {
                            System.out.println("Off [" + valore + "]");
                        }
                    } else {
                        //key = comp.getIdentifier().toString();
                        //Log.append(Component.POV.DOWN+" "+comp.getPollData()+" "+comp.getDeadZone(), DefaultFont.ERROR);
                        /*if (comp.getPollData() == Component.POV.DOWN) {
                            Log.append("POV DOWN: "+comp.getPollData(), DefaultFont.DEBUG);
                        } else {
                            Log.append("" + comp.getIdentifier() + " " + comp.getPollData(), DefaultFont.DEBUG);
                        }*/
                        key = new String[2];
                        key[0] = comp.getIdentifier().toString();
                        key[1] = ""+comp.getPollData();
                    }

                }
                //System.out.println("");

            }
        }
        return key;
    }
    
    public Controller getController(){
        return controller;
    }
}
