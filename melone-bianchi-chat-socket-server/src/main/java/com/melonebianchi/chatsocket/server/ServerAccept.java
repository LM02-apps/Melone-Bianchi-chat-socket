package com.melonebianchi.chatsocket.server;

import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerAccept
{
    private int porta;                  //Porta del Server
    ServerSocket server = null;         //Variabile per avviare il server in una specifica Porta
    BufferedReader inDalClient; 
    String nomeClient;
    String stringRicevuta = null;       //Variabile per l'input Client Stringa
    Socket client;
    

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

            ConsoleServer ConsoleServer = new ConsoleServer(server, lista);

            ConsoleServer.start();

            for(;;) //For per instanziare un Thread ogni volta che si connette un client
            {
                System.out.println("Server:$ In Attesa...");

                  client = server.accept();    //Il Server attende

                ControlloNomeClient controlloNomeClient = new ControlloNomeClient(client, lista);

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