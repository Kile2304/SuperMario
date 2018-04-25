/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario.rm.utility;

/**
 *
 * @author mantini.christian
 */
public class Font {
    
    public final class ColorName{
        
        public static final String RED = "RED";
        public static final String GREEN = "GREEN";
        public static final String YELLOW = "YELLOW";
        public static final String BLUE = "BLUE";
        public static final String PINK = "PINK";
        public static final String PURPLE = "PURPLE";
        public static final String BLACK = "BLACK";
        public static final String LIGHTBLUE = "LIGHTBLUE";
        public static final String GRAY = "GRAY";
    
    }
    
    public static String setHtmlColor(String testo, String color){
        return "<html>"
                + "<font color=\""+color+"\">"+testo+"</font>"
                + "</html>";
    }
    
}
