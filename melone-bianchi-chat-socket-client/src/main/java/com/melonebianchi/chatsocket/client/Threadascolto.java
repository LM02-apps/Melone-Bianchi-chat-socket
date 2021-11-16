package com.melonebianchi.chatsocket.client;

import java.io.DataOutputStream;
import java.io.*;
import java.net.*;
import javax.swing.JTextArea;

/**
 * Questa classe che è un Thread, permette l'ascolto dei messaggi che il Server invia.
 * Quando il Client vuole disconnettersi il Server invia un ACK di conferma prima di chiudera la connessione.
 * ACK = END --> Quando il Client riceve questo messaggio dal Server, significa che può chiudere la connessione.
 */

public class Threadascolto extends Thread
{
    private BufferedReader inServer;    //Stream di input per inviare al Server
    private String rispostaServer;      //Variabile per contenere la risposta del Server
    DataOutputStream outServer;         //Stream per la ricezzione dei messaggi da parte del Server
    Socket socketClient;                //Socket
    JTextArea chatMessaggio = new JTextArea();  //Elemento gragfico per poter stampare a video i messaggi che arrivano.

    public Threadascolto(BufferedReader inServer, DataOutputStream outServer, Socket socketClient, JTextArea chatMessaggio)
    {
        this.inServer = inServer;
        this.outServer = outServer;
        this.socketClient = socketClient;
        this.chatMessaggio = chatMessaggio;
    }

    public void run()
    {
        try
        {
            for(;;)
            {
               
                rispostaServer = inServer.readLine();   //Attendo un messaggio dal Client

                if (rispostaServer.equals("END"))       //Controllo la conferma di potermi disconnettere da parte del Server
                {
                    chatMessaggio.append("Chiusura connessione..." + "\n");

                    inServer.close();       //Chiusura dello Stream
                    outServer.close();      //Chiusura dello Stream
                    socketClient.close();   //Chiusura del Socket

                    chatMessaggio.append("Connessione Terminata" + "\n");

                    break;
                }
                
                rispostaServer = rispostaServer.replace("AWT-EventQueue-0:", "Server:");
                chatMessaggio.append(rispostaServer + "\n");

            }
            
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    
}
