package Connessione;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mantini.christian
 */
public class Query {

    public static Relazione score;
    
    public static Relazione sendSelect(String qrySel) {
        Relazione rel = null;
        Statement stmt2 = null;

        Connection conn = null;
        conn = Connessione.getConnection();
        try {
            stmt2 = conn.createStatement();
            ResultSet rs = stmt2.executeQuery(qrySel);
            ResultSetMetaData rsmd = rs.getMetaData();

            String[] column = new String[rsmd.getColumnCount()];
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                column[i - 1] = rsmd.getColumnName(i);
            }

            ArrayList<String[]> row = new ArrayList<>();

            while (rs.next()) {
                String[] riga = new String[column.length];
                for (int i = 0; i < riga.length; i++) {
                    riga[i] = rs.getString(column[i]);
                }
                row.add(riga);
            }
            String[][] value = new String[row.size()][column.length];
            for (int i = 0; i < row.size(); i++) {
                value[i] = row.get(i);
            }

            rel = new Relazione(column, value);
            //Gui.console.setText(qrySel+"\nEseguita correttamente");
        } catch (SQLException se) {
            System.out.println("ERRORE durante SELECT");
            System.out.println("Codice Errore: " + se.getErrorCode() + " " + se.getMessage());
            //Gui.console.setText(se.getErrorCode() + " " + se.getMessage());
        }
        return rel;
    }

    public static void insert(String query){
        try {
            Connection conn = Connessione.getConnection();
            Statement st = conn.createStatement();
            
            // note that i'm leaving "date_created" out of this insert statement
            st.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
