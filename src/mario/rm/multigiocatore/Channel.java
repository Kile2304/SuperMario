package mario.rm.multigiocatore;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static mario.rm.multigiocatore.Protocol.*;

/**
 *
 * @author LENOVO
 */
public class Channel implements Runnable {  //CREO UN CANALE DI COMUNICAZIONE CON IL CLIENT

    private Socket client;  //SOCKET DEL CLIENT

    private DataOutputStream print;   //VARIABILE PER INVIARE MESSAGGI AL CLIENT
    private DataInputStream input; //VARIABILE PER RICEVERE MESSAGGI DAL CLIENT

    private static ArrayList<DataOutputStream> share = new ArrayList<>();   //SICCOME MI SERVE POTER COMUNICARE CON ENTRAMBI I CLIENT DEVO CONDIVIDERE L'OUTPUT

    private boolean inGame;   //IN CASO CHE QUESTA SIA FALSE IL THREAD FINISCE

    private static int numThread = 0;  //VARIABILE FATTA PER CALCOLARE LA POSIZIONE DELLA VARIABIILE CONDIVISA NELL'ARRAYLLYST

    private int messaggio;

    public int currentPlayer;

    public int[] playerListIndex;

    public Channel(Socket client, int messaggio, int currentPlayer) {  //MEMORIZZO I DATI DEL CLIENT E GLI DICO SE PARTIRE O NO (READY/WAIT)
        this.client = client;
        this.messaggio = messaggio;
        inGame = true;  //IL CLIENT IN READY LO METTERA' IN TRUE, DOPO DI CHE UNA VOLTA CHE LA PARTITA E' FINITA TORNERA' FALSE E I DUE THREAD TERMINERANNO
        numThread++;
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void run() {
        int size = Server.SIZE; //AMPIEZZA DELLA MATRICE
        int index;  //INDICE DELLA CONNESSIONE CONDIVISA
        share.add(print);
        try {
            print = new DataOutputStream(client.getOutputStream());   //VARIABILE PER INVIARE MESSAGGI
            input = new DataInputStream(client.getInputStream());  //VARIABILE PER RICEVERE MESSAGGI

            if (messaggio == Protocol.WAIT.getProtocol()) {    //SE DEVE ASPETTARE (WAIT)
                share.add(print);   //AGGIUNGE LA MEMORIA CONDIVISA SULL'ARRAYLSIT
                System.out.println("C " + numThread + ")Mi metto in attesa di un secondo client");
                print.writeInt(messaggio); //GLI DICO AL CLIENT DI ASPETTARE
                messaggio = input.readInt();  //ASPETTO IL READY O QUALSIASI MESSAGGIO PER PARTIRE
                
                int numberOfPlayer = input.readInt();
                playerListIndex = new int[numberOfPlayer];
                for (int i = 0; i < playerListIndex.length; i++) {
                    playerListIndex[i] = input.readInt();
                }
                
                System.out.println("C " + numThread + ")Mi e' arrivato un messaggio di conferma dell'arrivo di un client");
            } else if (messaggio == Protocol.READY.getProtocol()) {  //NEL CASO IL CLIENT ABBIA READY, POTREBBE COMINCIARE
                //System.out.println("C " + numThread + ")Sono il thread del secondo client, posso cominciare, invio i messaggi ai client");
                print.writeInt(messaggio);  //DICO AL CLIENT DI PARTIRE
                
                for (int i = 0; i < playerListIndex.length; i++) {  //DICO AI VARI THREAD DEL SERVER DI PARTIRE E GLI SPECIFICO L'ELENCO DEI VARI CANALI DI COMUNICAZIONE
                    if(playerListIndex[i] != share.indexOf(print)){
                        share.get(playerListIndex[i]).writeInt(messaggio); //GLI DICO AL CANALE IN ATTESA DI PARTIRE
                        share.get(playerListIndex[i]).writeInt(playerListIndex.length); //GLI DICO AL SERVER QUANTI PLAYER STANO GIOCANDO
                        for (int j = 0; j < playerListIndex.length; j++) {
                            share.get(playerListIndex[i]).writeInt(playerListIndex[j]);
                        }
                    }
                }
            }
            inGame = true;   //ATTIVO IL LOOP
            while (inGame) {   //LOOP DEL GIOCO
                //System.out.println("C " + numThread + ")Aspetto comandi");
                messaggio = input.readInt();  //ATTENDO UN COMANDO
                //System.out.println("" + messaggio);
                if (messaggio == OK.getProtocol()) {
                    messaggio = input.readInt();
                    switch (messaggio) {
                        case 200:   //SE VIENE INVIATO IL COMANDO READY CAMBIO IL TURNO DA UN CLIENT ALL'ALTRO
                            //System.out.println("C " + numThread + ")Cambio turno");
                            print.writeInt(Protocol.WAIT.getProtocol());    //DICO AL CLIENT CHE HA APPENA EFFETTUATO IL TURNO DI ANDARE IN WAIT
                            //share.get(playerListIndex[i]).writeInt(Protocol.READY.getProtocol());   //DICO AL CLIENT CHE NON AVEVA FATTO L'ATTACCO DI ATTACCARE
                            //share.set(index, print);  //FACCIO IN MODO CHE IL THREAD POSSA COMUNICARE CON IL CLIENT DEL THREAD DEL SERVER CHE HA EFETTUATO QUESTA ASSEGNAZIONE
                            break;
                        case 400:    //LE COORDINATE
                            messaggio = input.readInt();  //IN ATTESA DELLA CASELLA
                            //share.get(index).writeInt(messaggio);   //INVIA AL DIFENSORE LE COORDINATE IN CUI E' STATO ATTACCATO
                            //share.set(index, print);  //FACCIO IN MODO CHE IL THREAD POSSA COMUNICARE CON IL CLIENT DEL THREAD DEL SERVER CHE HA EFETTUATO QUESTA ASSEGNAZIONE
                            System.out.println("c " + numThread + ")Maiuscolo corretto");
                            break;
                        default:
                            break;
                    }
                }
            }
            print.close();
            input.close();
            client.close();
            share.get(share.indexOf(print)).close();
        } catch (IOException ex) {
            System.out.println("Client disconnesso");
        }
    }

    public void initializeList(ArrayList<Integer> list) {
        playerListIndex = new int[list.size()];
        for (int i = 0; i < playerListIndex.length; i++) {
            playerListIndex[i] = list.get(i);
        }
    }

    public void getListIndex(int[] list) {
        playerListIndex = list;
    }

    public int[] getListIndex() {
        return playerListIndex;
    }

    public int getNumThread() {
        return share.indexOf(print);
    }

}
