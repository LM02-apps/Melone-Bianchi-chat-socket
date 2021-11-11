package com.melonebianchi.chatsocket.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ControlloNomeClient extends Thread
{
    Socket client;
    BufferedReader inDalClient; 
    DataOutputStream outVersoClient;    
    String nomeClient;
    ListaClient lista;
    boolean uscita;

    public ControlloNomeClient(Socket client, ListaClient lista)
    {
        this.client = client;
        this.lista = lista;
    }

    public void run()
    {
        try
        {
          inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
          outVersoClient = new DataOutputStream(client.getOutputStream());

          do
           {   
               nomeClient = inDalClient.readLine();

               if(lista.Input(nomeClient))
               {
                   uscita = true;
                   outVersoClient.writeBytes("Utente Gia' Esistente" + "\n");
               }
               else
               {
                   uscita = false;
                   outVersoClient.writeBytes("OK" + "\n");
               }

           }while(uscita);

          System.out.println("Server: Client Connesso: " + nomeClient);

          ThreadClient avvioThread = new ThreadClient(client, lista, inDalClient, nomeClient, outVersoClient);

          avvioThread.start();

          lista.AggiungiClient(nomeClient, avvioThread);

          
        } 
       catch (IOException e)
        {
          e.printStackTrace();
        } 
       
    }
}
