package com.melonebianchi.chatsocket.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JTextArea;

/**
 * Questa classe gestisce la comunicazione del Client al Server. Riceve i comandi da parte del Client, interpretando il comando.
 * 
 * I Comandi sono i Seguenti:
 * 
 * /all messaggio --> Invia a tutti il Messaggio 
 * 
 * /private nomeClient Messaggio --> Invia il messaggio ad un Client Specifico in privato
 * 
 * /exit --> Disconnette Il Clienti
 */

public class ThreadClient extends Thread 
{
    String stringaRicevuta;                             //Variabile per contenere la stringa ricevuta dal Client
    String nomeClient;                                  //nome Client
    Socket client = null;                               //Socket
    ListaClient lista;                                  //ListaClient
    BufferedReader inDalClient;                         //Stream per leggere i messaggi da parte del Client
    DataOutputStream outVersoClient;                    //Stream per inviare i messaggi al Client
    boolean ciclo = true;
    JTextArea chatMessaggio;                            // Un'area di Testo per visualizzare la ricezzione e l'invio dei messaggi
    final static String messaggioAbbandono = "Ha Abbandonato La Chat"; // Messaggio di abbandono Chat

    public ThreadClient(Socket socket, ListaClient lista, BufferedReader inDalClient, String nomeClient, DataOutputStream outVersoClient, JTextArea chatMessaggio) // Costruttore
    {
        this.client = socket;
        this.lista = lista;
        this.inDalClient = inDalClient;
        this.nomeClient = nomeClient;
        this.outVersoClient = outVersoClient;
        this.chatMessaggio = chatMessaggio;
    }

    public void run() 
    {
        try 
        {
            currentThread().setName(nomeClient);   //Imposto il nome del Thread con quello del Client
            Comunica();     // Chiamata del metodo
        } 
        catch (Exception e) 
        {
            System.out.println("Errore Comunica() Thread");
            try 
            {
                ChiudiConnessione();        //Metodo che chiude la connessione con il Client
            } 
            catch (IOException e1) 
            {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Il Metodo utilizza un altro metodo (Comndo()) per interpretare i comandi che il Server deve eseguire. Prima però controlla se è presente un solo Client, Perché
     * se è presente solo un CLient non si possono inviare messaggi.
     * @throws IOException
     */

    public void Comunica() throws IOException 
    {

        lista.ListaUtenti();

        for (; ciclo;) 
        {
            chatMessaggio.append("Server:$" + "\n");

            stringaRicevuta = inDalClient.readLine();       //Attento un messaggio dal Client

            chatMessaggio.append("Server:$" + Thread.currentThread().getName() + ": " + stringaRicevuta + "\n");    //Stampo nel Terminale del Server il messaggio del Client

            if (lista.getSize() >= 2 || stringaRicevuta.equals("/exit"))    //Controllo se il Client vuole scollegarsi e se all'interno della CHat è presente un solo utente
            {
                Comando();      //Richiamo del metodo
            } 
            else 
            {
                outVersoClient.writeBytes("Impossibile Inviare Messagi Se Soli Connessi" + "\n");   //In caso ci sia solo un utente connesso il Server risponde con la seguente Stringa
            }
        }
    }


    /**
     * Il Metodo interpreta il comando da eseguire, se inviare il messaggio a tutti o solo a un CLient. Prima di inviare il messaggio viene utilizzato un metodo che pulisce la stringa
     * per togliere il comando e far arrivare ai CLient solo il messaggio.(Così è più chiaro leggerlo)
     * @throws IOException
     */


    public void Comando() throws IOException 
    {
        if (stringaRicevuta.contains("/all ")) //Il messaggio viene inoltrato a tutti se vera la condizione
        {
            PulisciStringa();       //Pulisco la stringa dal Comando
            lista.InvioTotale(Thread.currentThread().getName(), stringaRicevuta);   //Richiamo il metodo che si trova all'interno della Classe ListaClient
        } 
        else if (stringaRicevuta.contains("/private "))     //Il Messaggio viene inoltrato al destinatario interessato se la condizione è vera 
        {
            String nomeUtente = "";

            PulisciStringa();           //Pulisco la stringa dal Comando

            for (int i = 0; i < stringaRicevuta.length(); i++) 
            {
                if (stringaRicevuta.charAt(i) != ' ') 
                {   
                    nomeUtente += stringaRicevuta.charAt(i);        //Prendo il Nome Utente
                } 
                else 
                {
                    stringaRicevuta = stringaRicevuta.replace(nomeUtente, "");      //Tolgo il nome Utente del Destinatario dal Messaggio per avere solo il messaggio da inoltrare al Client interessato
                    break;
                }
            }

            lista.InvioPrivato(stringaRicevuta, nomeUtente, Thread.currentThread().getName());      //Richiamo Il metodo dalla Classe ListaClient

        } 
        else if (stringaRicevuta.contains("/exit"))         //Il Client vuole Disconnettersi
        {
            ChiudiConnessione();
        }
    }

    /**
     * Il Metodo gestisce la Chiusura di tutti gli Stream e del Socket, inviando anche un ACK di conferma al Client per potersi sconnettere. 
     * Inoltre viene eliminato dalla Lista.
     * @throws IOException
     */

    public void ChiudiConnessione() throws IOException 
    {
        lista.RimuoviClient(Thread.currentThread().getName());
        lista.InvioTotale(Thread.currentThread().getName(), " " + messaggioAbbandono);
        lista.ListaUtenti();
        outVersoClient.writeBytes("END");
        outVersoClient.close();
        inDalClient.close();
        client.close();
        ciclo = false;
    }

    /**
     * Invia al Client il messaggio
     * @param messaggio
     * @throws IOException
     */

    public void InviaClient(String messaggio) throws IOException 
    {
        outVersoClient.writeBytes(Thread.currentThread().getName() + ": " + messaggio + "\n");
    }

    /**
     * Invia la lista degli utenti connessi nella Chat ogni volta che un Client si unisce o abbandona la chat
     * @param lista
     * @throws IOException
     */

    public void InviaLista(String lista) throws IOException {
        outVersoClient.writeBytes("Utenti Connessi:" + lista + "\n");
    }

    /**
     * Pulisce la Stringa da caratteri che non sono esenziali per il Mittente
     */

    public void PulisciStringa() {

        stringaRicevuta = stringaRicevuta.replace("/all ", "");
        stringaRicevuta = stringaRicevuta.replace("/private ", "");
        stringaRicevuta = stringaRicevuta.replace("/exit", "");

    }
}
