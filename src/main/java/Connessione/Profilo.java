package Connessione;

/**
 *
 * @author mantini.christian
 */
public class Profilo {
    
    public static String email;
    public static String username;
    public static String password;
    public static boolean looged = false;

    public Profilo(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
    
    public static final void logout(){
        looged = false;
        email = "";
        username = "";
        password = "";
    }

}
