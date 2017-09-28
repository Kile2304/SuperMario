package mario.rm.multigiocatore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LENOVO
 */
public class Server implements Runnable {

    public static final int PORT = 5000;    //PORTA IN CUI SI DOVRANNO COLLEGARE I CLIENT

    public static final int SIZE = 8;   //GRANDEZZA GRIGLIA

    private Socket client;

    private int numberMaxOfPlayer;

    public Server(int numberMaxOfPlayer) {
        this.numberMaxOfPlayer = numberMaxOfPlayer;
        try {
            System.out.println("S)Creo il server");
            ServerSocket server = new ServerSocket(PORT);   //CREO SERVER

            System.out.println("S)Mi metto in attesa del client");
            Socket client = server.accept();   //MI METTO IN ATTESA DI UN SECONDO CLIENT
            new Thread().start();    //FACCIO PARTIRE THREAD SECONDO CLIENT
            System.out.println("S)Ho creato il thread del secondo client");

        } catch (IOException ex) {
            System.out.println("Errore nella creazione del server alla porta: " + PORT);
        }
    }

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(PORT + 1);
            ArrayList<Integer> list = new ArrayList<>();
            for (int i = 0; i < numberMaxOfPlayer; i++) {
                Channel ch1 = null;
                try {
                    System.out.println("S)Aspetto il primo client");
                    client = server.accept();
                    if (i == numberMaxOfPlayer) {
                        ch1 = new Channel(client, Protocol.READY.getProtocol(), (i + 1));
                        list.add(ch1.getNumThread());
                        ch1.initializeList(list);
                    } else {
                        ch1 = new Channel(client, Protocol.WAIT.getProtocol(), (i + 1));
                        list.add(ch1.getNumThread());
                    }
                    new Thread(ch1).start();
                    System.out.println("S)Ho creato il thread del primo client");
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException e) {
            System.out.println("" + e);
        }
    }

    public static void main(String[] args) {
        new Server(4);
    }

}
