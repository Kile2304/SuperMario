/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connessione;

/**
 *
 * @author mantini.christian
 */
public class Relazione {
    
    private String[] columnName;
    private String[][] value;
    
    public Relazione(String[] columnName, String[][] value){
        this.columnName = columnName;
        this.value = value;
    }
    
    public String[] getColumnName(){
        return columnName;
    }
    
    public String[][] getValue(){
        return value;
    }
    
}
