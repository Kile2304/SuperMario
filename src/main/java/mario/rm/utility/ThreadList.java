package mario.rm.utility;

/**
 *
 * @author LENOVO
 */
public enum ThreadList {
    
    SUPERMARIO("SuperMario"),
    JOYSTICK("Joystick");
    
    public String threadName;
    
    ThreadList(String threadName){
        this.threadName = threadName;
    }
    
    public String getThreadName(){
        return threadName;
    }
}
