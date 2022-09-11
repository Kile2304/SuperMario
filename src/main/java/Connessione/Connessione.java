package Connessione;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import mario.rm.utility.Log;

/**
 *
 * @author mantini.christian
 */
public class Connessione {

    private static final String USER = "root";
    private static final String PASSWORD = "";
    //private static final String ADDRESS = "jdbc:mysql://192.168.1.252:3306/";
    //private static final String ADDRESS = "jdbc:mysql://192.168.1.123:3306/";
    //private static final String ADDRESS = "jdbc:mysql://80.22.95.8:3306/";
    //private static final String ADDRESS = "jdbc:mysql://localhost:3306/";
    private static final int PORT = 3306;
    private static final String ADDRESS = "jdbc:mysql://localhost:" + PORT + "/";
    private static final String DB_NAME = "superluigi";

    private static Connection connessione;

    public static Connection getConnection()  {
        try {
            if (connessione == null) {
                connessione = DriverManager.getConnection(ADDRESS + DB_NAME, USER, PASSWORD);
            }
        } catch(SQLException ex){
            Log.append("Non sono riuscito a connettermi all' indirizzo: "+ADDRESS+PORT+"/"+DB_NAME);
        }
        return connessione;
    }
    
    public static boolean isConnected(){
        return connessione != null;
    }

}
