package com.melonebianchi.chatsocket.client;

import java.io.DataOutputStream;
import java.io.*;
import java.net.*;

public class Threadascolto extends Thread
{
    private BufferedReader inServer;
    private String rispostaServer;
    DataOutputStream outServer;
    Socket socketClient;

    public Threadascolto(BufferedReader inServer, DataOutputStream outServer, Socket socketClient) {
        this.inServer = inServer;
        this.outServer = outServer;
        this.socketClient = socketClient;
    }

    public void run()
    {
        
        try
        {
            for(;;)
            {
               
                rispostaServer=inServer.readLine();

                if (rispostaServer.equals("END"))
                {
                    System.out.println("Chiusura connessione...");

                    inServer.close();
                    outServer.close();
                    socketClient.close();

                    System.out.println("Connessione Terminata");

                    break;
                }
                rispostaServer = rispostaServer.replace("Thread-0:", "Server:");
                System.out.println(rispostaServer);

            }
            
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    
}
