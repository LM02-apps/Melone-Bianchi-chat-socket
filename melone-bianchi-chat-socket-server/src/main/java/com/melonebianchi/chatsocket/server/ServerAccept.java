package com.melonebianchi.chatsocket.server;

import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;

/**
 * Questa classe ha il compito di gestire ogni Client che tenta di connettersi al Server. Una volta accettata la connessione 
 * Il Server lancia un Thread per gestire il controllo del Nome il cui si identificherà insieme agli altri Client.
 */

public class ServerAccept 
{
    private int porta;              // Porta del Server
    ServerSocket server = null;     // Variabile per avviare il server in una specifica Porta
    BufferedReader inDalClient;     //Stream per leggere i messaggi del Client
    String nomeClient;              //Variabile per contenere il Nome del Client
    String stringRicevuta = null;   // Variabile per l'input Client Stringa
    Socket client;                  //Socket
    ConsoleServer ConsoleServer;   
    JTextArea chatMessaggi = new JTextArea();   //Un'area di Testo per visualizzare la ricezzione e l'invio dei messaggi

    public ServerAccept(int porta) 
    {
        this.porta = porta;
    }

    public void Start() 
    {
        try 
        {
            server = new ServerSocket(porta); // Il Server si avvia aprendo la porta

            System.out.println("Server:$ Start");

            ListaClient lista = new ListaClient();  //Classe contenitore e Logica Del Server

            new ConsoleServer(server, lista, chatMessaggi); //Lancio Della Grafica per interagire con il Server

            for (;;) // For per instanziare un Thread ogni volta che si connette un client
            {
                System.out.println();
                chatMessaggi.append("\n" + "Server:$ In Attesa Sulla Porta 6778" + "\n");

                client = server.accept(); // Il Server attende

                ControlloNomeClient controlloNomeClient = new ControlloNomeClient(client, lista, chatMessaggi); //Classe per gestire i Client quando devono registrarsi

                controlloNomeClient.start(); //Lancio del Thread
            }

        } 
        catch (Exception e) 
        {
            System.out.println("Errore Durante La Connessione");
            System.exit(1);
        }
    }
}