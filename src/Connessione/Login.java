/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connessione;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import mario.rm.Menu.home.Home;

/**
 *
 * @author mantini.christian
 */
public class Login extends JFrame implements ActionListener {

    private JTextField username;
    private JPasswordField passw;

    public Login(Dimension dim) {
        super("Super Luigi");
        setLayout(null);
        setSize(dim);
        setBackground(Color.BLACK);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(content());

        setVisible(true);
    }

    private JPanel content() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        Dimension dim = new Dimension(getSize().width / 2, getSize().height / 2);
        panel.setSize(dim);

        panel.setLocation(getSize().width / 4, getSize().height / 4);

        panel.add(new JLabel("USERNAME"));
        panel.add((username = new JTextField()));

        panel.add(new JLabel("PASSWORD"));
        panel.add((passw = new JPasswordField()));

        JButton b = new JButton("INDIETRO");
        b.addActionListener(this);
        panel.add(b);

        b = new JButton("LOGIN");
        b.addActionListener(this);
        panel.add(b);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "LOGIN":
                System.out.println("" + passw.getText());
                Relazione sel = Query.sendSelect("SELECT * FROM utente where username=\"" + username.getText() + "\" AND password=\"" + passw.getText()+"\"");
                if (sel != null) {
                    Profilo.password = sel.getValue()[0][0];
                    Profilo.email = sel.getValue()[0][1];
                    Profilo.username = sel.getValue()[0][2];
                    Profilo.looged = true;
                    System.out.println(""+Profilo.email+" "+Profilo.username+" "+Profilo.password);
                } else {
                    username.setText("");
                    passw.setText("");
                    JOptionPane.showMessageDialog(rootPane, "Utente non trovato");
                    break;
                }
            case "INDIETRO":
                dispose();
                new Home();
                break;
        }
    }

}
