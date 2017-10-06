package mario.rm.Menu.opzioni;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import mario.rm.SuperMario;

/**
 *
 * @author LENOVO
 */
public class Menu extends JPanel implements ActionListener{
    
    private final SuperMario mario;
    
    public Menu(SuperMario mario){
        super();
        
        setLayout(new BorderLayout());
        
        JPanel center = new JPanel();
        
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        
        JButton resume = new JButton("RESUME");
        resume.addActionListener(this);
        center.add(resume);
        
        JButton option = new JButton("OPTION");
        option.addActionListener(this);
        center.add(option);
        
        JButton home = new JButton("HOME");
        home.addActionListener(this);
        center.add(home);
        
        JButton esci = new JButton("ESCI");
        esci.addActionListener(this);
        center.add(esci);
        
        add(center, BorderLayout.CENTER);
        
        this.mario = mario;
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        switch(e.getActionCommand()){
            case "RESUME":
                mario.removeOption();
                break;
            case "OPTION":
                break;
            case "HOME":
                mario.stopGame();
                break;
            case "ESCI":
                System.exit(0);
                break;
        }
        
    }
    
}
