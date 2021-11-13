package com.melonebianchi.chatsocket.server;

import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;

public class ServerAccept
{
    private int porta;                  //Porta del Server
    ServerSocket server = null;         //Variabile per avviare il server in una specifica Porta
    BufferedReader inDalClient; 
    String nomeClient;
    String stringRicevuta = null;       //Variabile per l'input Client Stringa
    Socket client;
    ConsoleServer ConsoleServer;
    JTextArea chatMessaggi = new JTextArea();    

    public ServerAccept(int porta) 
    {
        this.porta = porta;
    } 

    public void Start()
    {
        try
        {
            server = new ServerSocket(porta);   //Il Server si avvia aprendo la porta

            System.out.println("Server:$ Start");

            ListaClient lista = new ListaClient();

            
            new ConsoleServer(server, lista, chatMessaggi);

            for(;;) //For per instanziare un Thread ogni volta che si connette un client
            {
                System.out.println();
                chatMessaggi.append("\n" + "Server:$ In Attesa..." + "\n");

                  client = server.accept();    //Il Server attende

                ControlloNomeClient controlloNomeClient = new ControlloNomeClient(client, lista, chatMessaggi);

                controlloNomeClient.start();
            }

        }
        catch(Exception e)
        {
            System.out.println("Errore Durante La Connessione");
            System.exit(1);
        }
    }

   
}