package mario.rm.utility;

import mario.rm.other.DefaultFont;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.text.StyledDocument;
import mario.MainComponent;
import mario.rm.SuperMario;
import mario.rm.handler.Handler;
import mario.rm.input.Loader;
import mario.rm.sprite.Player;

/**
 *
 * @author LENOVO
 */
public class Log extends JFrame implements KeyListener {

    private static final String TITLE = "LOG CONSOLE";

    public static LogConsole logConsole; //zona per visualizzare l'output

    private static final String DEFAULT = "Salve, benvenuto sulla console di Super Mario, questo gioco e' ancora in beta, quindi puo incorrere in errore.\n\n";

    private static StyledDocument doc;

    private final JTextArea logCommand;   //zona per inserire i comandi

    private final MainComponent main;
    
    private static File f = new File("log.txt");

    /**
     * <b>viene utilizzato, per inizializzare la schermata di log</b>
     *
     * @param main questa classe deve contenere l'attributo SuperMario
     */
    public Log(MainComponent main) {
        super(TITLE);

        this.main = main;

        setLayout(new BorderLayout());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int width = screenSize.width / 2;
        int height = screenSize.height / 2;

        setSize(width, height);

        logConsole = new LogConsole();
        logConsole.setBackground(Color.BLACK);

        //logConsole.setLineWrap(true);
        //logConsole.setLineWrap(true);
        //logConsole.setPreferredSize(new Dimension(width, height / 10 * 9));
        doc = logConsole.getStyledDocument();   //VARIABILE PER SCRIVERE SULLA CONSOLE
        
        append(DEFAULT, DefaultFont.STANDARD);  //STRINGA DELLA CONSOLE INIZIALE

        logCommand = new JTextArea();
        logCommand.setCaretPosition(0); //LO FACCIO COMINCIARE DALL'INIZIO
        logCommand.setLineWrap(true);
        logCommand.setLineWrap(true);
        logCommand.addKeyListener(this);    //QUANDO PREMO INVIO
        logCommand.getDocument().putProperty("filterNewlines", Boolean.TRUE);   //SI PUO LAVORARE SOLO SU UNA RIGA

        JScrollPane scrollLog = new JScrollPane(logConsole);
        scrollLog.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollLog.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        logCommand.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        add(scrollLog, BorderLayout.CENTER);
        add(logCommand, BorderLayout.SOUTH);

        JPanel west = new JPanel();
        west.setBorder(BorderFactory.createMatteBorder(
                0, 0, 0, 1, Color.BLACK));
        add(west, BorderLayout.WEST);

        JPanel east = new JPanel();
        east.setBorder(BorderFactory.createMatteBorder(
                0, 1, 0, 0, Color.BLACK));
        add(east, BorderLayout.EAST);

        add(new JPanel(), BorderLayout.NORTH);

        this.setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setIconImage(Loader.LoadImage("Immagini/Luma-Yellow-icon.png"));

        setVisible(true);

    }

    /*public static final void append(String text) {
        if (logConsole == null) {
            return;
        }
        try {
            Style style = logConsole.addStyle("Arial", null);   //INIZIALIZZO LO STILE
            StyleConstants.setForeground(style, Color.BLACK);   //SETTO IL COLORE NELLO STILE

            if (text.charAt(text.length() - 1) != '\n') { //AGGIUNGO L'ACCAPO SE NON CI FOSSE
                text += "\n";
            }

            doc.insertString(doc.getLength(), text, style); //AGGIUNGO LA STRINGA AL TEXTPANE
        } catch (BadLocationException ex) {
            append(stackTraceToString(ex), DefaultFont.ERROR);
        }
    }*/

    /**
     *
     * @param text Testo da aggiungere al log
     * @param defFont font da usare (colore, stile, dimensione...)
     */
    /*public static final void append(String text, DefaultFont defFont) { //UGUALE ALL'ALTRO APPEND MA LO STYLE SI BASA SU UN FONT PERSONALIZZATO
        if (logConsole == null) {
            return;
        }
        try {
            Style style = logConsole.addStyle(defFont.getFont().getFontName(), null);

            StyleConstants.setForeground(style, defFont.getColor());    //setto il colore dei caratteri
            StyleConstants.setFontSize(style, defFont.getFont().getSize()); //setto la dimensione dei caratteri
            StyleConstants.setBold(style, defFont.getFont().isBold());  //setto in grassetto se neccessario
            StyleConstants.setItalic(style, defFont.getFont().isItalic());  //setto in corsivo se neccessario

            //Setting the font Size
            //doc.setCharacterAttributes(0, doc.getLength(), fontSize, false);
            if (text.charAt(text.length() - 1) != '\n') {
                text += "\n";
            }
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException ex) {
            append(stackTraceToString(ex), DefaultFont.ERROR);
        }
    }*/
    public static final void append(String text){
        write(text);
    }
    
