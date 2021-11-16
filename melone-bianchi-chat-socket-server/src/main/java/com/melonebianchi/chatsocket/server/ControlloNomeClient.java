package com.melonebianchi.chatsocket.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JTextArea;

/**
 * Questa Classe Gestisce i Client per la inizzializazione del Nome. Qui il Server attende la ricezzione del nome, dopo controlla all'interno della sua lista se il nome
 * è già presente. Dopo che il nome è univoco il Server lancia un Thread per gerstire il CLient
 */

public class ControlloNomeClient extends Thread 
{
    Socket client;                              //Socket
    BufferedReader inDalClient;                 //Stream per leggere i messaggi del Client
    DataOutputStream outVersoClient;            //Stream per inviare messaggi al Client
    String nomeClient;                          //Nome del Client
    ListaClient lista;                          //Classe per la lista e logica per i CLient
    boolean uscita;     
    JTextArea chatMessaggio;                    //Un'area di Testo per visualizzare la ricezzione e l'invio dei messaggi

    public ControlloNomeClient(Socket client, ListaClient lista, JTextArea chatMessaggio) 
    {
        this.client = client;
        this.lista = lista;
        this.chatMessaggio = chatMessaggio;
    }

    public void run() 
    {
        try 
        {   
            //Inizzializazione Stream
            inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outVersoClient = new DataOutputStream(client.getOutputStream());

            do 
            {
                nomeClient = inDalClient.readLine();    //Attendo messagio del Client

                if (lista.Input(nomeClient)) //Controllo se nella Chat è già presente il nome scelto dal Client
                {
                    uscita = true;
                    outVersoClient.writeBytes("Server: Utente Gia' Esistente" + "\n");  //Invio al Client il seguente messaggio
                } 
                else 
                {
                    uscita = false;
                    outVersoClient.writeBytes("OK" + "\n"); //Messaggio di Conferma che il nome inserito va bene.
                }

            } while (uscita);

            chatMessaggio.append("Server:$ Client Connesso: " + nomeClient + "\n"); //Visualizza nel terminale chi si è connesso

            ThreadClient avvioThread = new ThreadClient(client, lista, inDalClient, nomeClient, outVersoClient, chatMessaggio); 

            avvioThread.start();    //lancio del Thread

            lista.AggiungiClient(nomeClient, avvioThread); //Aggiungo il Client alla Lista

        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }

    }
}
