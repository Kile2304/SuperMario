package mario.rm.multigiocatore;

/**
 *
 * @author LENOVO
 */
public enum Protocol {
    
    WAIT(100),
    READY(200),
    OK(400),
    DISCONNESSO(500);
    
    private final int val;
    
    Protocol(int val){
        this.val = val;
    }
    
    public int getProtocol(){
        return val;
    }
    
}
