package com.melonebianchi.chatsocket.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JTextArea;

public class ControlloNomeClient extends Thread
{
    Socket client;
    BufferedReader inDalClient; 
    DataOutputStream outVersoClient;    
    String nomeClient;
    ListaClient lista;
    boolean uscita;
    JTextArea chatMessaggio;

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
          inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
          outVersoClient = new DataOutputStream(client.getOutputStream());

          do
           {   
               nomeClient = inDalClient.readLine();

               if(lista.Input(nomeClient))
               {
                   uscita = true;
                   outVersoClient.writeBytes("Server: Utente Gia' Esistente" + "\n");
               }
               else
               {
                   uscita = false;
                   outVersoClient.writeBytes("OK" + "\n");
               }

           }while(uscita);

           chatMessaggio.append("Server:$ Client Connesso: " + nomeClient + "\n");

          ThreadClient avvioThread = new ThreadClient(client, lista, inDalClient, nomeClient, outVersoClient, chatMessaggio);

          avvioThread.start();

          lista.AggiungiClient(nomeClient, avvioThread);

          
        } 
       catch (IOException e)
        {
          e.printStackTrace();
        } 
       
    }
}