    public static final void write(String text){
        FileWriter fw = null;
        try {
            fw = new FileWriter(f, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(text+"\n");
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static final void append(String text, DefaultFont defFont){
        write(text);
    }

    /**
     *
     * @param e eccezione creata dall'errore
     * @return ritorna una stringa dettagliata sull'errore e sui richiami delle
     * funzioni
     */
    public static final String stackTraceToString(Throwable e) {    //INSERISCE IN UNA STRINGA GLI ERRORI
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        boolean errore = false;
        SuperMario mario = main.getSuperMario();
        Handler handler = null;
        if (mario != null) {
            handler = mario.getHandler();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            String comando = logCommand.getText().toLowerCase();
            //System.out.println(comando);
            String[] scomposto = comando.split(" ");
            switch (scomposto[0]) {
                case "god":
                    if (scomposto.length == 3 && scomposto[1].equals("mode")) {
                        if (handler.getPlayer() != null) {
                            int numberOfPlayer = Integer.parseInt(scomposto[2]) - 1;
                            if (numberOfPlayer < handler.getPlayer().size() && numberOfPlayer > -1) {
                                handler.getPlayer().get(numberOfPlayer).changeGodMode();
                            } else {
                                errore = true;
                                comando = "Error, the player number " + numberOfPlayer + " does not exist";
                            }
                        } else {
                            errore = true;
                            comando = "Error in god, the player have not yet been initialized: " + comando;
                        }
                    } else {
                        errore = true;
                        comando = "Error in god: " + comando;
                    }
                    break;
                case "fly":
                    if (scomposto.length == 3 && scomposto[1].equals("mode")) {
                        if (handler.getPlayer() != null) {
                            int numberOfPlayer = Integer.parseInt(scomposto[2]) - 1;
                            if (numberOfPlayer < handler.getPlayer().size() && numberOfPlayer > -1) {
                                handler.getPlayer().get(numberOfPlayer).changeInfiniteJump();
                            } else {
                                errore = true;
                                comando = "Error, the player number " + numberOfPlayer + " does not exist";
                            }
                        } else {
                            errore = true;
                            comando = "Error in fly, the player have not yet been initialized: " + comando;
                        }
                    } else {
                        errore = true;
                        comando = "Error in fly: " + comando;
                    }
                    break;
                case "restart":
                    if (scomposto.length == 2 && scomposto[1].equals("level")) {
                        if (main.getSuperMario() != null) {
                            main.getSuperMario().createLV(true);
                        } else {
                            errore = true;
                            comando = "Error in restart level, the game have not yet been initialized: " + comando;
                        }
                    } else {
                        errore = true;
                        comando = "Error in restart command: " + comando;
                    }
                    break;
                case "home":
                    main.getSuperMario().stopGame();
                    break;
                case "quit":
                    System.exit(0);
                    break;
                case "set":
                    if (scomposto.length == 3 || scomposto.length == 4) {
                        switch (scomposto[1]) {
                            case "point":
                                Player.PUNTEGGIO = Integer.parseInt(scomposto[2]);
                                break;
                            case "life":
                                if (handler.getPlayer() != null) {
                                    int numberOfPlayer = Integer.parseInt(scomposto[2]) - 1;
                                    if (numberOfPlayer < handler.getPlayer().size() && numberOfPlayer > -1) {
                                        handler.getPlayer().get(numberOfPlayer).setLife(Integer.parseInt(scomposto[3]));
                                    } else {
                                        errore = true;
                                        comando = "Error, the player number " + (numberOfPlayer + 1) + " does not exist";
                                    }
                                } else {
                                    errore = true;
                                    comando = "Error in set life, the player have not yet been initialized: " + comando;
                                }
                                break;
                            case "move":
                                //Movement.velX = Integer.parseInt(scomposto[2]);
                                if (handler.getPlayer() != null) {
                                    int numberOfPlayer = Integer.parseInt(scomposto[2]) - 1;
                                    if (numberOfPlayer < handler.getPlayer().size() && numberOfPlayer > -1) {
                                        handler.getPlayer().get(numberOfPlayer).setMoveXIncrease(Integer.parseInt(scomposto[3]));
                                    } else {
                                        errore = true;
                                        comando = "Error, the player number " + (numberOfPlayer + 1) + " does not exist";
                                    }
                                } else {
                                    errore = true;
                                    comando = "Error in set move, the player have not yet been initialized: " + comando;
                                }
                                break;
                            case "jump":
                                if (handler.getPlayer() != null) {
                                    int numberOfPlayer = Integer.parseInt(scomposto[2]) - 1;
                                    if (numberOfPlayer < handler.getPlayer().size() && numberOfPlayer > - 1) {
                                        handler.getPlayer().get(numberOfPlayer).setJumpIncrease(Double.parseDouble(scomposto[3]));
                                    } else {
                                        errore = true;
                                        comando = "Error, the player number " + (numberOfPlayer + 1) + " does not exist";
                                    }
                                } else {
                                    errore = true;
                                    comando = "Error in set jump, the player have not yet been initialized: " + comando;
                                }
                                //Movement.jump = Double.parseDouble(scomposto[2]);
                                break;
                            default:
                                errore = true;
                                comando = "Error in set command: " + comando;
                                break;
                        }
                    } else {
                        errore = true;
                        comando = "Error in set command: " + comando;
                    }
                    break;
                case "get": //ritorna il valore
                    if (scomposto.length == 2 || scomposto.length == 3 || scomposto.length == 4) {
                        switch (scomposto[1]) {
                            case "point":
                                comando = "Point = " + Player.PUNTEGGIO;
                                break;
                            case "life":
                                if (handler.getPlayer() != null) {
                                    int numberOfPlayer = Integer.parseInt(scomposto[2]) - 1;
                                    if (numberOfPlayer < handler.getPlayer().size() && numberOfPlayer > -1) {
                                        comando = "Life = " + handler.getPlayer().get(numberOfPlayer).getLife();
                                    } else {
                                        errore = true;
                                        comando = "Error, the player number " + (numberOfPlayer + 1) + " does not exist";
                                    }
                                } else {
                                    errore = true;
                                    comando = "Error in get life, the player have not yetbeen initialized: " + comando;
                                }
                                break;
                            case "move":
                                if (handler.getPlayer() != null) {
                                    if (scomposto.length == 3) {
                                        int numberOfPlayer = Integer.parseInt(scomposto[2]) - 1;
                                        if (numberOfPlayer < handler.getPlayer().size() && numberOfPlayer > -1) {
                                            comando = "Move = " + handler.getPlayer().get(numberOfPlayer).getMoveXIncrease();
                                        } else {
                                            errore = true;
                                            comando = "Error, the player number " + (numberOfPlayer + 1) + " does not exist";
                                        }
                                    } else {
                                        errore = true;
                                        comando = "Error in get move command: " + comando;
                                    }
                                } else {
                                    errore = true;
                                    comando = "Error in get move, the player have not yet been initialized: " + comando;
                                }
                                break;
                            case "jump":
                                //comando = "Jump = " + Movement.jump + "px";
                                if (handler.getPlayer() != null) {
                                    if (scomposto.length == 3) {
                                        int numberOfPlayer = Integer.parseInt(scomposto[2]) - 1;
                                        if (numberOfPlayer < handler.getPlayer().size() && numberOfPlayer > -1) {
                                            comando = "Jump = " + handler.getPlayer().get(numberOfPlayer).getJumpIncrease();
                                        } else {
                                            errore = true;
                                            comando = "Error, the player number " + (numberOfPlayer + 1) + " does not exist";
                                        }
                                    } else {
                                        errore = true;
                                        comando = "Error in get jump command: " + comando;
                                    }
                                } else {
                                    errore = true;
                                    comando = "Error in get jump, the player have not yet been initialized: " + comando;
                                }
                                break;
                            case "god":
                                if (handler.getPlayer() != null) {
                                    if (scomposto[2].equals("mode") && scomposto.length == 4) {
                                        int numberOfPlayer = Integer.parseInt(scomposto[3]) - 1;
                                        if (numberOfPlayer < handler.getPlayer().size() && numberOfPlayer > -1) {
                                            comando = "God mode = " + handler.getPlayer().get(numberOfPlayer).getGodMode();
                                        } else {
                                            errore = true;
                                            comando = "Error, the player number " + (numberOfPlayer + 1) + " does not exist";
                                        }
                                    } else {
                                        errore = true;
                                        comando = "Error in get god command: " + comando;
                                    }
                                } else {
                                    errore = true;
                                    comando = "Error in get god, the player have not yet been initialized: " + comando;
                                }
                                break;
                            case "fly":
                                if (handler.getPlayer() != null) {
                                    if (scomposto[2].equals("mode") && scomposto.length == 4) {
                                        int numberOfPlayer = Integer.parseInt(scomposto[3]) - 1;
                                        if (numberOfPlayer < handler.getPlayer().size() && numberOfPlayer > -1) {
                                            comando = "fly mode = " + handler.getPlayer().get(numberOfPlayer).getInfiniteJump();
                                        } else {
                                            errore = true;
                                            comando = "Error, the player number " + (numberOfPlayer + 1) + " does not exist";
                                        }
                                    } else {
                                        errore = true;
                                        comando = "Error in get fly command: " + comando;
                                    }
                                } else {
                                    errore = true;
                                    comando = "Error in get fly, the player have not yet been initialized: " + comando;
                                }
                                break;
                            default:
                                comando = "Error in get command: " + comando;
                                errore = true;
                                break;
                        }
                    } else {
                        comando = "Error in get command: " + comando;
                        errore = true;
                    }
                    break;
                default:
                    errore = true;
                    comando = "Error, command not found: " + comando;
            }

            if (errore) {
                append(comando, DefaultFont.ERROR);
            } else {
                append(comando, DefaultFont.COMMAND);
            }
            logCommand.setText("");
        }
    }
}
