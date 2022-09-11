/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario.rm.utility;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author LENOVO
 */
public class LogConsole extends JTextPane  {



    public LogConsole() {

        setCaretPosition(0);
        setEditable(false);
        setAutoscrolls(true);
        setBorder(BorderFactory.createMatteBorder( //SETTO IL BORDO
                1, 0, 0, 0, Color.BLACK));
        DefaultCaret caret = (DefaultCaret) getCaret();   //set Autoscroll
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);  //position ever update
    }

}
