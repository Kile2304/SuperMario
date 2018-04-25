package mario.rm.utility;

import java.util.Map;

/**
 *
 * @author LENOVO
 */
public class Utility {
    
    public static boolean isThread(String threadName){
        Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();

        boolean isThread = false;
        
        for (Thread tt : map.keySet()) {
            if(tt.getName().equals(threadName)){
                isThread = true;
                break;
            }
        }
        return isThread;
    }
    
    public static Thread getThread(String threadName){
        Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();

        Thread isThread = null;
        
        for (Thread tt : map.keySet()) {
            if(tt.getName().equals(threadName)){
                isThread = tt;
                break;
            }
        }
        return isThread;
    }
    
}
