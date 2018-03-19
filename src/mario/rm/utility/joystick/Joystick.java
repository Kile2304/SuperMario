package mario.rm.utility.joystick;

import static java.lang.Thread.sleep;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mario.rm.other.DefaultFont;
import mario.rm.utility.Log;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

/**
 *
 * @author LENOVO
 */
public class Joystick extends Thread {

    public static boolean UPDATE = false;

    public static Controller[] lastUpdate;

    public static final Controller[] getController() {
        LinkedList<Controller> joystick = new LinkedList<>();

        try {
            /*for (Controller c : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
            if (c.getType() == Controller.Type.GAMEPAD) {
            joystick.add(c);
            //System.out.println(joystick.getName());
            }
            }*/
            
            for (Controller c : createDefaultEnvironment().getControllers()) {
                if (c.getType() == Controller.Type.GAMEPAD) {
                    joystick.add(c);
                    //System.out.println(joystick.getName());
                }
            }
        } catch (ReflectiveOperationException ex) {
            Logger.getLogger(Joystick.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*if (joystick == null) {
            System.err.println("No joystick was found.");
            System.exit(1);
        }*/

 /*for (Controller c : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
            System.out.println("last: " + c.getName());
        }*/
        Controller[] contr = new Controller[joystick.size()];
        for (int i = 0; i < contr.length; i++) {
            contr[i] = joystick.pop();
        }
        return contr;
    }

    private static ControllerEnvironment createDefaultEnvironment() throws ReflectiveOperationException {

        // Find constructor (class is package private, so we can't access it directly)
        Constructor<ControllerEnvironment> constructor = (Constructor<ControllerEnvironment>) Class.forName("net.java.games.input.DefaultControllerEnvironment").getDeclaredConstructors()[0];

        // Constructor is package private, so we have to deactivate access control checks
        constructor.setAccessible(true);

        // Create object with default constructor
        return constructor.newInstance();
    }

    @Override
    public void run() {

        while (UPDATE) {

            Controller[] controller = getController();

            if(controller.length > 0)   System.out.println("" + controller.length + " type: " + 
                    controller[0].getType().toString() + " name: " + controller[0].getName()+
                    " portNumber "+controller[0].getPortNumber()+
                    " portType: "+controller[0].getPortType().toString());

            LinkedList<Controller> toAdd = new LinkedList<>();
            LinkedList<PlaystationController> toRemove = new LinkedList<>();

            LinkedList<PlaystationController> actual = ControllerListener.controller;

            for (int i = 0; i < controller.length; i++) {  //per fare funzionare il ciclo bisogna invertirlo ed eliinare tutti gli elementi della linkedlist
                boolean find = false;
                //Log.append(""+controller[i].hashCode(), DefaultFont.ERROR);
                for (int j = 0; j < actual.size(); j++) {
                    if (actual.get(j).getController() == controller[i]) {
                        //actual.remove(j);
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    toAdd.add(controller[i]);
                }
            }
            for (int i = 0; i < actual.size(); i++) {
                boolean removed = true;
                for (int j = 0; j < controller.length; j++) {
                    if (controller[j] == actual.get(i).getController()) {
                        removed = false;
                    }
                }
                if (removed) {
                    toRemove.add(actual.get(i));
                }
            }

            if (actual.size() > 0) {
                ControllerListener.removeController(toRemove);
            }

            if (toAdd.size() > 0) {
                ControllerListener.addController(toAdd);
            }

            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Log.append(Log.stackTraceToString(ex), DefaultFont.ERROR);
            }
        }
    }

}
