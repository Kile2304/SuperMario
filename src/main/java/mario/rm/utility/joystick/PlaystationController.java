package mario.rm.utility.joystick;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
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

    private int id;

    private static LinkedList<Integer> player = new LinkedList<>(Arrays.asList(0, 1, 2, 3));

    /*private static final String[] name = new String[]{"quadrato", "x", "cerchio", "triangolo", "l1", "r1", "l2", "r2", "share",
        "options", "l3", "r3", "home", "centro"};*/
    public PlaystationController(Controller controller) {
        this.controller = controller;
        id = player.pop();
    }

    public String[] listener() {
        
        String[] key = null;

        controller.poll();
        for (Component c : controller.getComponents()) {

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
                        key[1] = "" + comp.getPollData();
                    }

                }
                //System.out.println("");

            }
        }
        return key;
    }

    public int getID() {
        return id;
    }

    public Controller getController() {
        return controller;
    }

    public void insert() {
        player.push(id);
        Collections.sort(player);
    }

}
